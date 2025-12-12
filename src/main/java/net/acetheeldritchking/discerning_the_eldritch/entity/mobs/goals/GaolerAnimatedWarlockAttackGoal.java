package net.acetheeldritchking.discerning_the_eldritch.entity.mobs.goals;

import io.redspace.ironsspellbooks.entity.mobs.goals.melee.AttackKeyframe;
import io.redspace.ironsspellbooks.entity.mobs.wizards.GenericAnimatedWarlockAttackGoal;
import io.redspace.ironsspellbooks.entity.spells.EarthquakeAoe;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.gaoler.GaolerEntity;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTESoundRegistry;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class GaolerAnimatedWarlockAttackGoal extends GenericAnimatedWarlockAttackGoal<GaolerEntity> {
    final GaolerEntity gaoler;

    public GaolerAnimatedWarlockAttackGoal(GaolerEntity entity, double pSpeedModifier, int minAttackInterval, int maxAttackInterval) {
        super(entity, pSpeedModifier, minAttackInterval, maxAttackInterval);
        this.gaoler = entity;
        this.wantsToMelee = true;
    }

    @Override
    public void playSwingSound() {
        gaoler.playSound(DTESoundRegistry.GAOLER_ATTACK_IMPACT.get(), 10.0F, gaoler.getVoicePitch());
    }

    @Override
    protected void handleAttackLogic(double distanceSquared) {
        super.handleAttackLogic(distanceSquared);

        if (currentAttack != null)
        {
            if (currentAttack.animationId.equals("slam_1"))
            {
                if (currentAttack.lengthInTicks >= 24 && isMeleeing())
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

                    spawnVisualEarthquake();
                }
            }
        }

        /*if (isMeleeing())
        {
            DiscerningTheEldritch.LOGGER.debug("Is Meleeing to stop movement?");
            this.mob.getNavigation().stop();
            this.mob.lerpMotion(0, 0, 0);
        }*/
    }

    private void spawnVisualEarthquake()
    {
        EarthquakeAoe aoe = new EarthquakeAoe(this.mob.level());
        aoe.moveTo(this.target.position());
        aoe.setOwner(this.mob);
        aoe.setCircular();
        aoe.setRadius(8);
        aoe.setDuration(10);
        aoe.setDamage(1.0F);
        aoe.setSlownessAmplifier(0);

        this.mob.level().addFreshEntity(aoe);
    }
}
