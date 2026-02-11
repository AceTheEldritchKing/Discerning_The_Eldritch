package net.acetheeldritchking.discerning_the_eldritch.registries;

import com.mojang.serialization.Codec;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class DTEAttachmentRegistry {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, DiscerningTheEldritch.MOD_ID);

    // Insanity System
    public static final Supplier<AttachmentType<Integer>> INSANITY_METER = ATTACHMENT_TYPES.register(
            "insanity_meter", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).copyOnDeath().build());

    public static final Supplier<AttachmentType<Boolean>> IS_INSANE = ATTACHMENT_TYPES.register(
            "is_insane", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).copyOnDeath().build());

    // Frostbite Stacking
    public static final Supplier<AttachmentType<Integer>> FROSTBITE_LEVEL = ATTACHMENT_TYPES.register(
            "frostbite_level", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build());

    // Devourer Consumption
    public static final Supplier<AttachmentType<Integer>> DEVOURED_ENTITIES = ATTACHMENT_TYPES.register(
            "devoured_entities", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build());


    public static void register(IEventBus eventBus)
    {
        ATTACHMENT_TYPES.register(eventBus);
    }

}
