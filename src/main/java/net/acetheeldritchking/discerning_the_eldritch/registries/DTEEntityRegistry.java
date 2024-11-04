package net.acetheeldritchking.discerning_the_eldritch.registries;

import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.entity.spells.esoteric_edge.EsotericEdge;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DTEEntityRegistry {
    private static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, DiscerningTheEldritch.MOD_ID);

    // Esoteric Edge
    public static final DeferredHolder<EntityType<?>, EntityType<EsotericEdge>> ESOTERIC_EDGE =
            ENTITIES.register("esoteric_edge", () -> EntityType.Builder.<EsotericEdge>of(EsotericEdge::new, MobCategory.MISC)
                    .sized(1f, 1f)
                    .clientTrackingRange(64)
                    .build(ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "esoteric_edge").toString())
            );

    public static void register(IEventBus eventBus)
    {
        ENTITIES.register(eventBus);
    }
}
