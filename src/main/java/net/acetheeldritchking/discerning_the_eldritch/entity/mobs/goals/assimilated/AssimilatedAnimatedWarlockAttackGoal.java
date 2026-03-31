package net.acetheeldritchking.discerning_the_eldritch.entity.mobs.goals.assimilated;

import io.redspace.ironsspellbooks.entity.mobs.goals.melee.AttackKeyframe;
import io.redspace.ironsspellbooks.entity.mobs.wizards.GenericAnimatedWarlockAttackGoal;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.sculk_mobs.the_assimilated.AssimilatedEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.Vec3;

public class AssimilatedAnimatedWarlockAttackGoal extends GenericAnimatedWarlockAttackGoal<AssimilatedEntity> {
    final AssimilatedEntity assimilated;

    public AssimilatedAnimatedWarlockAttackGoal(AssimilatedEntity entity, double pSpeedModifier, int minAttackInterval, int maxAttackInterval) {
        super(entity, pSpeedModifier, minAttackInterval, maxAttackInterval);
        this.assimilated = entity;
        this.wantsToMelee = true;
    }

    @Override
    public void playSwingSound() {
        assimilated.playSound(SoundEvents.PLAYER_ATTACK_STRONG);
    }

    @Override
    protected void handleAttackLogic(double distanceSquared) {
        super.handleAttackLogic(distanceSquared);

        if (currentAttack != null)
        {
            if (currentAttack.animationId.equals("basic_melee_lunge"))
            {
                if (currentAttack.lengthInTicks >= 24 && isMeleeing())
                {
                    //DiscerningTheEldritch.LOGGER.debug("Is Meleeing to stop movement?");
                    //DiscerningTheEldritch.LOGGER.debug("Current Attack Length in Ticks: " + currentAttack.lengthInTicks);
                    //DiscerningTheEldritch.LOGGER.debug("Melee Timer: " + meleeAnimTimer);
                    this.mob.getNavigation().stop();
                    this.mob.lerpMotion(0, 0, 0);
                }
                // get it right before the attack itself
                if (currentAttack.isHitFrame(meleeAnimTimer - 3))
                {
                    // Getting target coords
                    int xTarget = (int) target.getX();
                    int zTarget = (int) target.getZ();
                    // Getting attacker coords
                    int xAttacker = (int) assimilated.getX();
                    int zAttacker = (int) assimilated.getZ();
                    Vec3 vec3 = new Vec3(xTarget, 0, zTarget).subtract(xAttacker, 0, zAttacker).multiply(1.5, 0, 1.5).normalize();

                    //AttackKeyframe attackData = currentAttack.getHitFrame(meleeAnimTimer);
                    Vec3 lunge = target.position().subtract(mob.position()).normalize().multiply(2.4, .5, 2.4);
                    doLunge(new Vec3(lunge.x, 0.25, lunge.z), 4F);
                    //onHitFrame(attackData, meleeRange());
                }
            }
        }
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
    }
}
