package net.acetheeldritchking.discerning_the_eldritch.events;

import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.entity.render.mobs.ApothicAcolyteRenderer;
import net.acetheeldritchking.discerning_the_eldritch.entity.render.mobs.ApothicCrusaderRenderer;
import net.acetheeldritchking.discerning_the_eldritch.entity.render.mobs.ApothicSummonerRenderer;
import net.acetheeldritchking.discerning_the_eldritch.entity.render.mobs.GaolerRenderer;
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
        event.registerEntityRenderer(DTEEntityRegistry.GAOLER_ENTITY.get(), GaolerRenderer::new);
    }
}
