package net.acetheeldritchking.discerning_the_eldritch.entity.mobs.goals.apostle_of_sculk;

import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.entity.mobs.goals.melee.AttackKeyframe;
import io.redspace.ironsspellbooks.entity.mobs.wizards.GenericAnimatedWarlockAttackGoal;
import io.redspace.ironsspellbooks.entity.spells.EarthquakeAoe;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.bosses.apostle_of_sculk.keyframes.ApostleOfSculkAttackKeyframe;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.bosses.apostle_of_sculk.keyframes.ApostleOfSculkBlockKeyframe;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.bosses.apostle_of_sculk.ApostleOfSculkBoss;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.bosses.apostle_of_sculk.keyframes.ApostleOfSculkHeavyAttackKeyframe;
import net.acetheeldritchking.discerning_the_eldritch.particle.SoulFireSlashParticleOptions;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTESoundRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class ApostleOfSculkAnimatedWarlockAttackGoal extends GenericAnimatedWarlockAttackGoal<ApostleOfSculkBoss> {
    final ApostleOfSculkBoss apostle;

    public ApostleOfSculkAnimatedWarlockAttackGoal(ApostleOfSculkBoss entity, double pSpeedModifier, int minAttackInterval, int maxAttackInterval) {
        super(entity, pSpeedModifier, minAttackInterval, maxAttackInterval);
        this.apostle = entity;
    }

    @Override
    protected double movementSpeed() {
        return this.meleeMoveSpeedModifier;
    }

    @Override
    protected void doMovement(double distanceSquared) {
        double speed = (apostle.isCasting() ? 0.75F : 1F) * movementSpeed();
        apostle.lookAt(target, 30, 30);
        var meleeRange = meleeRange();
        float strafeMultiplier = getStrafeMultiplier();

        if (distanceSquared < spellcastingRangeSqr && seeTime >= 5)
        {
            apostle.getNavigation().stop();
            if (++strafeTime > 40)
            {
                if (apostle.getRandom().nextDouble() < 0.08D)
                {
                    strafingClockwise = !strafingClockwise;
                    strafeTime = 0;
                }
            }

            float strafeForward = meleeMoveSpeedModifier;

            if (distanceSquared > meleeRange * meleeRange * 3 * 3)
            {
                strafeForward *= 2F;
            } else if (distanceSquared > meleeRange * meleeRange * 0.75F * 0.75F)
            {
                strafeForward *= 1.3F;
            } else
            {
                strafeForward *= -1.15F;
            }

            int strafeDir = strafingClockwise ? 1 : -1;
            apostle.getMoveControl().strafe(strafeForward * strafeMultiplier, (float) (speed * strafeDir * strafeMultiplier));
        } else
        {
            if (apostle.tickCount % 5 == 0)
            {
                apostle.setXxa(0);
                apostle.getNavigation().moveTo(this.target, speedModifier);
            }
        }
    }

    public void setTarget(LivingEntity target) {
        this.target = target;
    }

    @Override
    protected void onHitFrame(AttackKeyframe attackKeyframe, float meleeRange) {
        super.onHitFrame(attackKeyframe, meleeRange);
        if (attackKeyframe instanceof ApostleOfSculkBlockKeyframe)
        {
            this.mob.procParry();
        }
        else
        {
            super.onHitFrame(attackKeyframe, meleeRange);
            if (attackKeyframe instanceof ApostleOfSculkAttackKeyframe apostleKeyFrame)
            {
                boolean mirrored = apostleKeyFrame.swingData.mirrored();
                boolean vertical = apostleKeyFrame.swingData.vertical();

                Vec3 forward = apostle.getForward();
                float reach = 2 * apostle.getScale();
                Vec3 hitLoc = apostle.getBoundingBox().getCenter().add(apostle.getForward().multiply(reach, 0.5F, reach));
                MagicManager.spawnParticles(apostle.level(),
                        new SoulFireSlashParticleOptions((float) forward.x, (float) forward.y, (float) forward.z, mirrored, vertical, mob.getScale()), hitLoc.x, hitLoc.y, hitLoc.z, 1, 0, 0, 0, 0, true);
            }
            // This is AoE w/o being AoE
            if (attackKeyframe instanceof ApostleOfSculkHeavyAttackKeyframe apostleKeyFrame)
            {
                boolean mirrored = apostleKeyFrame.swingData.mirrored();
                boolean vertical = apostleKeyFrame.swingData.vertical();
                boolean aoe = apostleKeyFrame.swingData.aoe();

                Vec3 forward = apostle.getForward();
                float reach = 2 * apostle.getScale();
                Vec3 hitLoc = apostle.getBoundingBox().getCenter().add(apostle.getForward().multiply(reach, 0.5F, reach));
                MagicManager.spawnParticles(apostle.level(),
                        new SoulFireSlashParticleOptions((float) forward.x, (float) forward.y, (float) forward.z, mirrored, vertical, mob.getScale()), hitLoc.x, hitLoc.y, hitLoc.z, 1, 0, 0, 0, 0, true);

                if (aoe)
                {
                    var targets = mob.level().getEntitiesOfClass(target.getClass(), mob.getBoundingBox().inflate(3.5));
                    for (LivingEntity target : targets)
                    {
                        handleDamaging(target, apostleKeyFrame);
                    }
                }

                spawnVisualEarthquake();
            }
        }
    }

    @Override
    protected void handleAttackLogic(double distanceSquared) {
        super.handleAttackLogic(distanceSquared);

        if (meleeAnimTimer > 0 && currentAttack != null)
        {
            int shortcut = 5;
            if (meleeAnimTimer < shortcut)
            {
                if (currentAttack.attacks.keySet().intStream().noneMatch(i -> i > currentAttack.lengthInTicks - shortcut))
                {
                    meleeAnimTimer = 0;
                }
            }
        }

        // Spin attack
        if (currentAttack != null)
        {
            if (currentAttack.animationId.equals("spin_slash_melee"))
            {
                if (currentAttack.lengthInTicks >= 20 && isMeleeing())
                {
                    //DiscerningTheEldritch.LOGGER.debug("Is Meleeing to stop movement?");
                    //DiscerningTheEldritch.LOGGER.debug("Current Attack Length in Ticks: " + currentAttack.lengthInTicks);
                    //DiscerningTheEldritch.LOGGER.debug("Melee Timer: " + meleeAnimTimer);
                    this.mob.getNavigation().stop();
                    this.mob.lerpMotion(0, 0, 0);
                }
                if (currentAttack.isHitFrame(meleeAnimTimer))
                {
                    AttackKeyframe attackData = currentAttack.getHitFrame(meleeAnimTimer);
                    //onHitFrame(attackData, meleeRange());

                    var targets = mob.level().getEntitiesOfClass(target.getClass(), mob.getBoundingBox().inflate(3.5));
                    for (LivingEntity target : targets)
                    {
                        handleDamaging(target, attackData);
                    }
                }
            }
        }
        // Slam attack
        // Forwards rush attack
        // Super staff slam attack
    }

    private void spawnVisualEarthquake()
    {
        EarthquakeAoe aoe = new EarthquakeAoe(this.mob.level());
        aoe.moveTo(this.target.position());
        aoe.setOwner(this.mob);
        aoe.setCircular();
        aoe.setRadius(8);
        aoe.setDuration(10);
        aoe.setDamage(0.0F);
        aoe.setSlownessAmplifier(0);

        this.mob.level().addFreshEntity(aoe);
    }

    @Override
    public void playSwingSound() {
        apostle.playSound(DTESoundRegistry.SOUL_SLAM.get(), 10.0F, apostle.getVoicePitch());
    }
}
