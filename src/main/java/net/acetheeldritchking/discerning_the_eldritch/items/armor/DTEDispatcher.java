package net.acetheeldritchking.discerning_the_eldritch.items.armor;

import mod.azure.azurelib.rewrite.animation.dispatch.command.AzCommand;
import mod.azure.azurelib.rewrite.animation.play_behavior.AzPlayBehaviors;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public class DTEDispatcher {
    private static final AzCommand EQUIP_COMMAND = AzCommand.create(
            "base_controller",
            "equipping",
            AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand IDLE_COMMAND = AzCommand.create(
            "base_controller",
            "idle",
            AzPlayBehaviors.LOOP
    );

    private static final AzCommand FLIGHT_COMMAND = AzCommand.create(
            "base_controller",
            "idle",
            AzPlayBehaviors.LOOP
    );

    public void equip(Entity entity, ItemStack itemStack) {
        EQUIP_COMMAND.sendForItem(entity, itemStack);
    }

    public void idle(Entity entity, ItemStack itemStack) {
        IDLE_COMMAND.sendForItem(entity, itemStack);
    }

    public void flight(Entity entity, ItemStack itemStack) {
        FLIGHT_COMMAND.sendForItem(entity, itemStack);
    }
}
