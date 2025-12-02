package net.acetheeldritchking.discerning_the_eldritch.events;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.fluids.SimpleClientFluidType;
import io.redspace.ironsspellbooks.item.weapons.StaffItem;
import io.redspace.ironsspellbooks.render.ClientStaffItemExtensions;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.gaoler.GaolerModel;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.sightless_maw.SightlessMawModel;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.eldritch_caster.TheApostleModel;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.untold_behemoth.UntoldBehemothModel;
import net.acetheeldritchking.discerning_the_eldritch.entity.render.mobs.*;
import net.acetheeldritchking.discerning_the_eldritch.entity.spells.blade_of_rancor.BladeOfRancorRenderer;
import net.acetheeldritchking.discerning_the_eldritch.entity.spells.cataclysm_blade_projectile.CataclysmBladeBigRenderer;
import net.acetheeldritchking.discerning_the_eldritch.entity.spells.cataclysm_blade_projectile.CataclysmBladeSmallRenderer;
import net.acetheeldritchking.discerning_the_eldritch.entity.spells.crystal_carve.CrystalCarveRenderer;
import net.acetheeldritchking.discerning_the_eldritch.entity.spells.esoteric_edge.EsotericEdgeRenderer;
import net.acetheeldritchking.discerning_the_eldritch.entity.spells.esoteric_strike.EsotericStrikeRenderer;
import net.acetheeldritchking.discerning_the_eldritch.entity.spells.glacial_edge.GlacialEdgeRenderer;
import net.acetheeldritchking.discerning_the_eldritch.entity.spells.ravenous_jaw.RavenousJawRenderer;
import net.acetheeldritchking.discerning_the_eldritch.entity.spells.razor_blade.RazorBladeRenderer;
import net.acetheeldritchking.discerning_the_eldritch.particle.*;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEEntityRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEFluidRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEParticleRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.ItemRegistries;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

@EventBusSubscriber(modid = DiscerningTheEldritch.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void registerRenderer(EntityRenderersEvent.RegisterRenderers event)
    {
        event.registerEntityRenderer(DTEEntityRegistry.ESOTERIC_EDGE.get(), EsotericEdgeRenderer::new);
        event.registerEntityRenderer(DTEEntityRegistry.APOTHIC_SUMMONER.get(), ApothicSummonerRenderer::new);
        event.registerEntityRenderer(DTEEntityRegistry.APOTHIC_CRUSADER.get(), ApothicCrusaderRenderer::new);
        event.registerEntityRenderer(DTEEntityRegistry.APOTHIC_ACOLYTE.get(), ApothicAcolyteRenderer::new);
        event.registerEntityRenderer(DTEEntityRegistry.ASCENDED_ONE.get(), AscendedOneRenderer::new);
        event.registerEntityRenderer(DTEEntityRegistry.ASCENDED_ONE_CULTIST.get(), AscendedOneRenderer::new);
        event.registerEntityRenderer(DTEEntityRegistry.GAOLER_ENTITY.get(), context -> {return new GaolerRenderer(context, new GaolerModel());});
        event.registerEntityRenderer(DTEEntityRegistry.APOSTLE_ENTITY.get(), context -> {return new TheApostleRenderer(context, new TheApostleModel());});
        event.registerEntityRenderer(DTEEntityRegistry.SIGHTLESS_MAW.get(), context -> {return new SightlessMawRenderer(context, new SightlessMawModel());});
        event.registerEntityRenderer(DTEEntityRegistry.UNTOLD_BEHEMOTH.get(), context -> {return new UntoldBehemothRenderer(context, new UntoldBehemothModel());});
        event.registerEntityRenderer(DTEEntityRegistry.GLACIAL_EDGE.get(), GlacialEdgeRenderer::new);
        event.registerEntityRenderer(DTEEntityRegistry.UNSTABLE_RIFT.get(), NoopRenderer::new);
        event.registerEntityRenderer(DTEEntityRegistry.ESOTERIC_STRIKE.get(), EsotericStrikeRenderer::new);
        event.registerEntityRenderer(DTEEntityRegistry.APOTHIC_TRAITOR.get(), ApothicTraitorRenderer::new);
        event.registerEntityRenderer(DTEEntityRegistry.CRYSTAL_CARVE.get(), CrystalCarveRenderer::new);
        event.registerEntityRenderer(DTEEntityRegistry.RAZOR_BLADE.get(), RazorBladeRenderer::new);
        event.registerEntityRenderer(DTEEntityRegistry.BLOOD_CULTIST_CAPTAIN.get(), BloodCultistCaptainRenderer::new);
        event.registerEntityRenderer(DTEEntityRegistry.BLADE_OF_RANCOR.get(), BladeOfRancorRenderer::new);
        event.registerEntityRenderer(DTEEntityRegistry.GORE_BILE_AOE.get(), NoopRenderer::new);
        event.registerEntityRenderer(DTEEntityRegistry.CATACLYSM_BLADE_SMALL.get(), CataclysmBladeSmallRenderer::new);
        event.registerEntityRenderer(DTEEntityRegistry.CATACLYSM_BLADE_BIG.get(), CataclysmBladeBigRenderer::new);
        event.registerEntityRenderer(DTEEntityRegistry.RAVENOUS_JAW.get(), RavenousJawRenderer::new);
    }

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event)
    {
        // Items
        event.registerItem(new ClientStaffItemExtensions(), ItemRegistries.getDTEItems().stream().filter(item -> item.get() instanceof StaffItem staffItem && !staffItem.hasCustomRendering()).map(holder -> (Item) holder.get()).toArray(Item[]::new));

        // Fluids
        event.registerFluidType(new SimpleClientFluidType(DiscerningTheEldritch.id("block/liquid_malice")), DTEFluidRegistry.LIQUID_MALICE_TYPE);
    }

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event)
    {
        event.registerSpriteSet(DTEParticleRegistry.GLACIAL_SHADOW_PARTICLE.get(), GlacialShadowParticle.Provider::new);
        event.registerSpriteSet(DTEParticleRegistry.ESOTERIC_SPARKS_PARTICLE.get(), EsotericSparksParticle.Provider::new);
        event.registerSpriteSet(DTEParticleRegistry.RIFT_SLICE_PARTICLE.get(), RiftSliceParticle.Provider::new);
        event.registerSpriteSet(DTEParticleRegistry.MALIGNANT_SOUL.get(), MalignantSoulParticle.Provider::new);
        event.registerSpriteSet(DTEParticleRegistry.MALIGNANT_FLAME.get(), MalignantFlameParticle.Provider::new);
    }
}
