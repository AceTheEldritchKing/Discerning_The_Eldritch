package net.acetheeldritchking.discerning_the_eldritch.items.armor;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.acetheeldritchking.discerning_the_eldritch.items.armor.old.ImbuableOldDTEArmorItem;

public class EldritchWarlockMaskItem extends ImbuableOldDTEArmorItem {
    public EldritchWarlockMaskItem(Type slot, Properties settings) {
        super(DTEArmorMaterialRegistry.ELDRITCH_WARLOCK, slot, settings, schoolAttributesWithResistance(AttributeRegistry.ELDRITCH_SPELL_POWER, AttributeRegistry.SUMMON_DAMAGE, 150, 0.15F, 0.05F, 0.05F));
    }
}
