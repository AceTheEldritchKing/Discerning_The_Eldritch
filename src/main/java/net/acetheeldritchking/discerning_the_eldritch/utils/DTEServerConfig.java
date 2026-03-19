package net.acetheeldritchking.discerning_the_eldritch.utils;

import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
@EventBusSubscriber(modid = DiscerningTheEldritch.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class DTEServerConfig
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();


    // Mend Flesh
    private static final ModConfigSpec.ConfigValue<Boolean> MEND_FLESH_LIFESTEAL = BUILDER
            .comment("Defines whether or not mend flesh will heal on entity hit. Default is true")
            .define("Mend flesh lifesteal", true);

    private static final ModConfigSpec.ConfigValue<Boolean> MEND_FLESH_EXP_GAIN = BUILDER
            .comment("Defines whether or not mend flesh will heal on XP gain. Default is true")
            .define("Mend flesh exp gain", true);

    // Insanity System
    private static final ModConfigSpec.ConfigValue<Boolean> ELDRITCH_INSANITY_SYSTEM = BUILDER
            .comment("Defines whether or not to enable the insanity system. Everytime an Eldritch spell is cast, it increases your insanity.")
            .comment("Default is false")
            .define("Enable insanity system", false);

    private static final ModConfigSpec.ConfigValue<Integer> ELDRITCH_MAX_INSANITY = BUILDER
            .comment("Defines the max value for insanity. At the max value, the insanity debuffs will be applied to the player.")
            .comment("Default is [15]")
            .define("Max value for insanity", 15);

    // Abracadabra Effects
    private static final ModConfigSpec.ConfigValue<Boolean> ENABLE_ABRACADABRA_DAMAGE_CAP = BUILDER
            .comment("Defines whether or not to enable Abracadabra's damage cap abilities.")
            .comment("Default is true")
            .define("Enable Abracadabra's damage cap", true);

    private static final ModConfigSpec.ConfigValue<Integer> ABRACADABRA_DAMAGE_CAP_VALUE = BUILDER
            .comment("Defines the base damage cap value for Abracadabra spell")
            .comment("Default is [80]")
            .define("Base damage cap", 80);

    private static final ModConfigSpec.ConfigValue<Boolean> ENABLE_ABRACADABRA_HEX_PREVENTION = BUILDER
            .comment("Defines whether or not to enable Abracadabra's hex prevention abilities. This prevents the user from gaining negative potion effects.")
            .comment("Default is true")
            .define("Enable Abracadabra's hex prevention", true);

    // Item Configs //
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
            .define("Sheath of The Razor Multiplier", 10);

    // Mourning Star
    private static final ModConfigSpec.ConfigValue<Double> MOURNING_STAR_MORTAL_DAMAGE_MULTIPLIER = BUILDER
            .comment("Defines the multiplier for the Mourning Star's mortal damage decree")
            .comment("Default is [5.5F]")
            .define("Mourning Star Damage Multiplier", 5.5);

    // Exorcist's Aegis
    private static final ModConfigSpec.ConfigValue<Double> EXORCISTS_AEGIS_FAIL_CHANCE = BUILDER
            .comment("Defines the fail chance for casting an Eldritch spell with Exorcist's Aegis equipped")
            .comment("Default is [25.0F]")
            .define("Exorcist's Aegis fail chance", 25.0);

    // Boss Damage Caps //
    // Ascended One
    private static final ModConfigSpec.ConfigValue<Boolean> ENABLED_ASCENDED_ONE_DAMAGE_CAP = BUILDER
            .comment("Defines whether or not to enable Ascended One's damage cap.")
            .comment("Default is true")
            .define("Enable Ascended One's damage cap", true);

    private static final ModConfigSpec.ConfigValue<Integer> ASCENDED_ONE_DAMAGE_CAP_VALUE = BUILDER
            .comment("Defines the max damage the Ascended One can take for the damage cap")
            .comment("Default is [100]")
            .define("Base Ascended One damage cap", 100);

    // Apostle of Sculk
    private static final ModConfigSpec.ConfigValue<Boolean> ENABLED_APOSTLE_OF_SCULK_DAMAGE_CAP = BUILDER
            .comment("Defines whether or not to enable Apostle of Sculk's damage cap.")
            .comment("Default is true")
            .define("Enable Apostle of Sculk damage cap", true);

    private static final ModConfigSpec.ConfigValue<Integer> APOSTLE_OF_SCULK_DAMAGE_CAP_VALUE = BUILDER
            .comment("Defines the max damage the Apostle of Sculk can take for the damage cap")
            .comment("Default is [100]")
            .define("Base Apostle of Sculk damage cap", 100);

    private static final ModConfigSpec.ConfigValue<Integer> APOSTLE_OF_SCULK_DAMAGE_THRESHOLD = BUILDER
            .comment("Defines the threshold damage for the Apostle of Sculk when enraged")
            .comment("This is the amount of damage you have to meet or surpass in order to damage the boss")
            .comment("NOTE: This NEEDS to be LOWER than than the damage cap, otherwise you will not be able to hurt the boss")
            .comment("Default is [25]")
            .define("Base Apostle of Sculk damage threshold", 25);

    // Trading //
    // Blood Cultist Aggro
    private static final ModConfigSpec.ConfigValue<Boolean> BLOOD_CULTIST_AGGRESSION = BUILDER
            .comment("Defines whether or not to enable blood cultist aggression.")
            .comment("This means blood cultists will attack you on sight if your blood spell power is below 1.15x.")
            .comment("Default is false")
            .define("Enable Blood Cultist aggression", false);

    // CLIENT //
    // Gaoler Screenshake
    private static final ModConfigSpec.ConfigValue<Boolean> ENABLE_GAOLER_SCREENSHAKE = BUILDER
            .comment("Defines whether or not to enable the Gaoler's walking screenshake.")
            .comment("Default is true")
            .define("Enable Gaoler's walking screenshake", true);


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
    public static boolean enableApostleOfSculkDamageCap;
    public static int apostleOfSculkDamageCap;
    public static int apostleOfSculkDamageThreshold;
    public static int pyriumSheathCooldown;
    public static int frostbourneSheathCooldown;
    public static int razorSheathCooldown;
    public static int razorSheathMultiplier;
    public static boolean gaolerWalkingScreenshake;
    public static double mourningStarMortalDamageMultiplier;
    public static boolean enableBloodCultistAggression;
    public static double exorcistsAegisFailChance;


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
        mourningStarMortalDamageMultiplier = MOURNING_STAR_MORTAL_DAMAGE_MULTIPLIER.get();
        exorcistsAegisFailChance = EXORCISTS_AEGIS_FAIL_CHANCE.get();

        enableAscendedOneDamageCap = ENABLED_ASCENDED_ONE_DAMAGE_CAP.get();
        ascendedOneDamageCap = ASCENDED_ONE_DAMAGE_CAP_VALUE.get();
        enableApostleOfSculkDamageCap = ENABLED_APOSTLE_OF_SCULK_DAMAGE_CAP.get();
        apostleOfSculkDamageCap = APOSTLE_OF_SCULK_DAMAGE_CAP_VALUE.get();
        apostleOfSculkDamageThreshold = APOSTLE_OF_SCULK_DAMAGE_THRESHOLD.get();

        enableBloodCultistAggression = BLOOD_CULTIST_AGGRESSION.get();

        gaolerWalkingScreenshake = ENABLE_GAOLER_SCREENSHAKE.get();
    }
}
