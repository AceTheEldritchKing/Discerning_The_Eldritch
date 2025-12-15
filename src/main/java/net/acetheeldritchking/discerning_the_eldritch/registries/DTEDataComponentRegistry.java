package net.acetheeldritchking.discerning_the_eldritch.registries;

import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public class DTEDataComponentRegistry {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister.createDataComponents(DiscerningTheEldritch.MOD_ID);

    public static DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> SOUL_FIRE_STACKS =
            register("soul_fire_stacks", builder -> builder.persistent(ExtraCodecs.POSITIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));

    private static <T>DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder)
    {
        return DATA_COMPONENT_TYPES.register(name, () -> builder.apply(DataComponentType.builder()).build());
    }

    public static void register(IEventBus eventBus)
    {
        DATA_COMPONENT_TYPES.register(eventBus);
    }
}
