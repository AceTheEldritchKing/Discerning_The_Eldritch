package net.acetheeldritchking.discerning_the_eldritch.items.curios;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.compat.Curios;
import io.redspace.ironsspellbooks.item.curios.PassiveAbilityCurio;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.acetheeldritchking.aces_spell_utils.registries.ASAttributeRegistry;
import net.acetheeldritchking.discerning_the_eldritch.utils.DTERarities;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

public class TheThornRingCurio extends PassiveAbilityCurio {
    public TheThornRingCurio() {
        super(ItemPropertiesHelper.equipment().stacksTo(1).fireResistant().rarity(DTERarities.APOTHIC_RARITY_PROXY.getValue()), Curios.RING_SLOT);
    }

    @Override
    protected int getCooldownTicks() {
        return 5 * 20;
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        Multimap<Holder<Attribute>, AttributeModifier> attr = LinkedHashMultimap.create();
        attr.put(AttributeRegistry.ELDRITCH_SPELL_POWER, new AttributeModifier(id, 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        attr.put(ASAttributeRegistry.MANA_REND, new AttributeModifier(id, 0.25, AttributeModifier.Operation.ADD_VALUE));

        return attr;
    }
}
