package net.acetheeldritchking.discerning_the_eldritch.entity.mobs.bosses.apostle_of_sculk.keyframes;

import io.redspace.ironsspellbooks.entity.mobs.goals.melee.AttackKeyframe;
import net.minecraft.world.phys.Vec3;

public class ApostleOfSculkHeavyAttackKeyframe extends AttackKeyframe {
    public record SwingData(boolean vertical, boolean mirrored, boolean aoe)
    {
        //
    }

    final public SwingData swingData;

    public ApostleOfSculkHeavyAttackKeyframe(int timeStamp, Vec3 lungeVector, SwingData swingData) {
        this(timeStamp, lungeVector, Vec3.ZERO, swingData);
    }

    public ApostleOfSculkHeavyAttackKeyframe(int timeStamp, Vec3 lungeVector, Vec3 extraKnockback, SwingData swingData) {
        super(timeStamp, lungeVector, extraKnockback);
        this.swingData = swingData;
    }
}
