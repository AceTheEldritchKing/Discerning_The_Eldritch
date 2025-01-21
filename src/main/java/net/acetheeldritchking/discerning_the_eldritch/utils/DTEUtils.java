package net.acetheeldritchking.discerning_the_eldritch.utils;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import top.theillusivec4.curios.api.CuriosApi;

public class DTEUtils {
    // Gets equipped curio on the player
    public static boolean hasCurio(Player player, Item item)
    {
        return CuriosApi.getCuriosHelper().findEquippedCurio(item, player).isPresent();
    }
}
