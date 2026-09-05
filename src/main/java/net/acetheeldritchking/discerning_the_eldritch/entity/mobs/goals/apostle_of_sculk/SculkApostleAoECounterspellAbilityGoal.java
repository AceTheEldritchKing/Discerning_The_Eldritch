package net.acetheeldritchking.discerning_the_eldritch.entity.mobs.goals.apostle_of_sculk;

import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.events.CounterSpellEvent;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.RecastResult;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss.goals.AnimatedActionGoal;
import net.acetheeldritchking.aces_spell_utils.utils.ASUtils;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.bosses.apostle_of_sculk.ApostleOfSculkBoss;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTESoundRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.neoforged.neoforge.common.NeoForge;

import java.util.List;

public class SculkApostleAoECounterspellAbilityGoal extends AnimatedActionGoal<ApostleOfSculkBoss> {
    public SculkApostleAoECounterspellAbilityGoal(ApostleOfSculkBoss mob) {
        super(mob);
    }

    @Override
    protected boolean canStartAction() {
        // Make sure they're within the range for the AoE, but not too beyond the range
        return mob.onGround() && mob.getTarget() != null && mob.distanceToSqr(mob.getTarget()) > 8 * 8;
    }

    @Override
    protected int getActionTimestamp() {
        return 24;
    }

    @Override
    protected int getActionDuration() {
        return 25;
    }

    @Override
    protected int getCooldown() {
        return Utils.random.nextIntBetweenInclusive(130, 170);
    }

    @Override
    protected String getAnimationId() {
        return "overhead_swing_full";
    }

    @Override
    public void tick() {
        if (mob.getTarget() != null) {
            mob.setTarget(mob.getTarget());
        }

        if (abilityTimer <= 5)
        {
            mob.playSound(DTESoundRegistry.DARK_MAGIC_CHARGE_1.get(), 2.5f, Utils.random.nextIntBetweenInclusive(80, 110) * .01f);
            ASUtils.spawnParticlesInCircle(16, 10.5F, 0.8F, 0.1F, mob, ParticleTypes.SCULK_SOUL);
        }
        super.tick();
    }

    @Override
    protected void doAction() {
        DiscerningTheEldritch.LOGGER.debug("GO INTO AOE CTS GOAL!!!");

        float radius = 10.5F;
        List<LivingEntity> targets = mob.level().getEntitiesOfClass(LivingEntity.class, mob.getBoundingBox().inflate(radius));

        ASUtils.spawnParticlesInRing(36, 2.5F, 6.5F, radius, 0.8F, 0.1F, mob, ParticleTypes.SCULK_SOUL);
        for (LivingEntity target : targets)
        {
            if (target != null)
            {
                this.mob.getNavigation().stop();
                this.mob.lerpMotion(0, 0, 0);
                mob.playSound(DTESoundRegistry.DARK_MAGIC_SWORD_CAST.get(), 2.5f, Utils.random.nextIntBetweenInclusive(80, 110) * .01f);

                var playerMagicData = MagicData.getPlayerMagicData(target);

                if (!(NeoForge.EVENT_BUS.post(new CounterSpellEvent(mob, target)).isCanceled()))
                {
                    if (target instanceof AntiMagicSusceptible antiMagicSusceptible) {
                        // Disspell all summons that don't belong to us
                        if (antiMagicSusceptible instanceof IMagicSummon summon && summon.getSummoner() != mob) {
                            if (summon.getSummoner() == target) {
                                if (summon instanceof Mob mob && mob.getTarget() == null) {
                                    antiMagicSusceptible.onAntiMagic(playerMagicData);
                                }
                            } else {
                                antiMagicSusceptible.onAntiMagic(playerMagicData);
                            }
                        } else {
                            antiMagicSusceptible.onAntiMagic(playerMagicData);
                        }
                    } else if (target instanceof ServerPlayer serverPlayer) {
                        Utils.serverSideCancelCast(serverPlayer, true);
                        MagicData.getPlayerMagicData(serverPlayer).getPlayerRecasts().removeAll(RecastResult.COUNTERSPELL);
                    } else if (target instanceof IMagicEntity abstractSpellCastingMob) {
                        abstractSpellCastingMob.cancelCast();
                    }
                    if (target instanceof LivingEntity livingEntity) {
                        //toList to avoid concurrent modification
                        for (Holder<MobEffect> mobEffect : livingEntity.getActiveEffectsMap().keySet().stream().toList()) {
                            if (mobEffect.value() instanceof MagicMobEffect) {
                                livingEntity.removeEffect(mobEffect);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void stop() {
        super.stop();
        mob.setTarget(null);
    }
}
