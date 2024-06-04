package net.acetheeldritchking.discerning_the_eldritch.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SilencePotionEffect extends MobEffect {
    public SilencePotionEffect() {
        super(MobEffectCategory.HARMFUL, 3755355);
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        return ret;
    }
}