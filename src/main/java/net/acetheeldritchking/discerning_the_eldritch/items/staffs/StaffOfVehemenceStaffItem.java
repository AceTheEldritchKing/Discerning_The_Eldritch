package net.acetheeldritchking.discerning_the_eldritch.items.staffs;

import io.redspace.ironsspellbooks.api.item.weapons.ExtendedSwordItem;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.acetheeldritchking.aces_spell_utils.items.staves.ImbuableStaffItem;
import net.acetheeldritchking.discerning_the_eldritch.items.custom.DTEItemDispatcher;
import net.acetheeldritchking.discerning_the_eldritch.registries.SpellRegistries;
import net.acetheeldritchking.discerning_the_eldritch.utils.DTERarities;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class StaffOfVehemenceStaffItem extends ImbuableStaffItem {
    public final DTEItemDispatcher dispatcher;

    public StaffOfVehemenceStaffItem() {
        super(
                ItemPropertiesHelper.equipment(1).fireResistant().rarity(DTERarities.APOTHIC_RARITY_PROXY.getValue()).attributes(ExtendedSwordItem.createAttributes(DTEStaffTier.STAFF_OF_VEHEMENCE)),
                SpellDataRegistryHolder.of(
                        new SpellDataRegistryHolder(SpellRegistries.ESOTERIC_EDGE, 6),
                        new SpellDataRegistryHolder(SpellRegistries.SILENCE, 6))
        );
        this.dispatcher = new DTEItemDispatcher();
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide && entity instanceof Player player )
        {
            dispatcher.idle(player, stack);
        }
    }
}
