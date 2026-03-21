package net.acetheeldritchking.discerning_the_eldritch.compat;

import net.acetheeldritchking.discerning_the_eldritch.compat.pastel.PastelCompatBlocks;
import net.acetheeldritchking.discerning_the_eldritch.compat.pastel.PastelCompatItems;
import net.neoforged.bus.api.IEventBus;

public class CompatRegistry {
    public static void registerPastelItems(IEventBus modBus)
    {
        if (CompatManager.isPastelLoaded())
        {
            PastelCompatItems.register(modBus);
            PastelCompatBlocks.register(modBus);
        }
    }
}
