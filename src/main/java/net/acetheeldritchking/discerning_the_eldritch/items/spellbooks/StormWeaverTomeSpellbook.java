package net.acetheeldritchking.discerning_the_eldritch.items.spellbooks;

import io.redspace.ironsspellbooks.api.item.curios.AffinityData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import io.redspace.ironsspellbooks.util.TooltipsUtils;
import net.acetheeldritchking.aces_spell_utils.items.curios.PassiveAbilitySpellbook;
import net.acetheeldritchking.aces_spell_utils.utils.ASRarities;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class StormWeaverTomeSpellbook extends PassiveAbilitySpellbook {
    public static final int COOLDOWN = 5*20;

    public StormWeaverTomeSpellbook() {
        super(12, ItemPropertiesHelper.equipment().fireResistant().stacksTo(1).rarity(ASRarities.AQUATIC_RARITY_PROXY.getValue()));
        this.withSpellbookAttributes(
                new AttributeContainer(AttributeRegistry.LIGHTNING_SPELL_POWER, 0.10F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                //new AttributeContainer(DTEAttributeRegistry.RITUAL_MAGIC_POWER, .05F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                new AttributeContainer(AttributeRegistry.MAX_MANA, 200, AttributeModifier.Operation.ADD_VALUE)
        );
        /*super(SpellDataRegistryHolder.of(
                new SpellDataRegistryHolder(SpellRegistry.GUST_SPELL, 12),
                new SpellDataRegistryHolder(SpellRegistry.THUNDERSTORM_SPELL, 10)
        ), 8);
        withSpellbookAttributes(
                new AttributeContainer(AttributeRegistry.MAX_MANA, 200, AttributeModifier.Operation.ADD_VALUE),
                new AttributeContainer(AttributeRegistry.LIGHTNING_SPELL_POWER, 0.10F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
        );*/
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, TooltipContext context, @NotNull List<Component> lines, @NotNull TooltipFlag flag) {
        super.appendHoverText(itemStack, context, lines, flag);
        var affinityData = AffinityData.getAffinityData(itemStack);
        if (!affinityData.affinityData().isEmpty()) {
            int i = TooltipsUtils.indexOfComponent(lines, "tooltip.irons_spellbooks.spellbook_spell_count");
            lines.addAll(i < 0 ? lines.size() : i + 1, affinityData.getDescriptionComponent());
        }
    }

    @Override
    public void initializeSpellContainer(ItemStack itemStack) {
        if (itemStack == null) {
            return;
        }

        super.initializeSpellContainer(itemStack);
        AffinityData.set(itemStack, new AffinityData(Map.of(
                SpellRegistry.THUNDERSTORM_SPELL.get().getSpellResource(), 1,
                SpellRegistry.VOLT_STRIKE_SPELL.get().getSpellResource(), 1
        )));
    }

    protected int getCooldownTicks() {
        return COOLDOWN;
    }
}
