package net.acetheeldritchking.discerning_the_eldritch.registries;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.apothic_cultists.ApothicAcolyteEntity;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.apothic_cultists.ApothicCrusaderEntity;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.apothic_cultists.ApothicSummonerEntity;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.apothic_cultists.ApothicTraitorEntity;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.bosses.ascended_one.AscendedOneBoss;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.bosses.ascended_one.AscendedOneCultistEntity;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.eldritch_caster.TheApostleEntity;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.gaoler.GaolerEntity;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.sightless_maw.SightlessMawEntity;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.untold_behemoth.UntoldBehemothEntity;
import net.acetheeldritchking.discerning_the_eldritch.entity.spells.crystal_carve.CrystalCarveEntity;
import net.acetheeldritchking.discerning_the_eldritch.entity.spells.esoteric_edge.EsotericEdge;
import net.acetheeldritchking.discerning_the_eldritch.entity.spells.esoteric_strike.EsotericStrike;
import net.acetheeldritchking.discerning_the_eldritch.entity.spells.glacial_edge.GlacialEdge;
import net.acetheeldritchking.discerning_the_eldritch.entity.spells.razor_blade.RazorBlade;
import net.acetheeldritchking.discerning_the_eldritch.entity.spells.rift_walker.UnstableRiftEntity;
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
    public static final DeferredHolder<EntityType<?>, EntityType<UntoldBehemothEntity>> UNTOLD_BEHEMOTH =
            ENTITIES.register("untold_behemoth", () -> EntityType.Builder.<UntoldBehemothEntity>of
                            (UntoldBehemothEntity::new, MobCategory.MONSTER).
                    sized(.6f, 1.8f)
                    .build(
                            ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "untold_behemoth").toString()
                    ));

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

    // Glacial Edge
    public static final DeferredHolder<EntityType<?>, EntityType<GlacialEdge>> GLACIAL_EDGE =
            ENTITIES.register("glacial_edge", () -> EntityType.Builder.<GlacialEdge>of(GlacialEdge::new, MobCategory.MISC)
                    .sized(5f, 1f)
                    .clientTrackingRange(64)
                    .build(ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "glacial_edge").toString())
            );

    // Unstable Rift
    public static final DeferredHolder<EntityType<?>, EntityType<UnstableRiftEntity>> UNSTABLE_RIFT =
            ENTITIES.register("unstable_rift", () -> EntityType.Builder.<UnstableRiftEntity>of(UnstableRiftEntity::new, MobCategory.MISC)
                    .sized(1f, 1f)
                    .clientTrackingRange(64)
                    .build(ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "unstable_rift").toString()));

    // Esoteric Strike
    public static final DeferredHolder<EntityType<?>, EntityType<EsotericStrike>> ESOTERIC_STRIKE =
            ENTITIES.register("esoteric_strike", () -> EntityType.Builder.<EsotericStrike>of(EsotericStrike::new, MobCategory.MISC)
                    .sized(2f, 1f)
                    .clientTrackingRange(64)
                    .build(ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "esoteric_strike").toString())
            );

    // Apothic Traitor
    public static final DeferredHolder<EntityType<?>, EntityType<ApothicTraitorEntity>> APOTHIC_TRAITOR =
            ENTITIES.register("apothic_traitor", () -> EntityType.Builder.of(ApothicTraitorEntity::new, MobCategory.MONSTER)
                    .sized(.6f, 1.8f)
                    .clientTrackingRange(64)
                    .build(ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "apothic_traitor").toString())
            );

    // Crystal Carve
    public static final DeferredHolder<EntityType<?>, EntityType<CrystalCarveEntity>> CRYSTAL_CARVE =
            ENTITIES.register("crystal_carve", () -> EntityType.Builder.<CrystalCarveEntity>of(CrystalCarveEntity::new, MobCategory.MISC)
                    .sized(5f, 1f)
                    .clientTrackingRange(64)
                    .build(ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "crystal_carve").toString())
            );

    // Razor Blade
    public static final DeferredHolder<EntityType<?>, EntityType<RazorBlade>> RAZOR_BLADE =
            ENTITIES.register("razor_blade", () -> EntityType.Builder.<RazorBlade>of
                            (RazorBlade::new, MobCategory.MISC).
                    sized(1f, 2f)
                    .build(
                            ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "razor_blade").toString()
                    ));


    public static void register(IEventBus eventBus)
    {
        ENTITIES.register(eventBus);
    }
}
