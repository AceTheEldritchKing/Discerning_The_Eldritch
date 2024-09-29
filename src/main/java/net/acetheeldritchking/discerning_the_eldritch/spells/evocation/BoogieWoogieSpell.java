package net.acetheeldritchking.discerning_the_eldritch.spells.evocation;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.RecastInstance;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.registeries.DTESoundRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registeries.ItemRegistries;
import net.acetheeldritchking.discerning_the_eldritch.spells.DTESpellAnimations;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@AutoSpellConfig
public class BoogieWoogieSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(DiscerningTheEldritch.MOD_ID, "boogie_woogie");

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.RARE)
            .setSchoolResource(SchoolRegistry.EVOCATION_RESOURCE)
            .setMaxLevel(5)
            .setCooldownSeconds(10)
            .build();

    public BoogieWoogieSpell()
    {
        this.manaCostPerLevel = 5;
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 1;
        this.castTime = 0;
        this.baseManaCost = 20;
    }

    @Override
    public ResourceLocation getSpellResource() {
        return spellId;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    @Override
    public CastType getCastType() {
        return CastType.INSTANT;
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(DTESoundRegistry.CLAP_SPELL_CAST.get());
    }

    @Override
    public AnimationHolder getCastFinishAnimation() {
        return DTESpellAnimations.ANIMATION_CLAP;
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        return Utils.preCastTargetHelper(level, entity, playerMagicData, this, 32, .15f);
    }

    @Override
    public int getRecastCount(int spellLevel, @Nullable LivingEntity entity) {
        if (ItemRegistries.ECHO_VIBRATION_RING.get().isEquippedBy(entity))
        {
            return 1 + spellLevel;
        }
        else {
            return 1;
        }
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (!playerMagicData.getPlayerRecasts().hasRecastForSpell(getSpellId()))
        {
            playerMagicData.getPlayerRecasts().addRecast
                    (new RecastInstance(getSpellId(), spellLevel, getRecastCount(spellLevel, entity), 80, castSource, null), playerMagicData);
        }

        if (playerMagicData.getAdditionalCastData() instanceof TargetEntityCastData targetEntityCastData)
        {
            var targetEntity = targetEntityCastData.getTarget((ServerLevel) level);
            if (targetEntity != null)
            {
                final Vec3 targetPos = targetEntity.position();
                final Vec3 casterPos = entity.position();

                entity.teleportTo(targetPos.x, targetPos.y, targetPos.z);
                targetEntity.teleportTo(casterPos.x, casterPos.y, casterPos.z);

                targetEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 60, 0, true, true, true));
            }
        }

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
}
