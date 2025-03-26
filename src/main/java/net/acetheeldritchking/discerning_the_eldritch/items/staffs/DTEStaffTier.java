package net.acetheeldritchking.discerning_the_eldritch.items.staffs;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import io.redspace.ironsspellbooks.item.weapons.IronsWeaponTier;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEAttributeRegistry;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class DTEStaffTier implements IronsWeaponTier {
    // Staff of Vehemence
    public static DTEStaffTier STAFF_OF_VEHEMENCE = new DTEStaffTier(3, -3,
            new AttributeContainer(AttributeRegistry.ELDRITCH_SPELL_POWER, 0.10f, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(DTEAttributeRegistry.RITUAL_MAGIC_POWER, 0.15f, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(AttributeRegistry.MANA_REGEN, 0.15f, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(AttributeRegistry.COOLDOWN_REDUCTION, 0.25f, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
    );

    float damage;
    float speed;
    AttributeContainer[] attributeContainers;

    public DTEStaffTier(float damage, float speed, AttributeContainer... attributeContainers)
    {
        this.damage = damage;
        this.speed = speed;
        this.attributeContainers = attributeContainers;
    }

    @Override
    public float getAttackDamageBonus() {
        return damage;
    }

    @Override
    public float getSpeed() {
        return speed;
    }

    @Override
    public AttributeContainer[] getAdditionalAttributes() {
        return this.attributeContainers;
    }
}
