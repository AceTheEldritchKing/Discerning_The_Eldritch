package net.acetheeldritchking.discerning_the_eldritch.spells.ritual;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastResult;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractRitualSpell extends AbstractSpell {
    @Override
    public boolean allowLooting() {
        return false;
    }

    @Override
    public boolean canBeCraftedBy(Player player) {
        return false;
    }

    @Override
    public boolean allowCrafting() {
        return false;
    }

    public boolean isComplex()
    {
        return true;
    }

    @Override
    public CastResult canBeCastedBy(int spellLevel, CastSource castSource, MagicData playerMagicData, Player player) {
        if (castSource == CastSource.SPELLBOOK && isComplex())
        {
            return new CastResult(CastResult.Type.FAILURE, Component.translatable("ui.discerning_the_eldritch.ritual_cast_failure", new Object[]{this.getDisplayName(player)}).withStyle(ChatFormatting.RED));
        }
        return super.canBeCastedBy(spellLevel, castSource, playerMagicData, player);
    }

    @Override
    public boolean canBeInterrupted(@Nullable Player player) {
        return false;
    }
}
