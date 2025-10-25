package net.acetheeldritchking.discerning_the_eldritch.registries;

import io.redspace.ironsspellbooks.fluids.NoopFluid;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class DTEFluidRegistry {
    private static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, DiscerningTheEldritch.MOD_ID);
    private static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, DiscerningTheEldritch.MOD_ID);

    // Fluids
    public static final DeferredHolder<FluidType, FluidType> LIQUID_MALICE_TYPE = FLUID_TYPES.register("liquid_malice", () -> new FluidType(FluidType.Properties.create()));

    // Fluid Types
    public static final DeferredHolder<Fluid, NoopFluid> LIQUID_MALICE = registerNoop("liquid_malice", LIQUID_MALICE_TYPE::value);

    // Forgive me for copying this over
    private static DeferredHolder<Fluid, NoopFluid> registerNoop(String name, Supplier<FluidType> fluidType) {
        DeferredHolder<Fluid, NoopFluid> holder = DeferredHolder.create(Registries.FLUID, DiscerningTheEldritch.id(name));
        BaseFlowingFluid.Properties properties = new BaseFlowingFluid.Properties(fluidType, holder::value, holder::value).bucket(() -> Items.AIR);
        FLUIDS.register(name, () -> new NoopFluid(properties));
        return holder;
    }

    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
        FLUID_TYPES.register(eventBus);
    }
}
