package net.acetheeldritchking.discerning_the_eldritch.entity.mobs.goals;

import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;
import java.util.List;

public class WizardSpellComboGoal extends Goal {
    protected static final int interval = 20;
    protected final PathfinderMob mob;
    protected final IMagicEntity spellCastingMob;
    protected LivingEntity target;
    protected final int attackIntervalMin;
    protected final int attackIntervalMax;
    protected final float attackRadius;
    protected final float attackRadiusSqr;
    protected final int spellListLength;
    protected final List<AbstractSpell> spells;
    protected int attackTime;

    protected final int minSpellLevel;
    protected final int maxSpellLevel;

    public WizardSpellComboGoal(IMagicEntity abstractSpellCastingMob, List<AbstractSpell> spells, int minLevel, int maxLevel, int pAttackIntervalMin, int pAttackIntervalMax)
    {
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Flag.TARGET));

        this.spellCastingMob = abstractSpellCastingMob;
        if (abstractSpellCastingMob instanceof PathfinderMob pathfinderMob)
        {
            this.mob = pathfinderMob;
        } else throw new IllegalStateException("Unable to add " + this.getClass().getSimpleName() + "to entity, must extend PathfinderMob.");
        this.attackIntervalMin = pAttackIntervalMin;
        this.attackIntervalMax = pAttackIntervalMax;
        this.attackRadius = 20;
        this.attackRadiusSqr = attackRadius * attackRadius;
        this.minSpellLevel = minLevel;
        this.maxSpellLevel = maxLevel;
        this.spellListLength = spells.toArray().length;
        this.spells = spells;
        resetAttackTimer();
    }

    @Override
    public boolean canUse() {
        target = this.mob.getTarget();
        if (target == null || spellCastingMob.isCasting())
        {
            return false;
        }

        if (attackTime <= -interval * (spellListLength - 1))
        {
            resetAttackTimer();
        }
        attackTime--;

        return attackTime <= 0 && attackTime % interval == 0;
    }

    @Override
    public boolean canContinueToUse() {
        return false;
    }

    @Override
    public void stop() {
        this.target = null;
        if (attackTime > 0)
        {
            this.attackTime = -spellListLength * interval - 1;
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (target == null)
        {
            return;
        }

        double distanceSquared = this.mob.distanceToSqr(target.getX(), target.getY(), target.getZ());
        if (distanceSquared < attackRadiusSqr)
        {
            System.out.println("Begin loop");
            this.mob.getLookControl().setLookAt(target, 45, 45);

            for (int i = 0; i < spellListLength; i++)
            {
                AbstractSpell currentSpell = spells.get(i);

                spellCastingMob.initiateCastSpell(currentSpell, mob.getRandom().nextIntBetweenInclusive(minSpellLevel, maxSpellLevel));
                stop();

                if (spellCastingMob.getMagicData().getCastDurationRemaining() <= 0)
                {

                }
            }
            //stop();
        }
    }

    protected void resetAttackTimer()
    {
        this.attackTime = mob.getRandom().nextIntBetweenInclusive(attackIntervalMin, attackIntervalMax);
    }
}
