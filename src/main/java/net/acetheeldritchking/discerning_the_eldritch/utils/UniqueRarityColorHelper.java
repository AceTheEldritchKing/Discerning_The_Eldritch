package net.acetheeldritchking.discerning_the_eldritch.utils;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

public class UniqueRarityColorHelper {
    // Colors will be handled in the client class

    // how many ticks each effect takes to complete one full cycle
    private static final int WAVE_CYCLE_TICKS = 60;   // 3 seconds
    private static final int PULSE_CYCLE_TICKS = 100; // 5 seconds

    public static long clientTick = 0;

    private UniqueRarityColorHelper() {}

    /*
    Builds a MutableComponent by going through every character in the item name one at a time and
    assigning each character its own color, It computes two things before the loop.

    waveTime — a 0 to 1 float representing where we are in the 60 tick horizontal wave cycle.
    This advances forward every tick so the wave appears to move left to right across the text

    pulseTime / pulseStrength — same idea but for the 100 tick global pulse.
    pulseStrength is a 0 to 1 float that smoothly rises and falls using a sine curve,
    meaning the red-pink color fades in and out globally across all characters uniformly
     */

    public static MutableComponent createWaveGradient(String text, int firstColor, int secondColor)
    {
        MutableComponent result = Component.literal("");

        // where we are in the horizontal wave cycle as a 0-1 float
        float waveTime = (float)(clientTick % WAVE_CYCLE_TICKS) / (float) WAVE_CYCLE_TICKS;

        // where we are in the global pulse cycle as a 0-1 float
        float pulseTime = (float)(clientTick % PULSE_CYCLE_TICKS) / (float) PULSE_CYCLE_TICKS;

        // convert pulse position into a smooth 0->1->0 curve using sine
        // this makes the red-pink color phase in and out rather than just appear then disappear
        float pulseStrength = (float)(Math.sin(pulseTime * Math.PI * 2.0 - Math.PI / 2.0) * 0.5 + 0.5);

        int length = text.length();
        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);

            // get the smooth wave value for this character's position in the text
            float smoothWave = getSmoothWave((float) i, length, waveTime);

            // use that wave value to pick a color between aqua and yellow for this character
            int waveColor = interpolateColor(firstColor, secondColor, smoothWave);

            result.append(Component.literal(String.valueOf(c)).setStyle(Style.EMPTY.withColor(waveColor)));
        }

        return result;
    }

    public static MutableComponent createWaveGradient(String text, int firstColor, int secondColor, int blendColor)
    {
        MutableComponent result = Component.literal("");

        // where we are in the horizontal wave cycle as a 0-1 float
        float waveTime = (float)(clientTick % WAVE_CYCLE_TICKS) / (float) WAVE_CYCLE_TICKS;

        // where we are in the global pulse cycle as a 0-1 float
        float pulseTime = (float)(clientTick % PULSE_CYCLE_TICKS) / (float) PULSE_CYCLE_TICKS;

        // convert pulse position into a smooth 0->1->0 curve using sine
        // this makes the red-pink color phase in and out rather than just appear then disappear
        float pulseStrength = (float)(Math.sin(pulseTime * Math.PI * 2.0 - Math.PI / 2.0) * 0.5 + 0.5);

        int length = text.length();
        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);

            // get the smooth wave value for this character's position in the text
            float smoothWave = getSmoothWave((float) i, length, waveTime);

            // use that wave value to pick a color between aqua and yellow for this character
            int waveColor = interpolateColor(firstColor, secondColor, smoothWave);

            // blend the wave color toward red-pink based on how strong the global pulse is right now
            // pulseStrength is the same for every character so this effect is uniform across the whole name
            int finalColor = interpolateColor(waveColor, blendColor, pulseStrength);

            result.append(Component.literal(String.valueOf(c)).setStyle(Style.EMPTY.withColor(finalColor)));
        }

        return result;
    }

    private static float getSmoothWave(float i, int length, float waveTime) {
        // normalize character index to a 0-1 value based on its position in the text
        float charPosition = i / (float) Math.max(1, length - 1);

        // slight diagonal tilt so the wave travels across the text rather than all chars changing at once
        float diagonalOffset = charPosition * 0.3F;

        // combine character position, diagonal tilt, and current wave time into one 0-1 wave position
        float wavePosition = (charPosition - waveTime + diagonalOffset) % 1.0F;

        // keep the value positive after the modulo wraps it
        if (wavePosition < 0.0F) {
            wavePosition++;
        }

        // run through sine to get a smooth eased value instead of a linear one
        return (float)(Math.sin((double) wavePosition * Math.PI * 2.0 - Math.PI / 2.0) * 0.5 + 0.5);
    }

    /*
    blends two hex colors together. ratio is 0 to 1 — at 0 you fully get colorAQUA,
    at 1 you fully get colorYELLOW, at 0.5 you get an even mix.
    It works by splitting each color into its red, green, and blue components separately,
    blending each channel individually by the ratio, then recombining them back into a single hex color int
     */
    private static int interpolateColor(int color1, int color2, float ratio) {
        // clamp ratio to 0-1 so we never over or undershoot
        ratio = Math.max(0.0F, Math.min(1.0F, ratio));

        // split each color into its red, green, blue component
        int r1 = (color1 >> 16) & 0xFF;
        int g1 = (color1 >>  8) & 0xFF;
        int b1 =  color1        & 0xFF;
        int r2 = (color2 >> 16) & 0xFF;
        int g2 = (color2 >>  8) & 0xFF;
        int b2 =  color2        & 0xFF;

        // blend each component separately by the ratio
        int r = (int)(r1 + (r2 - r1) * ratio);
        int g = (int)(g1 + (g2 - g1) * ratio);
        int b = (int)(b1 + (b2 - b1) * ratio);

        // recombine the three components back into a single hex color int
        return (r << 16) | (g << 8) | b;
    }
}
