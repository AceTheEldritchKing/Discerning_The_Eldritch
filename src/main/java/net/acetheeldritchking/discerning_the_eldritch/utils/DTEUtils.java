package net.acetheeldritchking.discerning_the_eldritch.utils;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import top.theillusivec4.curios.api.CuriosApi;

public class DTEUtils {
    // Gets equipped curio on the player
    public static boolean hasCurio(Player player, Item item)
    {
        return CuriosApi.getCuriosHelper().findEquippedCurio(item, player).isPresent();
    }

    public static boolean isLongAnimCast(AbstractSpell spell)
    {
        if (spell.getCastType() == CastType.LONG)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static boolean isContAnimCast(AbstractSpell spell)
    {
        if (spell.getCastType() == CastType.CONTINUOUS)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
