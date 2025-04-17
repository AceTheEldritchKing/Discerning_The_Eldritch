package net.acetheeldritchking.discerning_the_eldritch.utils;

import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
@EventBusSubscriber(modid = DiscerningTheEldritch.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class DTEConfig
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue MEND_FLESH_LIFESTEAL = BUILDER
            .comment("Defines whether or not mend flesh will heal on entity hit. Default is true")
            .define("Mend flesh lifesteal", true);

    private static final ModConfigSpec.BooleanValue MEND_FLESH_EXP_GAIN = BUILDER
            .comment("Defines whether or not mend flesh will heal on XP gain. Default is true")
            .define("Mend flesh exp gain", true);


    /*
    private static final ModConfigSpec.BooleanValue ELDRITCH_INSANITY_SYSTEM = BUILDER
            .comment("Defines whether or not to enable the insanity system. Everytime an Eldritch spell is cast, it increases your insanity.")
            .comment("Default is false")
            .define("Enable insanity system", false);

    private static final ModConfigSpec.ConfigValue<Integer> ELDRITCH_MAX_INSANITY = BUILDER
            .comment("Defines the max value for insanity. At the max value, the insanity debuffs will be applied to the player.")
            .define("Max value for insanity", 15);
    */

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean mendFleshLifesteal;
    public static boolean mendFleshEXPGain;
    public static boolean enableInsanitySystem;
    public static int maxInsanityValue;


    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        mendFleshLifesteal = MEND_FLESH_LIFESTEAL.get();
        mendFleshEXPGain = MEND_FLESH_EXP_GAIN.get();

        //enableInsanitySystem = ELDRITCH_INSANITY_SYSTEM.get();
        //maxInsanityValue = ELDRITCH_MAX_INSANITY.get();
    }
}
