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

    // Silence Spell Cast
    public static DeferredHolder<SoundEvent, SoundEvent> SILENCE_SPELL_CAST = registerSoundEvent("silence_cast");

    // Clap Spell Cast
    public static DeferredHolder<SoundEvent, SoundEvent> CLAP_SPELL_CAST = registerSoundEvent("clap");

    // Esoteric Edge
    public static DeferredHolder<SoundEvent, SoundEvent> ESOTERIC_EDGE_SLASH = registerSoundEvent("esoteric_edge_slash");

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

    // Test Boss Music
    public static DeferredHolder<SoundEvent, SoundEvent> TEST_BOSS_MUSIC = registerSoundEvent("test_boss_music");

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
