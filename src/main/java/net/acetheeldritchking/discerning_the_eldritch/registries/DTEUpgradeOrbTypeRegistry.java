package net.acetheeldritchking.discerning_the_eldritch.registries;

import com.mojang.serialization.Codec;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.item.armor.UpgradeOrbType;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

public class DTEUpgradeOrbTypeRegistry {
    public static final ResourceKey<Registry<UpgradeOrbType>> DTE_UPGRADE_ORB_REGISTRY_KEY = ResourceKey.createRegistryKey(DiscerningTheEldritch.id("upgrade_orb_type"));

    public static void setDteDatapackUpgradeOrbRegistryKey(DataPackRegistryEvent.NewRegistry event)
    {
        event.dataPackRegistry(DTE_UPGRADE_ORB_REGISTRY_KEY, UpgradeOrbType.CODEC, UpgradeOrbType.CODEC);
    }

    public static ResourceKey<UpgradeOrbType> ELDRITCH_SPELL_POWER = ResourceKey.create(DTE_UPGRADE_ORB_REGISTRY_KEY, DiscerningTheEldritch.id("eldritch_power"));
}
