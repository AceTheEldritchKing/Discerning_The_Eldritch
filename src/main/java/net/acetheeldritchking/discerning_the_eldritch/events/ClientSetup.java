package net.acetheeldritchking.discerning_the_eldritch.events;

import io.redspace.ironsspellbooks.item.weapons.StaffItem;
import io.redspace.ironsspellbooks.render.ClientStaffItemExtensions;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.gaoler.GaolerModel;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.sightless_maw.SightlessMawModel;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.eldritch_caster.TheApostleModel;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.untold_behemoth.UntoldBehemothModel;
import net.acetheeldritchking.discerning_the_eldritch.entity.render.mobs.*;
import net.acetheeldritchking.discerning_the_eldritch.entity.spells.esoteric_edge.EsotericEdgeRenderer;
import net.acetheeldritchking.discerning_the_eldritch.entity.spells.glacial_edge.GlacialEdgeRenderer;
import net.acetheeldritchking.discerning_the_eldritch.particle.EsotericSparksParticle;
import net.acetheeldritchking.discerning_the_eldritch.particle.GlacialShadowParticle;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEEntityRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEParticleRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.ItemRegistries;
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
    }

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event)
    {
        event.registerItem(new ClientStaffItemExtensions(), ItemRegistries.getDTEItems().stream().filter(item -> item.get() instanceof StaffItem staffItem && !staffItem.hasCustomRendering()).map(holder -> (Item) holder.get()).toArray(Item[]::new));
    }

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event)
    {
        event.registerSpriteSet(DTEParticleRegistry.GLACIAL_SHADOW_PARTICLE.get(), GlacialShadowParticle.Provider::new);
        event.registerSpriteSet(DTEParticleRegistry.ESOTERIC_SPARKS_PARTICLE.get(), EsotericSparksParticle.Provider::new);
    }
}
