package net.acetheeldritchking.discerning_the_eldritch.spells.ritual;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AutoSpellConfig;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEPotionEffectRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTESchoolRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class RavenousRevenantSpell extends AbstractRitualSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "ravenous_revenant");

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(DTESchoolRegistry.RITUAL_RESOURCE)
            .setMaxLevel(1)
            .setCooldownSeconds(35)
            .build();

    public RavenousRevenantSpell()
    {
        this.manaCostPerLevel = 15;
        this.baseSpellPower = 25;
        this.spellPowerPerLevel = 2;
        this.castTime = 20;
        this.baseManaCost = 100;
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
        return CastType.LONG;
    }

    @Override
    public boolean isComplex() {
        return true;
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundRegistry.BLIGHT_BEGIN.get());
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        double radius = 15;

        List<LivingEntity> entitiesNearby = level.getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(radius));
        for (LivingEntity targets : entitiesNearby)
        {
            targets.addEffect(new MobEffectInstance(DTEPotionEffectRegistry.PREY_POTION_EFFECT, getDuration(spellLevel, entity), 0, false, false, false));
        }

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private int getDuration(int spellLevel, LivingEntity caster)
    {
        return (int) (20 * (getSpellPower(spellLevel, caster)));
    }
}
