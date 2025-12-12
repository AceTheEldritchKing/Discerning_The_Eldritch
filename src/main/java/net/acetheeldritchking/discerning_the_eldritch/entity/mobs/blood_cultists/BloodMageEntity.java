package net.acetheeldritchking.discerning_the_eldritch.entity.mobs.blood_cultists;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.NeutralWizard;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

// Empty class to be used by the blood mages
public class BloodMageEntity extends NeutralWizard {
    protected BloodMageEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
}
