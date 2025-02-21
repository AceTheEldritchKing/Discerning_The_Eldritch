package net.acetheeldritchking.discerning_the_eldritch.events;

import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.utils.boss_music.AscendedOneMusicManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = DiscerningTheEldritch.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void onPlayerLogout(ClientPlayerNetworkEvent.LoggingOut event)
    {
        AscendedOneMusicManager.hardStop();
    }
}
