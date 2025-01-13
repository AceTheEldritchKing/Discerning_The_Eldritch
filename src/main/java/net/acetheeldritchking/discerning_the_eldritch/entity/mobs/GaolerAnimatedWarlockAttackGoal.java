package net.acetheeldritchking.discerning_the_eldritch.entity.mobs;

import io.redspace.ironsspellbooks.entity.mobs.wizards.GenericAnimatedWarlockAttackGoal;
import net.minecraft.sounds.SoundEvents;

public class GaolerAnimatedWarlockAttackGoal extends GenericAnimatedWarlockAttackGoal {
    final GaolerEntity gaoler;

    public GaolerAnimatedWarlockAttackGoal(GaolerEntity entity, double pSpeedModifier, int minAttackInterval, int maxAttackInterval, float meleeRange) {
        super(entity, pSpeedModifier, minAttackInterval, maxAttackInterval, meleeRange);
        this.gaoler = entity;
        this.wantsToMelee = true;
    }

    @Override
    public void playSwingSound() {
        gaoler.playSound(SoundEvents.WARDEN_ATTACK_IMPACT, 10.0F, gaoler.getVoicePitch());
    }
}
