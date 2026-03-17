package net.acetheeldritchking.discerning_the_eldritch.entity.mobs.goals.ender_ronin;

import io.redspace.ironsspellbooks.entity.mobs.wizards.GenericAnimatedWarlockAttackGoal;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.ender_ronin.EnderRoninEntity;

public class EnderRoninAnimatedWarlockAttackGoal extends GenericAnimatedWarlockAttackGoal<EnderRoninEntity> {
    final EnderRoninEntity ronin;

    public EnderRoninAnimatedWarlockAttackGoal(EnderRoninEntity entity, double pSpeedModifier, int minAttackInterval, int maxAttackInterval) {
        super(entity, pSpeedModifier, minAttackInterval, maxAttackInterval);
        this.ronin = entity;
    }

    @Override
    protected double movementSpeed() {
        return this.meleeMoveSpeedModifier;
    }

    @Override
    protected void doMovement(double distanceSquared) {
        double speed = (ronin.isCasting() ? 0.55F : 1F) * movementSpeed();
        ronin.lookAt(target, 30, 30);
        var meleeRange = meleeRange();
        float strafeMultiplier = getStrafeMultiplier();

        if (distanceSquared < spellcastingRangeSqr && seeTime >= 5)
        {
            ronin.getNavigation().stop();
            if (++strafeTime > 40)
            {
                if (ronin.getRandom().nextDouble() < 0.08D)
                {
                    strafingClockwise = !strafingClockwise;
                    strafeTime = 0;
                }
            }

            float strafeForward = meleeMoveSpeedModifier;

            if (distanceSquared > meleeRange * meleeRange * 2 * 2)
            {
                strafeForward *= 1.5F;
            } else if (distanceSquared > meleeRange * meleeRange * 0.55F * 0.55F)
            {
                strafeForward *= 1.1F;
            } else
            {
                strafeForward *= -1.05F;
            }

            int strafeDir = strafingClockwise ? 1 : -1;
            ronin.getMoveControl().strafe(strafeForward * strafeMultiplier, (float) (speed * strafeDir * strafeMultiplier));
        } else
        {
            if (ronin.tickCount % 5 == 0)
            {
                ronin.setXxa(0);
                ronin.getNavigation().moveTo(this.target, speedModifier);
            }
        }
    }
}
