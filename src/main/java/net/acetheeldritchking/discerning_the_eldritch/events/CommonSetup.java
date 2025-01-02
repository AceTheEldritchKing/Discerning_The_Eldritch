package net.acetheeldritchking.discerning_the_eldritch.events;

import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.ApothicAcolyteEntity;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.ApothicCrusaderEntity;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.ApothicSummonerEntity;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEEntityRegistry;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber(modid = DiscerningTheEldritch.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class CommonSetup {
    @SubscribeEvent
    public static void onAttributeCreateEvent(EntityAttributeCreationEvent event)
    {
        event.put(DTEEntityRegistry.APOTHIC_SUMMONER.get(), ApothicSummonerEntity.createAttributes().build());
        event.put(DTEEntityRegistry.APOTHIC_CRUSADER.get(), ApothicCrusaderEntity.createAttributes().build());
        event.put(DTEEntityRegistry.APOTHIC_ACOLYTE.get(), ApothicAcolyteEntity.createAttributes().build());
    }
}
