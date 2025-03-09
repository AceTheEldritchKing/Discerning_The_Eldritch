package net.acetheeldritchking.discerning_the_eldritch.registries;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.item.armor.UpgradeOrbType;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class DTEUpgradeOrbTypeRegistry {
    public static final ResourceKey<Registry<UpgradeOrbType>> DTE_UPGRADE_ORB_REGISTRY_KEY = ResourceKey.createRegistryKey(IronsSpellbooks.id("upgrade_orb_type"));

    //

    public static ResourceKey<UpgradeOrbType> ELDRITCH_SPELL_POWER = ResourceKey.create(DTE_UPGRADE_ORB_REGISTRY_KEY, DiscerningTheEldritch.id("eldritch_power"));
}
