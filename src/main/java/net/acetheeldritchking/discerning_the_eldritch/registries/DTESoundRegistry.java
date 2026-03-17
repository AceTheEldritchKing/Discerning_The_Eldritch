package net.acetheeldritchking.discerning_the_eldritch.registries;

import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DTESoundRegistry {
    private static final DeferredRegister<SoundEvent> SOUND_EVENT = DeferredRegister.create(Registries.SOUND_EVENT, DiscerningTheEldritch.MOD_ID);

    /***
     * Spell Sounds
     */
    // Silence Spell Cast
    public static DeferredHolder<SoundEvent, SoundEvent> SILENCE_SPELL_CAST = registerSoundEvent("silence_cast");

    // Clap Spell Cast
    public static DeferredHolder<SoundEvent, SoundEvent> CLAP_SPELL_CAST = registerSoundEvent("clap");

    // Esoteric Edge
    public static DeferredHolder<SoundEvent, SoundEvent> ESOTERIC_EDGE_SLASH = registerSoundEvent("esoteric_edge_slash");

    // Esoteric Edge 2
    public static DeferredHolder<SoundEvent, SoundEvent> ESOTERIC_EDGE_SLASH_2 = registerSoundEvent("esoteric_edge_slash_2");

    // Mend Flesh
    public static DeferredHolder<SoundEvent, SoundEvent> MEND_FLESH_SOUND = registerSoundEvent("mend_flesh");

    // Insanity Voices
    public static DeferredHolder<SoundEvent, SoundEvent> INSANITY_VOICES = registerSoundEvent("insanity_voices");

    // Glacial Cleave Start

    // Glacial Cleave Finish

    // Glacial Cleave Full
    public static DeferredHolder<SoundEvent, SoundEvent> GLACIAL_CLEAVE_FULL = registerSoundEvent("glacial_cleave_full");

    // Rift Walker Cast
    public static DeferredHolder<SoundEvent, SoundEvent> RIFT_WALKER_CAST = registerSoundEvent("rift_walker_cast");

    // Sculk Lantern Toll
    public static DeferredHolder<SoundEvent, SoundEvent> SCULK_LANTERN_TOLL = registerSoundEvent("sculk_lantern_ominous_normal");

    // Sculk Lantern Toll Success
    public static DeferredHolder<SoundEvent, SoundEvent> SCULK_LANTERN_TOLL_SUCCESS = registerSoundEvent("sculk_lantern_ominous_success");

    // Esoteric Strike
    public static DeferredHolder<SoundEvent, SoundEvent> ESOTERIC_STRIKE = registerSoundEvent("esoteric_strike");

    // Crystalline Carver Base
    public static DeferredHolder<SoundEvent, SoundEvent> CRYSTALLINE_CARVER_BASE = registerSoundEvent("crystalline_carver_base");

    // Crystalline Carver Finish
    public static DeferredHolder<SoundEvent, SoundEvent> CRYSTALLINE_CARVER_FINISH = registerSoundEvent("crystalline_carver_finish");

    // Ymir Slash
    public static DeferredHolder<SoundEvent, SoundEvent> YMIR_SLASH = registerSoundEvent("ymir_slash");

    // Razor Unsheathed
    public static DeferredHolder<SoundEvent, SoundEvent> RAZOR_UNSHEATHED = registerSoundEvent("razor_unsheathed");

    // Razor Sheathed
    public static DeferredHolder<SoundEvent, SoundEvent> RAZOR_SHEATHED = registerSoundEvent("razor_sheathed");

    // Magic Sword Cast
    public static DeferredHolder<SoundEvent, SoundEvent> MAGIC_SWORD_CAST = registerSoundEvent("magic_sword_cast");

    // Devourer Wretch
    public static DeferredHolder<SoundEvent, SoundEvent> DEVOURER_WRETCH = registerSoundEvent("devourer_wretch");

    // Dark Magic Sword Cast
    public static DeferredHolder<SoundEvent, SoundEvent> DARK_MAGIC_SWORD_CAST = registerSoundEvent("dark_magic_sword_cast");

    // Dark Magic Charge 1
    public static DeferredHolder<SoundEvent, SoundEvent> DARK_MAGIC_CHARGE_1 = registerSoundEvent("dark_magic_charge_1");

    // Soul Slam
    public static DeferredHolder<SoundEvent, SoundEvent> SOUL_SLAM = registerSoundEvent("soul_slam");

    // Soul Slam Echo
    public static DeferredHolder<SoundEvent, SoundEvent> SOUL_SLAM_ECHO = registerSoundEvent("soul_slam_echo");

    // Magic Sword Blood Cast
    public static DeferredHolder<SoundEvent, SoundEvent> MAGIC_SWORD_BLOOD_CAST = registerSoundEvent("magic_sword_blood_cast");


    /***
     * Entity Sounds
     */
    // Gaoler Hurt
    public static DeferredHolder<SoundEvent, SoundEvent> GAOLER_HURT = registerSoundEvent("entity.gaoler.hurt");

    // Gaoler Death
    public static DeferredHolder<SoundEvent, SoundEvent> GAOLER_DEATH = registerSoundEvent("entity.gaoler.death");

    // Gaoler Step
    public static DeferredHolder<SoundEvent, SoundEvent> GAOLER_STEP = registerSoundEvent("entity.gaoler.step");

    // Gaoler Ambient
    public static DeferredHolder<SoundEvent, SoundEvent> GAOLER_AMBIENT = registerSoundEvent("entity.gaoler.ambient");

    // Gaoler Attack
    public static DeferredHolder<SoundEvent, SoundEvent> GAOLER_ATTACK_IMPACT = registerSoundEvent("entity.gaoler.attack_impact");

    // Gaoler Roar
    public static DeferredHolder<SoundEvent, SoundEvent> GAOLER_ROAR = registerSoundEvent("entity.gaoler.roar");

    // Ascended One Hurt
    public static DeferredHolder<SoundEvent, SoundEvent> ASCENDED_ONE_HURT = registerSoundEvent("entity.ascended_one.hurt");

    // Ascended One Death
    public static DeferredHolder<SoundEvent, SoundEvent> ASCENDED_ONE_DEATH = registerSoundEvent("entity.ascended_one.death");

    // Ascended One Taunt 1
    public static DeferredHolder<SoundEvent, SoundEvent> ASCENDED_ONE_TAUNT_ONE = registerSoundEvent("entity.ascended_one.taunt_1");

    // Ascended One Taunt 2
    public static DeferredHolder<SoundEvent, SoundEvent> ASCENDED_ONE_TAUNT_TWO = registerSoundEvent("entity.ascended_one.taunt_2");

    // Apostle of Sculk Hurt
    public static DeferredHolder<SoundEvent, SoundEvent> APOSTLE_OF_SCULK_HURT = registerSoundEvent("entity.apostle_of_sculk.hurt");


    /***
     * Music Sounds
     */
    // Test Boss Music
    public static DeferredHolder<SoundEvent, SoundEvent> TEST_BOSS_MUSIC = registerSoundEvent("test_boss_music");

    // Ascended One Theme
    public static DeferredHolder<SoundEvent, SoundEvent> ASCENDED_ONE_THEME = registerSoundEvent("ascended_one_theme");

    // Ascended One Transition
    public static DeferredHolder<SoundEvent, SoundEvent> ASCENDED_ONE_TRANSITION = registerSoundEvent("ascended_one_transition");

    // // Ascended One Final Phase - START
    public static DeferredHolder<SoundEvent, SoundEvent> ASCENDED_ONE_FINAL_PHASE = registerSoundEvent("ascended_one_final_phase");

    // Ascended One Final Phase - FULL
    public static DeferredHolder<SoundEvent, SoundEvent> ASCENDED_ONE_FINAL_PHASE_FULL = registerSoundEvent("ascended_one_final_phase_full");

    // Apostle of Sculk Theme
    public static DeferredHolder<SoundEvent, SoundEvent> APOSTLE_OF_SCULK_THEME = registerSoundEvent("apostle_of_sculk_theme");


    private static DeferredHolder<SoundEvent, SoundEvent> registerSoundEvent(String name)
    {
        return SOUND_EVENT.register(name, () -> SoundEvent.createVariableRangeEvent
                (ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus)
    {
        SOUND_EVENT.register(eventBus);
    }
}
