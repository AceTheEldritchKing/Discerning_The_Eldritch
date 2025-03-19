package net.acetheeldritchking.discerning_the_eldritch.registries;

import io.redspace.ironsspellbooks.item.armor.UpgradeOrbType;
import io.redspace.ironsspellbooks.registries.UpgradeOrbTypeRegistry;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.minecraft.resources.ResourceKey;

public class DTEUpgradeOrbTypeRegistry {

    public static ResourceKey<UpgradeOrbType> ELDRITCH_SPELL_POWER = ResourceKey.create(UpgradeOrbTypeRegistry.UPGRADE_ORB_REGISTRY_KEY, DiscerningTheEldritch.id("eldritch_power"));

    public static ResourceKey<UpgradeOrbType> RITUAL_SPELL_POWER = ResourceKey.create(UpgradeOrbTypeRegistry.UPGRADE_ORB_REGISTRY_KEY, DiscerningTheEldritch.id("ritual_power"));
}
