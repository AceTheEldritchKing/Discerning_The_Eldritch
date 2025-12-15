package net.acetheeldritchking.discerning_the_eldritch.items.weapons;

import io.redspace.ironsspellbooks.api.item.weapons.ExtendedSwordItem;
import io.redspace.ironsspellbooks.api.item.weapons.MagicSwordItem;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.item.UniqueItem;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import it.crystalnest.prometheus.api.Fire;
import it.crystalnest.prometheus.api.FireManager;
import net.acetheeldritchking.aces_spell_utils.utils.ASRarities;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.networking.DTEAttachmentSync;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEAttachmentRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEDataComponentRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTESchoolRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.ItemRegistries;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import java.util.List;

public class SoulFireScytheItem extends MagicSwordItem implements UniqueItem {
    public static final int COOLDOWN = 15 * 20;

    public SoulFireScytheItem() {
        super(
                DTEWeaponTiers.SOUL_FIRE_SCYTHE,
                ItemPropertiesHelper.equipment(1).fireResistant().rarity(ASRarities.AQUATIC_RARITY_PROXY.getValue())
                        .component(DTEDataComponentRegistry.SOUL_FIRE_STACKS, 0)
                        .attributes(ExtendedSwordItem.createAttributes(DTEWeaponTiers.SOUL_FIRE_SCYTHE)),
                SpellDataRegistryHolder.of(
                        new SpellDataRegistryHolder(SpellRegistry.FLAMING_BARRAGE_SPELL, 6),
                        new SpellDataRegistryHolder(SpellRegistry.FLAMING_STRIKE_SPELL, 10)
                )
        );
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        if (Screen.hasShiftDown())
        {
            LivingEntity attacker = MinecraftInstanceHelper.getPlayer();

            tooltipComponents.add(
                    Component.translatable(
                            "tooltip.irons_spellbooks.passive_ability",
                            Component.literal(Utils.timeFromTicks(COOLDOWN, 1)).withStyle(ChatFormatting.LIGHT_PURPLE)
                    ).withStyle(ChatFormatting.DARK_PURPLE)
            );
            tooltipComponents.add(Component.literal(" ").append(Component.translatable(this.getDescriptionId() + ".desc")).withStyle(ChatFormatting.YELLOW));
            if (stack.get(DTEDataComponentRegistry.SOUL_FIRE_STACKS) != null)
            {
                assert attacker != null;
                tooltipComponents.add(Component.translatable(this.getDescriptionId() + ".desc2").append(soulFireStacks(attacker)).
                        withStyle(ChatFormatting.BLUE).
                        withStyle(ChatFormatting.ITALIC)
                );
            }
        } else
        {
            tooltipComponents.add(Component.translatable("item.discerning_the_eldritch.more_details").withStyle(ChatFormatting.GRAY));
        }
    }

    private static String soulFireStacks(LivingEntity entity)
    {
        ItemStack mainhandItem = entity.getMainHandItem();

        return String.format("%2d", mainhandItem.get(DTEDataComponentRegistry.SOUL_FIRE_STACKS));
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack)
    {
        return UseAnim.SPEAR;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity)
    {
        return 15 * 20;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged)
    {
        if (livingEntity instanceof Player player)
        {
            Integer soulFireStacks = stack.get(DTEDataComponentRegistry.SOUL_FIRE_STACKS);
            assert soulFireStacks != null;

            if (soulFireStacks >= 5)
            {
                if (getUseDuration(stack, player) <= 0)
                {
                    // Do AoE that soul burns everything
                    double radius = 10;

                    List<LivingEntity> entitiesNearby = level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(radius));

                    for (LivingEntity targets : entitiesNearby)
                    {
                        if (targets != player)
                        {
                            FireManager.affect(targets, FireManager.SOUL_FIRE_TYPE, Fire::getInFire);
                            MagicManager.spawnParticles(level, new BlastwaveParticleOptions(SchoolRegistry.FIRE.get().getTargetingColor(), (float) radius), player.getX(), player.getY() + 0.165F, player.getZ(), 1, 0, 0, 0, 0, true);
                        }
                    }
                }
            }

            DiscerningTheEldritch.LOGGER.debug("Stacks cleared?: " + soulFireStacks);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand)
    {
        ItemStack mainhandItem = player.getMainHandItem();

        Integer soulFireStacks = mainhandItem.get(DTEDataComponentRegistry.SOUL_FIRE_STACKS);
        assert soulFireStacks != null;

        if (soulFireStacks >= 5)
        {
            player.getCooldowns().addCooldown(ItemRegistries.SOUL_FIRE_SCYTHE.get(), SoulFireScytheItem.COOLDOWN);
            // Consume soul fire souls
            mainhandItem.set(DTEDataComponentRegistry.SOUL_FIRE_STACKS, soulFireStacks - 5);
            return InteractionResultHolder.consume(mainhandItem);
        } else {
            return InteractionResultHolder.fail(mainhandItem);
        }
    }
}
