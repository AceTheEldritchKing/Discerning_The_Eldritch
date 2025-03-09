package net.acetheeldritchking.discerning_the_eldritch.entity.mobs;

import io.redspace.ironsspellbooks.api.network.IClientEventEntity;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import net.acetheeldritchking.discerning_the_eldritch.utils.boss_music.BossMusicManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;

public abstract class GenericBossEntity extends AbstractSpellCastingMob implements Enemy, IClientEventEntity {

    public GenericBossEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public final static EntityDataAccessor<Integer> PHASE = SynchedEntityData.defineId(GenericBossEntity.class, EntityDataSerializers.INT);

    public SoundEvent getBossMusic() {

        return null;
    }

    @Override
    public void handleClientEvent(byte b) {
        // Music will be handled here, will be overridden by child classes
    }

    // Phase stuff //
    public enum Phase
    {
        FirstPhase(0),
        SecondPhase(1),
        ThirdPhase(2),
        FourthPhase(3),
        FifthPhase(4),
        SixthPhase(5),
        SeventhPhase(6);

        final int value;

        Phase(int value)
        {
            this.value = value;
        }
    }

    public void setPhase(int phase)
    {
        this.entityData.set(PHASE, phase);
    }

    public void setPhase(Phase phase)
    {
        this.setPhase(phase.value);
    }

    public int getPhase()
    {
        return this.entityData.get(PHASE);
    }

    public boolean isPhase(Phase phase)
    {
        return phase.value == getPhase();
    }

    // NBT
    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        pCompound.putInt("phase", getPhase());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        setPhase(pCompound.getInt("phase"));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(PHASE, 0);
    }
}
