package net.acetheeldritchking.discerning_the_eldritch.compat;

import net.neoforged.fml.ModList;

public class CompatManager {
    public static boolean isPastelLoaded()
    {
        return ModList.get().isLoaded("pastel");
    }
}
