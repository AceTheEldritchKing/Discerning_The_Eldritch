package net.acetheeldritchking.discerning_the_eldritch.registries;

import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.*;
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
    public static final DeferredHolder<EntityType<?>, EntityType<SightlessMawEntity>> SIGHTLESS_MAW =
            ENTITIES.register("sightless_maw", () -> EntityType.Builder.<SightlessMawEntity>of
                            (SightlessMawEntity::new, MobCategory.MONSTER).
                    sized(.6f, 1.8f)
                    .build(
                            ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "sightless_maw").toString()
                    ));

    // Untold Behemoth

    // The Apostle
    public static final DeferredHolder<EntityType<?>, EntityType<TheApostleEntity>> APOSTLE_ENTITY =
            ENTITIES.register("apostle", () -> EntityType.Builder.<TheApostleEntity>of
                            (TheApostleEntity::new, MobCategory.MONSTER).
                    sized(.6f, 1.8f)
                    .build(
                            ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "apostle").toString()
                    ));

    // Gaoler
    public static final DeferredHolder<EntityType<?>, EntityType<GaolerEntity>> GAOLER_ENTITY =
            ENTITIES.register("gaoler", () -> EntityType.Builder.<GaolerEntity>of
                            (GaolerEntity::new, MobCategory.MONSTER).
                    sized(3.5f, 5f)
                    .build(
                            ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "gaoler").toString()
                    ));

    // The Ascended One
    public static final DeferredHolder<EntityType<?>, EntityType<AscendedOneBoss>> ASCENDED_ONE =
            ENTITIES.register("ascended_one", () -> EntityType.Builder.<AscendedOneBoss>of(AscendedOneBoss::new, MobCategory.MONSTER)
                    .sized(.6f, 1.8f)
                    .clientTrackingRange(64)
                    .build(ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "ascended_one").toString())
            );

    public static final DeferredHolder<EntityType<?>, EntityType<AscendedOneCultistEntity>> ASCENDED_ONE_CULTIST =
            ENTITIES.register("ascended_one_cultist", () -> EntityType.Builder.of(AscendedOneCultistEntity::new, MobCategory.MISC)
                    .sized(.6f, 1.8f)
                    .clientTrackingRange(64)
                    .build(ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "ascended_one_cultist").toString())
            );

    public static void register(IEventBus eventBus)
    {
        ENTITIES.register(eventBus);
    }
}
