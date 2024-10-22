package net.acetheeldritchking.discerning_the_eldritch.items.weapons;

import io.redspace.ironsspellbooks.api.item.weapons.MagicSwordItem;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.render.SpecialItemRenderer;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class DeepGreatsword extends MagicSwordItem {
    public DeepGreatsword(SpellDataRegistryHolder[] imbuedSpells)
    {
        super(
                DTEWeaponTiers.DEEP_GREATSWORD,
                7,
                -2.0F,
                imbuedSpells,
                Map.of(
                        AttributeRegistry.ELDRITCH_SPELL_POWER.get(),
                        new AttributeModifier(UUID.fromString("3eab8639-17d8-467f-a5b3-1b8b22705484"), "Weapon Modifier", 0.05, AttributeModifier.Operation.MULTIPLY_BASE)
                ),
                ItemPropertiesHelper.equipment().fireResistant().rarity(Rarity.RARE));
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new SpecialItemRenderer(Minecraft.getInstance().getItemRenderer(),
                        Minecraft.getInstance().getEntityModels(),
                        "deep_greatsword");
            }
        });
    }
}
