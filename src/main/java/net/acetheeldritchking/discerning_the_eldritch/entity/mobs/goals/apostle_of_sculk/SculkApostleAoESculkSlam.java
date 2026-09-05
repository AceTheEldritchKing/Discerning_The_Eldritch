package net.acetheeldritchking.discerning_the_eldritch.entity.mobs.goals.apostle_of_sculk;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss.goals.AnimatedActionGoal;
import io.redspace.ironsspellbooks.entity.spells.void_tentacle.VoidTentacle;
import net.acetheeldritchking.aces_spell_utils.utils.ASUtils;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.bosses.apostle_of_sculk.ApostleOfSculkBoss;
import net.acetheeldritchking.discerning_the_eldritch.entity.spells.sculk_explosion.SculkSlamAoE;
import net.acetheeldritchking.discerning_the_eldritch.entity.spells.soul_eruption.SoulEruptionAoe;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTESoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class SculkApostleAoESculkSlam extends AnimatedActionGoal<ApostleOfSculkBoss> {
    public SculkApostleAoESculkSlam(ApostleOfSculkBoss mob) {
        super(mob);
    }

    @Override
    protected boolean canStartAction() {
        // Do it if the mob is close to us, so we can get them out of our face
        return mob.onGround() && mob.getTarget() != null && mob.distanceToSqr(mob.getTarget()) < 5 * 5;
    }

    @Override
    protected int getActionTimestamp() {
        return 25;
    }

    @Override
    protected int getActionDuration() {
        return 30;
    }

    @Override
    protected int getCooldown() {
        return Utils.random.nextIntBetweenInclusive(200, 240);
    }

    @Override
    protected String getAnimationId() {
        return "overhead_staff_slam1";
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
        DiscerningTheEldritch.LOGGER.debug("GO INTO AOE SLAM GOAL!!!");

        mob.playSound(DTESoundRegistry.SOUL_SLAM_ECHO.get(), 2.5f, Utils.random.nextIntBetweenInclusive(80, 110) * .01f);

        float radius = 12.5F;
        int count = 3;
        int rings = 3;
        Vec3 center = mob.getEyePosition();

        SculkSlamAoE slamAoE = new SculkSlamAoE(mob.level(), radius);
        slamAoE.setOwner(mob);
        slamAoE.setDamage(50);
        slamAoE.moveTo(center);
        mob.level().addFreshEntity(slamAoE);

        /*for (int i = 0; i < rings; i++)
        {
            float tentacles = count + i + i;

            for (int j = 0; j < tentacles; j++)
            {
                Vec3 spawn = center.add(new Vec3(0,0, 1.5F * (i + 1)).yRot(mob.getYRot() * Mth.DEG_TO_RAD + ((6.281F / tentacles) * j)));
                spawn = Utils.moveToRelativeGroundLevel(this.mob.level(), spawn, 5);

                if (!this.mob.level().getBlockState(BlockPos.containing(spawn).below()).isAir())
                {
                    VoidTentacle tentacle = new VoidTentacle(this.mob.level(), mob, 20);
                    tentacle.moveTo(spawn);
                    tentacle.setYRot(Utils.random.nextInt(360));

                    this.mob.level().addFreshEntity(tentacle);
                }
            }
        }

        for (int i = 0; i < rings; i++)
        {
            float tentacles = count + i + i;

            for (int j = 0; j < tentacles; j++)
            {
                Vec3 spawn = center.add(new Vec3(0,0, 1.5F * (i + 4)).yRot(mob.getYRot() * Mth.DEG_TO_RAD + ((6.281F / tentacles) * j)));
                spawn = Utils.moveToRelativeGroundLevel(this.mob.level(), spawn, 5);

                if (!this.mob.level().getBlockState(BlockPos.containing(spawn).below()).isAir())
                {
                    VoidTentacle tentacle = new VoidTentacle(this.mob.level(), mob, 20);
                    tentacle.moveTo(spawn);
                    tentacle.setYRot(Utils.random.nextInt(360));

                    this.mob.level().addFreshEntity(tentacle);
                }
            }
        }

        for (int i = 0; i < rings; i++)
        {
            float tentacles = count + i + i;

            for (int j = 0; j < tentacles; j++)
            {
                Vec3 spawn = center.add(new Vec3(0,0, 1.5F * (i + 7)).yRot(mob.getYRot() * Mth.DEG_TO_RAD + ((6.281F / tentacles) * j)));
                spawn = Utils.moveToRelativeGroundLevel(this.mob.level(), spawn, 5);

                if (!this.mob.level().getBlockState(BlockPos.containing(spawn).below()).isAir())
                {
                    VoidTentacle tentacle = new VoidTentacle(this.mob.level(), mob, 20);
                    tentacle.moveTo(spawn);
                    tentacle.setYRot(Utils.random.nextInt(360));

                    this.mob.level().addFreshEntity(tentacle);
                }
            }
        }*/
    }

    @Override
    public void stop() {
        super.stop();
        mob.setTarget(null);
    }
}
