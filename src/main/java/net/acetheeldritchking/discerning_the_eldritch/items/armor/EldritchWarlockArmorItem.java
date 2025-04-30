package net.acetheeldritchking.discerning_the_eldritch.items.armor;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.acetheeldritchking.discerning_the_eldritch.items.armor.old.ImbuableOldDTEArmorItem;

public class EldritchWarlockArmorItem extends ImbuableOldDTEArmorItem {
    public EldritchWarlockArmorItem(Type slot, Properties settings) {
        super(DTEArmorMaterialRegistry.ELDRITCH_WARLOCK, slot, settings, schoolAttributesWithResistance(AttributeRegistry.ELDRITCH_SPELL_POWER, AttributeRegistry.MANA_REGEN, 150, 0.15F, 0.05F, 0.05F));
    }
}
