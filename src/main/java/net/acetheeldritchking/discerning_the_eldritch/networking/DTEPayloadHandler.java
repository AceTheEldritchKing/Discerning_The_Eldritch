package net.acetheeldritchking.discerning_the_eldritch.networking;

import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.networking.devour.SyncDevourStacksPacket;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = DiscerningTheEldritch.MOD_ID)
public class DTEPayloadHandler {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar payloadRegistrar = event.registrar(DiscerningTheEldritch.MOD_ID).versioned("1.0.0").optional();


        payloadRegistrar.playToClient(SyncDevourStacksPacket.TYPE, SyncDevourStacksPacket.STREAM_CODEC, SyncDevourStacksPacket::handle);
    }
}
