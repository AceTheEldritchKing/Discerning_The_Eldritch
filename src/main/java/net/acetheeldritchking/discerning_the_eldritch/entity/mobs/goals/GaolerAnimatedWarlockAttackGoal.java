package net.acetheeldritchking.discerning_the_eldritch.entity.mobs.goals;

import io.redspace.ironsspellbooks.entity.mobs.wizards.GenericAnimatedWarlockAttackGoal;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.GaolerEntity;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTESoundRegistry;

public class GaolerAnimatedWarlockAttackGoal extends GenericAnimatedWarlockAttackGoal {
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
}
