package net.acetheeldritchking.discerning_the_eldritch.items.curios;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.compat.Curios;
import io.redspace.ironsspellbooks.item.curios.PassiveAbilityCurio;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.acetheeldritchking.aces_spell_utils.registries.ASAttributeRegistry;
import net.acetheeldritchking.discerning_the_eldritch.items.custom.DTEItemDispatcher;
import net.acetheeldritchking.discerning_the_eldritch.utils.DTERarities;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

public class LanternOfHatredCurio extends PassiveAbilityCurio {
    public final DTEItemDispatcher dispatcher;

    public LanternOfHatredCurio() {
        super(ItemPropertiesHelper.equipment().stacksTo(1).fireResistant().rarity(DTERarities.APOTHIC_RARITY_PROXY.getValue()), Curios.NECKLACE_SLOT);
        this.dispatcher = new DTEItemDispatcher();
    }

    @Override
    protected int getCooldownTicks() {
        return 35 * 20;
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        Multimap<Holder<Attribute>, AttributeModifier> attr = LinkedHashMultimap.create();
        attr.put(AttributeRegistry.ELDRITCH_SPELL_POWER, new AttributeModifier(id, 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        attr.put(ASAttributeRegistry.MAGIC_DAMAGE_CRIT_CHANCE, new AttributeModifier(id, 0.15, AttributeModifier.Operation.ADD_VALUE));
        attr.put(ASAttributeRegistry.MAGIC_DAMAGE_CRIT_DAMAGE, new AttributeModifier(id, 2.5, AttributeModifier.Operation.ADD_VALUE));
        attr.put(ASAttributeRegistry.MANA_REND, new AttributeModifier(id, 0.15, AttributeModifier.Operation.ADD_VALUE));

        return attr;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide && entity instanceof Player player )
        {
            dispatcher.idle(player, stack);
        }
    }
}
