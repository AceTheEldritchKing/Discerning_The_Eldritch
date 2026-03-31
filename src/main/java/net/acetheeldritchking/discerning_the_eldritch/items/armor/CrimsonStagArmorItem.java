package net.acetheeldritchking.discerning_the_eldritch.items.armor;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.acetheeldritchking.aces_spell_utils.registries.ASAttributeRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEAttributeRegistry;

public class CrimsonStagArmorItem extends ImbuableDTEArmorItem {
    public CrimsonStagArmorItem(Type slot, Properties settings) {
        super(DTEArmorMaterialRegistry.CRIMSON_STAG, slot, settings,
                schoolAttributesWithResistance(
                        AttributeRegistry.BLOOD_SPELL_POWER,
                        ASAttributeRegistry.RITUAL_MAGIC_POWER,
                        150,
                        0.15F,
                        0.05F,
                        0.05F
                ));
    }
}
