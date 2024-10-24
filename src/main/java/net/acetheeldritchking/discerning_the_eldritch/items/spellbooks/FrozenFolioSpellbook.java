package net.acetheeldritchking.discerning_the_eldritch.items.spellbooks;

import io.redspace.ironsspellbooks.item.SpellBook;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class FrozenFolioSpellbook extends SpellBook {
    @Override
    public boolean canWalkOnPowderedSnow(ItemStack stack, LivingEntity wearer) {
        return true;
    }
}
