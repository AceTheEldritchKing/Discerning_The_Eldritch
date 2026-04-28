package net.acetheeldritchking.discerning_the_eldritch.items.custom;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.registries.PoiTypeRegistry;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.acetheeldritchking.aces_spell_utils.utils.ASRarities;
import net.acetheeldritchking.aces_spell_utils.utils.ASUtils;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.bosses.apostle_of_sculk.ApostleOfSculkBoss;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEEntityRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEPoiRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTESoundRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.ItemRegistries;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Objects;

public class FadingSculkLantern extends Item {
    public FadingSculkLantern() {
        super(ItemPropertiesHelper.material().rarity(ASRarities.SCULK_RARITY_PROXY.getValue()).stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("item.discerning_the_eldritch.fading_sculk_lantern.description").
                withStyle(ChatFormatting.GRAY).
                withStyle(ChatFormatting.ITALIC));
    }

    // We're not doing much in here for now, this is a teaser for next update...
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);

        if (level instanceof ServerLevel serverLevel && player instanceof ServerPlayer serverPlayer)
        {
            player.getCooldowns().addCooldown(ItemRegistries.FADING_SCULK_LANTERN.get(), 10 * 20);

            BlockPos pos = player.blockPosition();
            PoiManager poiManager = serverLevel.getPoiManager();
            var resonatingDeepslate = poiManager.findClosest(poi -> Objects.equals(poi.getKey(), DTEPoiRegistry.RESONATING_DEEPSLATE_POI.getKey()), pos, 26, PoiManager.Occupancy.ANY);

            if (resonatingDeepslate.isPresent() && pos.getY() + 2 >= resonatingDeepslate.get().getY())
            {
                BlockPos deepslatePos = resonatingDeepslate.get();
                AABB exclusiveRange = AABB.ofSize(deepslatePos.getCenter(), 80, 80, 80);

                if (level.getEntitiesOfClass(ApostleOfSculkBoss.class, exclusiveRange).isEmpty())
                {
                    if (!player.getAbilities().instabuild)
                    {
                        Vec3 particlePos = player.getEyePosition().add(player.getForward().scale(0.6)).subtract(0, 0.3, 0);
                        MagicManager.spawnParticles(serverLevel, new ItemParticleOption(ParticleTypes.ITEM, itemStack), particlePos.x, particlePos.y, particlePos.z, 9, .15, .15, .15, 0.08, false);
                        itemStack.shrink(1);
                        player.setItemInHand(usedHand, itemStack);
                    }

                    serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(Component.translatable("display.discerning_the_eldritch.fading_sculk_lantern_success")
                            .withStyle(s -> s.withColor(TextColor.fromRgb(0x009295)))));

                    Vec3 center = deepslatePos.getCenter().add(0, 0.6, 0);
                    float yRot = Utils.getAngle(center.x, center.z, player.getX(), player.getZ()) * Mth.RAD_TO_DEG;
                    ApostleOfSculkBoss apostleOfSculkBoss = DTEEntityRegistry.APOSTLE_OF_SCULK.get().create(serverLevel);
                    apostleOfSculkBoss.moveTo(center);
                    apostleOfSculkBoss.setYBodyRot(yRot);
                    apostleOfSculkBoss.setXRot(player.getXRot());
                    apostleOfSculkBoss.triggerSpawnAnim();
                    apostleOfSculkBoss.finalizeSpawn(serverLevel, level.getCurrentDifficultyAt(player.blockPosition()), MobSpawnType.MOB_SUMMONED, null);
                    level.addFreshEntity(apostleOfSculkBoss);
                    onUseEffects(serverPlayer, serverLevel, player.position(), true);

                    var advancement = serverPlayer.serverLevel().getServer().getAdvancements().get(DiscerningTheEldritch.id("discerning_the_eldritch/main/apostle_of_sculk_fight"));
                    if (advancement != null) {
                        serverPlayer.getAdvancements().award(advancement, "apostle_of_sculk_fight");
                    }
                } else
                {
                    serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(Component.translatable("display.discerning_the_eldritch.fading_sculk_lantern_fail_2")
                            .withStyle(s -> s.withColor(TextColor.fromRgb(0x009295)))));
                    onUseEffects(serverPlayer, serverLevel, player.position(), false);
                }
            } else
            {
                serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(Component.translatable("display.discerning_the_eldritch.fading_sculk_lantern_fail_1")
                        .withStyle(s -> s.withColor(TextColor.fromRgb(0x009295)))));
                onUseEffects(serverPlayer, serverLevel, player.position(), false);
            }
        }
        player.swing(usedHand);
        return InteractionResultHolder.pass(itemStack);
    }

    public void onUseEffects(Player player, ServerLevel serverLevel, Vec3 usePosition, boolean success)
    {
        serverLevel.playSound(null, usePosition.x, usePosition.y, usePosition.z, success ? DTESoundRegistry.SCULK_LANTERN_TOLL_SUCCESS : DTESoundRegistry.SCULK_LANTERN_TOLL, SoundSource.PLAYERS, 6, 1f);
        ASUtils.spawnParticlesInRing(16, 0.25F, 1.25F, 2.25F, 0.5F, 0.1F, player, ParticleTypes.SCULK_SOUL);
    }
}
