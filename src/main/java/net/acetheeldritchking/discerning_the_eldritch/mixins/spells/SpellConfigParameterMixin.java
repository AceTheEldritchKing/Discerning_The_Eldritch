package net.acetheeldritchking.discerning_the_eldritch.mixins.spells;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.config.SpellConfigParameter;
import net.acetheeldritchking.discerning_the_eldritch.spells.DTESpellRarities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SpellConfigParameter.class)
public class SpellConfigParameterMixin {
    @Shadow public static final SpellConfigParameter<DTESpellRarities> MIN_RARITY =
            new SpellConfigParameter<>(IronsSpellbooks.id("min_rarity"), DTESpellRarities.CODEC, DTESpellRarities.COMMON);
}
