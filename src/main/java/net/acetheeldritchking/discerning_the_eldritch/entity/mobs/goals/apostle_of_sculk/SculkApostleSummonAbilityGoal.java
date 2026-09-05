package net.acetheeldritchking.discerning_the_eldritch.entity.mobs.goals.apostle_of_sculk;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss.goals.AnimatedActionGoal;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.bosses.apostle_of_sculk.ApostleOfSculkBoss;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.sculk_mobs.the_assimilated.AssimilatedEntity;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTESoundRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;

public class SculkApostleSummonAbilityGoal extends AnimatedActionGoal<ApostleOfSculkBoss> {
    public SculkApostleSummonAbilityGoal(ApostleOfSculkBoss mob) {
        super(mob);
    }

    @Override
    protected boolean canStartAction() {
        return mob.onGround() && mob.getTarget() != null;
    }

    @Override
    protected int getActionTimestamp() {
        return 20;
    }

    @Override
    protected int getActionDuration() {
        return 37;
    }

    @Override
    protected int getCooldown() {
        return Utils.random.nextIntBetweenInclusive(245, 395);
    }

    @Override
    protected String getAnimationId() {
        return "apostle_of_sculk_summon";
    }

    @Override
    public void tick() {
        if (mob.getTarget() != null) {
            mob.setTarget(mob.getTarget());
        }

        if (abilityTimer >= 6 && abilityTimer <= 8)
        {
            mob.playSound(DTESoundRegistry.APOSTLE_OF_SCULK_SUMMON_ENTITY.get(), 2.5f, 1);
        }
        super.tick();
    }

    @Override
    protected void doAction() {
        DiscerningTheEldritch.LOGGER.debug("GO INTO SUMMON GOAL!!!");

        int simCount = 5;
        float simRadius = 1.5F + 0.185F * simCount;

        for (int i = 0; i < simCount; i++)
        {
            AssimilatedEntity assimilated = new AssimilatedEntity(mob.level());
            assimilated.finalizeSpawn((ServerLevel) mob.level(), mob.level().getCurrentDifficultyAt(assimilated.getOnPos()), MobSpawnType.MOB_SUMMONED, null);
            var aosYRot = 6.281F / simCount * i + mob.getYRot() * Mth.DEG_TO_RAD;
            Vec3 spawn = Utils.moveToRelativeGroundLevel(mob.level(), mob.getEyePosition().add(new Vec3(simRadius * Mth.cos(aosYRot), 0, simRadius * Mth.sin(aosYRot))), 10);
            spawn = mob.level().clip(new ClipContext(mob.getEyePosition(), spawn, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, CollisionContext.empty())).getLocation();

            if (!mob.level().noCollision(assimilated.getBoundingBox().move(spawn)))
            {
                spawn = Utils.moveToRelativeGroundLevel(mob.level(), spawn.add(mob.getEyePosition().subtract(spawn).normalize().scale(mob.getBbWidth() * 1.1)), 3);
            }

            assimilated.setPos(spawn.x, spawn.y, spawn.z);
            assimilated.setYRot(mob.getYRot());
            assimilated.setOldPosAndRot();
            mob.level().addFreshEntity(assimilated);
        }
    }
}
