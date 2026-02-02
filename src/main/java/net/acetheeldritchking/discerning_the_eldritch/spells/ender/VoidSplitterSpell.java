package net.acetheeldritchking.discerning_the_eldritch.spells.ender;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.capabilities.magic.PlayerRecasts;
import io.redspace.ironsspellbooks.capabilities.magic.RecastInstance;
import io.redspace.ironsspellbooks.capabilities.magic.RecastResult;
import net.acetheeldritchking.aces_spell_utils.network.AddShaderEffectPacket;
import net.acetheeldritchking.aces_spell_utils.network.RemoveShaderEffectPacket;
import net.acetheeldritchking.aces_spell_utils.spells.ASSpellAnimations;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.entity.spells.voidsplitter.VoidsplitterProjectile;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEPotionEffectRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.SpellRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

public class VoidSplitterSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "void_splitter");
    private boolean isFirstCast;

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(SchoolRegistry.ENDER_RESOURCE)
            .setMaxLevel(1)
            .setCooldownSeconds(60)
            .build();

    public VoidSplitterSpell()
    {
        this.manaCostPerLevel = 95;
        this.baseSpellPower = 5;
        this.spellPowerPerLevel = 2;
        this.castTime = 20;
        this.baseManaCost = 50;
    }

    @Override
    public ResourceLocation getSpellResource() {
        return spellId;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    @Override
    public CastType getCastType() {
        return CastType.LONG;
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        if (isFirstCast)
        {
            return ASSpellAnimations.ANIMATION_LEFT_HORIZONTAL_SWORD_SLASH;
        } else
        {
            return SpellAnimations.OVERHEAD_MELEE_SWING_ANIMATION;
        }
    }

    @Override
    public AnimationHolder getCastFinishAnimation() {
        return AnimationHolder.pass();
    }

    @Override
    public boolean canBeInterrupted(@Nullable Player player) {
        return false;
    }

    @Override
    public int getEffectiveCastTime(int spellLevel, @Nullable LivingEntity entity) {
        return getCastTime(spellLevel);
    }

    @Override
    public int getRecastCount(int spellLevel, @Nullable LivingEntity entity) {
        return 2;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (!playerMagicData.getPlayerRecasts().hasRecastForSpell(getSpellId()))
        {
            playerMagicData.getPlayerRecasts().addRecast
                    (new RecastInstance(getSpellId(), spellLevel, getRecastCount(spellLevel, entity), 8*20, castSource, null), playerMagicData);
        }

        if (entity instanceof ServerPlayer serverPlayer)
        {
            PacketDistributor.sendToPlayer(serverPlayer, new AddShaderEffectPacket(DiscerningTheEldritch.MOD_ID, "shaders/post/purple.json"));
            //entity.addEffect(new MobEffectInstance(DTEPotionEffectRegistry.PORTENT_EFFECT, 8*20, 1, false, false, false));
        }

        isFirstCast = true;

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    @Override
    public void onRecastFinished(ServerPlayer serverPlayer, RecastInstance recastInstance, RecastResult recastResult, ICastDataSerializable castDataSerializable) {
        super.onRecastFinished(serverPlayer, recastInstance, recastResult, castDataSerializable);

        /*PlayerRecasts recasts = MagicData.getPlayerMagicData(serverPlayer).getPlayerRecasts();

        if(recasts.hasRecastForSpell(SpellRegistries.VOID_SPLITTER.get().getSpellId())){
            recasts.removeRecast(SpellRegistries.VOID_SPLITTER.get().getSpellId());
            recasts.syncAllToPlayer();
        }*/

        if (recastResult.isSuccess())
        {
            PacketDistributor.sendToPlayer(serverPlayer, new RemoveShaderEffectPacket());

            isFirstCast = false;
            PacketDistributor.sendToPlayer(serverPlayer, new AddShaderEffectPacket(DiscerningTheEldritch.MOD_ID, "shaders/post/inverted_purple.json"));
            serverPlayer.addEffect(new MobEffectInstance(DTEPotionEffectRegistry.PORTENT_EFFECT, 3*20, 1, false, false, false));

            VoidsplitterProjectile voidSplitter = new VoidsplitterProjectile(serverPlayer.level(), serverPlayer);
            voidSplitter.setDelay(60);
            voidSplitter.setDamage(25);
            voidSplitter.getSpeed();
            voidSplitter.setNoGravity(true);
            voidSplitter.setDeltaMovement(0, 0, 0);
            voidSplitter.moveTo(serverPlayer.getX(), serverPlayer.getY() + 1, serverPlayer.getZ());

            serverPlayer.level().addFreshEntity(voidSplitter);
        }
        else
        {
            isFirstCast = true;
            PacketDistributor.sendToPlayer(serverPlayer, new RemoveShaderEffectPacket());
        }
    }
}
