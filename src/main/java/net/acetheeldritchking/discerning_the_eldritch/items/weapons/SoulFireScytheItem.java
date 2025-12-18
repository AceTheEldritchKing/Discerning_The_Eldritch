package net.acetheeldritchking.discerning_the_eldritch.items.weapons;

import io.redspace.ironsspellbooks.api.item.weapons.ExtendedSwordItem;
import io.redspace.ironsspellbooks.api.item.weapons.MagicSwordItem;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.item.UniqueItem;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import it.crystalnest.prometheus.api.FireManager;
import net.acetheeldritchking.aces_spell_utils.utils.ASRarities;
import net.acetheeldritchking.aces_spell_utils.utils.ASUtils;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEDataComponentRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTESoundRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.ItemRegistries;
import net.acetheeldritchking.discerning_the_eldritch.registries.SpellRegistries;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
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
                        new SpellDataRegistryHolder(SpellRegistries.SOUL_SLICE, 1),
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
                            "tooltip.discerning_the_eldritch.active_ability",
                            Component.literal(Utils.timeFromTicks(COOLDOWN, 1)).withStyle(ChatFormatting.LIGHT_PURPLE)
                    ).withStyle(ChatFormatting.DARK_PURPLE)
            );
            tooltipComponents.add(Component.literal(" ").append(Component.translatable(this.getDescriptionId() + ".desc")).withStyle(ChatFormatting.YELLOW));
            tooltipComponents.add(Component.translatable(this.getDescriptionId() + ".desc2").
                    withStyle(ChatFormatting.GOLD).
                    withStyle(ChatFormatting.ITALIC)
            );
            if (stack.get(DTEDataComponentRegistry.SOUL_FIRE_STACKS) != null && attacker != null)
            {
                tooltipComponents.add(Component.translatable(this.getDescriptionId() + ".desc3").append(soulFireStacks(attacker)).
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
        return 5 * 20;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged)
    {
        if (livingEntity instanceof Player player)
        {
            Integer soulFireStacks = stack.get(DTEDataComponentRegistry.SOUL_FIRE_STACKS);
            assert soulFireStacks != null;

            if (soulFireStacks >= 5 && timeCharged >= getUseDuration(stack, livingEntity))
            {
                livingEntity.level().playLocalSound(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), DTESoundRegistry.DEVOURER_WRETCH.get(), SoundSource.PLAYERS, 1, 1, false);

                // Do AoE that soul burns everything
                double radius = 10;

                List<LivingEntity> entitiesNearby = level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(radius));

                for (LivingEntity targets : entitiesNearby)
                {
                    if (targets != player)
                    {
                        FireManager.setOnFire(targets, 10, FireManager.SOUL_FIRE_TYPE);
                    }
                }

                level.addParticle(new BlastwaveParticleOptions(ASUtils.rbgToVector3F(39, 166, 245), (float) radius), player.getX(), player.getY() + 0.165F, player.getZ(), 0, 0, 0);

                DiscerningTheEldritch.LOGGER.debug("Stacks cleared?: " + soulFireStacks);
                stack.set(DTEDataComponentRegistry.SOUL_FIRE_STACKS, soulFireStacks - 5);
                player.getCooldowns().addCooldown(ItemRegistries.SOUL_FIRE_SCYTHE.get(), SoulFireScytheItem.COOLDOWN);
            }
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
            // Consume soul fire souls
            player.startUsingItem(usedHand);
            return InteractionResultHolder.consume(mainhandItem);
        } else {
            return InteractionResultHolder.fail(mainhandItem);
        }
    }
}
