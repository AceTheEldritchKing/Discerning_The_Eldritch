package net.acetheeldritchking.discerning_the_eldritch.spells.eldritch;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.events.SpellSummonEvent;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.*;
import net.acetheeldritchking.aces_spell_utils.AcesSpellUtils;
import net.acetheeldritchking.aces_spell_utils.network.AddShaderEffectPacket;
import net.acetheeldritchking.aces_spell_utils.network.RemoveShaderEffectPacket;
import net.acetheeldritchking.aces_spell_utils.spells.ASSpellAnimations;
import net.acetheeldritchking.aces_spell_utils.utils.ASUtils;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.gaoler.GaolerEntity;
import net.acetheeldritchking.discerning_the_eldritch.registries.ItemRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class ConjureGaolerSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "conjure_gaoler");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.hp", Utils.stringTruncation(getGaolerHealth(spellLevel, null), 2)),
                Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation(getGaolerDamage(spellLevel, null), 2))
        );
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(SchoolRegistry.ELDRITCH_RESOURCE)
            .setMaxLevel(1)
            .setCooldownSeconds(600)
            .build();

    public ConjureGaolerSpell()
    {
        this.manaCostPerLevel = 100;
        this.baseSpellPower = 25;
        this.spellPowerPerLevel = 1;
        this.castTime = 100;
        this.baseManaCost = 500;
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
    public boolean canBeCraftedBy(Player player) {
        return false;
    }

    @Override
    public boolean allowCrafting() {
        return false;
    }

    @Override
    public boolean allowLooting() {
        return false;
    }

    @Override
    public boolean requiresLearning() {
        return false;
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return ASSpellAnimations.ANIMATION_GAOLER_SUMMON;
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundEvents.WARDEN_HEARTBEAT);
    }

    @Override
    public boolean canBeInterrupted(@Nullable Player player) {
        return false;
    }

    @Override
    public int getRecastCount(int spellLevel, @Nullable LivingEntity entity) {
        if (entity instanceof Player player)
        {
            if (ASUtils.hasCurio(player, ItemRegistries.KINGS_EFFIGY.get()))
            {
                return 2;
            }
        }

        return 1;
    }

    @Override
    public void onRecastFinished(ServerPlayer serverPlayer, RecastInstance recastInstance, RecastResult recastResult, ICastDataSerializable castDataSerializable) {
        if (SummonManager.recastFinishedHelper(serverPlayer, recastInstance, recastResult, castDataSerializable) && ASUtils.hasCurio(serverPlayer, ItemRegistries.KINGS_EFFIGY.get())) {
            super.onRecastFinished(serverPlayer, recastInstance, recastResult, castDataSerializable);
        }
    }

    @Override
    public ICastDataSerializable getEmptyCastData() {
        return new SummonedEntitiesCastData();
    }

    @Override
    public void onServerPreCast(Level level, int spellLevel, LivingEntity entity, @Nullable MagicData playerMagicData) {
        double radius = 15;

        List<LivingEntity> entitiesNearby = level.getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(radius));
        for (LivingEntity targets : entitiesNearby)
        {
            targets.addEffect(new MobEffectInstance(MobEffects.DARKNESS, getCastTime(spellLevel), 1, false, false, false));
        }
        entity.addEffect(new MobEffectInstance(MobEffects.DARKNESS, getCastTime(spellLevel), 1, false, false, false));

        if (entity instanceof ServerPlayer serverPlayer)
        {
            PacketDistributor.sendToPlayer(serverPlayer, new AddShaderEffectPacket(DiscerningTheEldritch.MOD_ID, "shaders/grayscale_darker.json"));
        }

        super.onServerPreCast(level, spellLevel, entity, playerMagicData);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        // This is the strongest summon possible, hard limit to one minute
        // This is also not following the standard recast stuff for summons, this is fucking Mahoraga you are letting him LOOSE
        // Unless you are wearing King's Visage, then he acts like the typical summon
        PlayerRecasts recasts = playerMagicData.getPlayerRecasts();
        int summonTimer = (20 * 60);
        SummonedEntitiesCastData summonedEntitiesCastData = new SummonedEntitiesCastData();

        BlockPos pos = new BlockPos((int) entity.getX(), (int) entity.getY(), (int) entity.getZ());

        Vec3 lookDir = entity.getLookAngle();
        Vec3 flatDir = new Vec3(lookDir.x, 0, lookDir.z).normalize();
        double offset = 6.0;
        Vec3 spawnPos = entity.position().subtract(flatDir.scale(offset));

        if (entity instanceof Player player && ASUtils.hasCurio(player, ItemRegistries.KINGS_EFFIGY.get()))
        {
            if (!recasts.hasRecastForSpell(this))
            {
                spawnGaoler(spawnPos.x, spawnPos.y, spawnPos.z, entity, level, summonTimer, spellLevel, summonedEntitiesCastData, false);
                RecastInstance recastInstance = new RecastInstance(this.getSpellId(), spellLevel, getRecastCount(spellLevel, entity), summonTimer, castSource, summonedEntitiesCastData);
                recasts.addRecast(recastInstance, playerMagicData);
            }
        }
        else
        {
            spawnGaoler(spawnPos.x, spawnPos.y, spawnPos.z, entity, level, summonTimer, spellLevel, summonedEntitiesCastData, true);
        }
        if (entity instanceof ServerPlayer serverPlayer)
        {
            PacketDistributor.sendToPlayer(serverPlayer, new RemoveShaderEffectPacket());
        }

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private void spawnGaoler(double x, double y, double z, LivingEntity caster, Level level, int timer, int spellLevel, SummonedEntitiesCastData castData, boolean isFeral)
    {
        GaolerEntity gaoler = new GaolerEntity(level, caster, true);

        gaoler.setPos(x, y, z);
        gaoler.setOldPosAndRot();
        gaoler.getLookAngle().reverse();
        gaoler.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(getGaolerDamage(spellLevel, caster));
        gaoler.getAttributes().getInstance(Attributes.MAX_HEALTH).setBaseValue(getGaolerHealth(spellLevel, caster));
        gaoler.setHealth(gaoler.getMaxHealth());

        var event = NeoForge.EVENT_BUS.post(new SpellSummonEvent<>(caster, gaoler, this.spellId, spellLevel)).getCreature();

        level.addFreshEntity(event);

        SummonManager.initSummon(caster, event, timer, castData);

        if (isFeral)
        {
            SummonManager.removeSummon(gaoler);
        }

        /*
        System.out.println("////");
        System.out.println("/");
        System.out.println("HP: " + gaoler.getMaxHealth());
        System.out.println("Damage: " + gaoler.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).getBaseValue());
        System.out.println("/");
        System.out.println("////");
        */
    }

    private float getGaolerDamage(int spellLevel, LivingEntity caster)
    {
        return getSpellPower(spellLevel, caster) * 4.5f;
    }

    private float getGaolerHealth(int spellLevel, LivingEntity caster)
    {
        return getSpellPower(spellLevel, caster) * 20.5f;
    }
}
