package net.acetheeldritchking.discerning_the_eldritch.registries;

import com.google.common.collect.ImmutableSet;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set;

public class DTEPoiRegistry {
    private static final DeferredRegister<PoiType> POIS = DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, DiscerningTheEldritch.MOD_ID);

    public static final DeferredHolder<PoiType, PoiType> RESONATING_DEEPSLATE_POI = POIS.register("resonating_deepslate",
            () -> new PoiType(
                    getBlockStates(BlockRegistries.RESONATING_DEEPSLATE.get()),
                    1,
                    1
            ));

    private static Set<BlockState> getBlockStates(Block block)
    {
        return ImmutableSet.copyOf(block.getStateDefinition().getPossibleStates());
    }

    public static void register(IEventBus eventBus)
    {
        POIS.register(eventBus);
    }
}
