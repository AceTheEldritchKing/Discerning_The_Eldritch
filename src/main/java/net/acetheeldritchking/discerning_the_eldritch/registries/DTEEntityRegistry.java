package net.acetheeldritchking.discerning_the_eldritch.registries;

import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.ApothicAcolyteEntity;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.ApothicCrusaderEntity;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.ApothicSummonerEntity;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.GaolerEntity;
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
                    .sized(5f, 1f)
                    .clientTrackingRange(64)
                    .build(ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "esoteric_edge").toString())
            );

    // Apothic Summoner
    public static final DeferredHolder<EntityType<?>, EntityType<ApothicSummonerEntity>> APOTHIC_SUMMONER =
            ENTITIES.register("apothic_summoner", () -> EntityType.Builder.of(ApothicSummonerEntity::new, MobCategory.MONSTER)
                    .sized(.6f, 1.8f)
                    .clientTrackingRange(64)
                    .build(ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "apothic_summoner").toString())
            );

    // Apothic Crusader
    public static final DeferredHolder<EntityType<?>, EntityType<ApothicCrusaderEntity>> APOTHIC_CRUSADER =
            ENTITIES.register("apothic_crusader", () -> EntityType.Builder.of(ApothicCrusaderEntity::new, MobCategory.MONSTER)
                    .sized(.6f, 1.8f)
                    .clientTrackingRange(64)
                    .build(ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "apothic_crusader").toString())
            );

    // Apothic Acolyte
    public static final DeferredHolder<EntityType<?>, EntityType<ApothicAcolyteEntity>> APOTHIC_ACOLYTE =
            ENTITIES.register("apothic_acolyte", () -> EntityType.Builder.of(ApothicAcolyteEntity::new, MobCategory.MONSTER)
                    .sized(.6f, 1.8f)
                    .clientTrackingRange(64)
                    .build(ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "apothic_acolyte").toString())
            );

    // Sightless Maw

    // Untold Behemoth

    // The Apostle

    // Gaoler
    public static final DeferredHolder<EntityType<?>, EntityType<GaolerEntity>> GAOLER_ENTITY =
            ENTITIES.register("gaoler", () -> EntityType.Builder.<GaolerEntity>of
                            (GaolerEntity::new, MobCategory.MONSTER).
                    sized(1.5f, 5f)
                    .build(
                            ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "gaoler").toString()
                    ));

    public static void register(IEventBus eventBus)
    {
        ENTITIES.register(eventBus);
    }
}
