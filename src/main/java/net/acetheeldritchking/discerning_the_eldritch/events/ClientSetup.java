package net.acetheeldritchking.discerning_the_eldritch.events;

import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.GaolerModel;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.SightlessMawModel;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.TheApostleModel;
import net.acetheeldritchking.discerning_the_eldritch.entity.render.mobs.*;
import net.acetheeldritchking.discerning_the_eldritch.entity.spells.esoteric_edge.EsotericEdgeRenderer;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEEntityRegistry;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

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
    }
}
