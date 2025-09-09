package net.acetheeldritchking.discerning_the_eldritch.spells.eldritch;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.spells.ender.TeleportSpell;
import io.redspace.ironsspellbooks.util.Log;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.entity.spells.rift_walker.UnstableRiftEntity;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@AutoSpellConfig
public class RiftWalkerSpell extends AbstractSpell {
    public static final float RADIUS = 3;
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "rift_walker");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.distance", Utils.stringTruncation(getDistance(spellLevel, caster), 1)),
                Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation(getDamage(spellLevel, caster), 1))
        );
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(SchoolRegistry.ELDRITCH_RESOURCE)
            .setMaxLevel(5)
            .setCooldownSeconds(15)
            .build();

    public RiftWalkerSpell()
    {
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 12;
        this.spellPowerPerLevel = 5;
        this.castTime = 0;
        this.baseManaCost = 35;
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
        return CastType.INSTANT;
    }

    @Override
    public void onServerPreCast(Level level, int spellLevel, LivingEntity entity, @Nullable MagicData playerMagicData) {
        super.onServerPreCast(level, spellLevel, entity, playerMagicData);

        UnstableRiftEntity unstableRift = new UnstableRiftEntity(level, entity, getDamage(spellLevel, entity), RADIUS);
        unstableRift.setPos(entity.getBoundingBox().getCenter().subtract(0, unstableRift.getBbHeight() * .5f, 0));
        level.addFreshEntity(unstableRift);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        Vec3 dest = null;
        var teleportData = (TeleportSpell.TeleportData) playerMagicData.getAdditionalCastData();

        if (teleportData != null)
        {
            var potentialTarget = teleportData.getTeleportTargetPosition();
            if (potentialTarget != null)
            {
                dest = potentialTarget;
                Utils.handleSpellTeleport(this, entity, dest);
            }
        }
        else
        {
            HitResult hitResult = Utils.raycastForEntity(level, entity, getDistance(spellLevel, entity), true);

            if (entity.isPassenger())
            {
                entity.stopRiding();
            }
            if (hitResult.getType() == HitResult.Type.ENTITY && ((EntityHitResult) hitResult).getEntity() instanceof LivingEntity target)
            {
                for (int i = 0; i < 8; i++)
                {
                    dest = target.position().subtract(new Vec3(0, 0, 1.5).yRot(-(target.getYRot() + i * 45) * Mth.DEG_TO_RAD));
                    if (level.getBlockState(BlockPos.containing(dest).above()).isAir())
                    {
                        break;
                    }

                }
                Utils.handleSpellTeleport(this, entity, dest.add(0, 1, 0));
                entity.lookAt(EntityAnchorArgument.Anchor.EYES, target.getEyePosition().subtract(0, .15, 0));
            } else
            {
                dest = TeleportSpell.findTeleportLocation(level, entity, getDistance(spellLevel, entity));
                Utils.handleSpellTeleport(this, entity, dest);
            }
        }

        entity.resetFallDistance();

        // Invis & Spawn Rift
        entity.setInvisible(true);
        entity.addEffect(new MobEffectInstance(MobEffectRegistry.TRUE_INVISIBILITY, 100, 0, false, false, true));

        UnstableRiftEntity unstableRift = new UnstableRiftEntity(level, entity, getDamage(spellLevel, entity), RADIUS);
        unstableRift.setTracking(entity);
        unstableRift.setPos(entity.getBoundingBox().getCenter().subtract(0, unstableRift.getBbHeight() * .5f, 0));
        level.addFreshEntity(unstableRift);

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private float getDamage(int spellLevel, LivingEntity caster)
    {
        return getSpellPower(spellLevel, caster) * 0.5F;
    }

    private float getDistance(int spellLevel, LivingEntity entity)
    {
        return (float) (Utils.softCapFormula(getEntityPowerMultiplier(entity)) * (getSpellPower(spellLevel, null) / 2));
    }

    // I am REALLY tired, so I copied this from TP spell
    public static class TeleportData implements ICastData {
        private Vec3 teleportTargetPosition;

        public TeleportData(Vec3 teleportTargetPosition) {
            this.teleportTargetPosition = teleportTargetPosition;
        }

        public void setTeleportTargetPosition(Vec3 targetPosition) {
            this.teleportTargetPosition = targetPosition;
        }

        public Vec3 getTeleportTargetPosition() {
            return this.teleportTargetPosition;
        }

        @Override
        public void reset() {
            //Nothing needed here for teleport
        }
    }
}
