package net.acetheeldritchking.discerning_the_eldritch.registeries;

import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DTESoundRegistry {
    private static final DeferredRegister<SoundEvent> SOUND_EVENT =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, DiscerningTheEldritch.MOD_ID);

    public static RegistryObject<SoundEvent> SILENCE_SPELL_CAST = registerSoundEvent("silence_cast");

    public static void register(IEventBus eventBus)
    {
        SOUND_EVENT.register(eventBus);
    }

    private static RegistryObject<SoundEvent> registerSoundEvent(String name)
    {
        return SOUND_EVENT.register(name, () ->
                new SoundEvent(new ResourceLocation(DiscerningTheEldritch.MOD_ID, name)));
    }
}
