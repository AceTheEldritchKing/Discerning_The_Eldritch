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

    // Mend Flesh
    public static DeferredHolder<SoundEvent, SoundEvent> MEND_FLESH_SOUND = registerSoundEvent("mend_flesh");

    // Insanity Voices
    public static DeferredHolder<SoundEvent, SoundEvent> INSANITY_VOICES = registerSoundEvent("insanity_voices");

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
