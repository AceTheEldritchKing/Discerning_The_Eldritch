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


    // Mend Flesh
    private static final ModConfigSpec.BooleanValue MEND_FLESH_LIFESTEAL = BUILDER
            .comment("Defines whether or not mend flesh will heal on entity hit. Default is true")
            .define("Mend flesh lifesteal", true);

    private static final ModConfigSpec.BooleanValue MEND_FLESH_EXP_GAIN = BUILDER
            .comment("Defines whether or not mend flesh will heal on XP gain. Default is true")
            .define("Mend flesh exp gain", true);

    // Insanity System
    private static final ModConfigSpec.BooleanValue ELDRITCH_INSANITY_SYSTEM = BUILDER
            .comment("Defines whether or not to enable the insanity system. Everytime an Eldritch spell is cast, it increases your insanity.")
            .comment("Default is false")
            .define("Enable insanity system", false);

    private static final ModConfigSpec.ConfigValue<Integer> ELDRITCH_MAX_INSANITY = BUILDER
            .comment("Defines the max value for insanity. At the max value, the insanity debuffs will be applied to the player.")
            .comment("Default is [15]")
            .define("Max value for insanity", 15);

    // Abracadabra Effects
    private static final ModConfigSpec.BooleanValue ENABLE_ABRACADABRA_DAMAGE_CAP = BUILDER
            .comment("Defines whether or not to enable Abracadabra's damage cap abilities.")
            .comment("Default is true")
            .define("Enable Abracadabra's damage cap", true);

    private static final ModConfigSpec.ConfigValue<Integer> ABRACADABRA_DAMAGE_CAP_VALUE = BUILDER
            .comment("Defines the base damage cap value for Abracadabra spell")
            .comment("Default is [80]")
            .define("Base damage cap", 80);

    private static final ModConfigSpec.BooleanValue ENABLE_ABRACADABRA_HEX_PREVENTION = BUILDER
            .comment("Defines whether or not to enable Abracadabra's hex prevention abilities. This prevents the user from gaining negative potion effects.")
            .comment("Default is true")
            .define("Enable Abracadabra's hex prevention", true);

    // Curio Configs //
    // Pyrium Sheath
    private static final ModConfigSpec.ConfigValue<Integer> PYRIUM_SHEATH_COOLDOWN = BUILDER
            .comment("Defines the cooldown in seconds value for the Pyrium Sheath's ability")
            .comment("Default is [5]")
            .define("Pyrium Sheath CD", 5);

    // Frostbourne Sheath
    private static final ModConfigSpec.ConfigValue<Integer> FROSTBOURNE_SHEATH_COOLDOWN = BUILDER
            .comment("Defines the cooldown in seconds value for the Frostbourne Sheath's ability")
            .comment("Default is [5]")
            .define("Frostbourne Sheath CD", 5);

    // Razor Sheath
    private static final ModConfigSpec.ConfigValue<Integer> RAZOR_SHEATH_COOLDOWN = BUILDER
            .comment("Defines the cooldown in seconds value for the Sheath of The Razor's ability")
            .comment("Default is [10]")
            .define("Sheath of The Razor CD", 10);

    private static final ModConfigSpec.ConfigValue<Integer> RAZOR_SHEATH_MULTIPLIER = BUILDER
            .comment("Defines the multiplier to be added when evaluating the damage from the razor blades")
            .comment("Math is: 2 + (base attack damage * (config value / 100))")
            .comment("Dividing by 100 gets the percentage of what the config value, so if you input 10, it will result in 0.10 aka 10%")
            .comment("Default is [10]")
            .define("Sheath of The Razor CD", 10);

    // Boss Damage Caps //
    // Ascended One
    private static final ModConfigSpec.BooleanValue ENABLED_ASCENDED_ONE_DAMAGE_CAP = BUILDER
            .comment("Defines whether or not to enable Ascended One's damage cap.")
            .comment("Default is true")
            .define("Enable Ascended One's damage cap", true);

    private static final ModConfigSpec.ConfigValue<Integer> ASCENDED_ONE_DAMAGE_CAP_VALUE = BUILDER
            .comment("Defines the max damage the Ascended One can take for the damage cap")
            .comment("Default is [100]")
            .define("Base Ascended One damage cap", 100);


    public static final ModConfigSpec SPEC = BUILDER.build();


    public static boolean mendFleshLifesteal;
    public static boolean mendFleshEXPGain;
    public static boolean enableInsanitySystem;
    public static int maxInsanityValue;
    public static int abracadabraDamageCap;
    public static boolean enableDamageCap;
    public static boolean enableHexPrevention;
    public static boolean enableAscendedOneDamageCap;
    public static int ascendedOneDamageCap;
    public static int pyriumSheathCooldown;
    public static int frostbourneSheathCooldown;
    public static int razorSheathCooldown;
    public static int razorSheathMultiplier;


    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        mendFleshLifesteal = MEND_FLESH_LIFESTEAL.get();
        mendFleshEXPGain = MEND_FLESH_EXP_GAIN.get();

        enableInsanitySystem = ELDRITCH_INSANITY_SYSTEM.get();
        maxInsanityValue = ELDRITCH_MAX_INSANITY.get();

        enableDamageCap = ENABLE_ABRACADABRA_DAMAGE_CAP.get();
        abracadabraDamageCap = ABRACADABRA_DAMAGE_CAP_VALUE.get();
        enableHexPrevention = ENABLE_ABRACADABRA_HEX_PREVENTION.get();

        pyriumSheathCooldown = PYRIUM_SHEATH_COOLDOWN.get();
        frostbourneSheathCooldown = FROSTBOURNE_SHEATH_COOLDOWN.get();
        razorSheathCooldown = RAZOR_SHEATH_COOLDOWN.get();
        razorSheathMultiplier = RAZOR_SHEATH_MULTIPLIER.get();

        enableAscendedOneDamageCap = ENABLED_ASCENDED_ONE_DAMAGE_CAP.get();
        ascendedOneDamageCap = ASCENDED_ONE_DAMAGE_CAP_VALUE.get();
    }
}
