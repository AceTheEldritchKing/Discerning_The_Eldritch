package net.acetheeldritchking.discerning_the_eldritch.spells;

import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.minecraft.resources.ResourceLocation;

public class DTESpellAnimations {
    public static ResourceLocation ANIMATION_RESOURCE = ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "animation");

    public static final AnimationHolder ANIMATION_CLAP = new AnimationHolder(DiscerningTheEldritch.MOD_ID + ":clap", true);
    public static final AnimationHolder ANIMATION_GAOLER_SUMMON = new AnimationHolder(DiscerningTheEldritch.MOD_ID + ":gaoler_summon", true);
}
