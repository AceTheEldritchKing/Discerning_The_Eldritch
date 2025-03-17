package net.acetheeldritchking.discerning_the_eldritch.spells.eldritch;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.events.SpellSummonEvent;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.TheApostleEntity;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEPotionEffectRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;

@AutoSpellConfig
public class ConjureForsakenAidSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "conjure_forsaken_aid");

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(SchoolRegistry.ELDRITCH_RESOURCE)
            .setMaxLevel(3)
            .setCooldownSeconds(100)
            .build();

    public ConjureForsakenAidSpell()
    {
        this.manaCostPerLevel = 20;
        this.baseSpellPower = 10;
        this.spellPowerPerLevel = 5;
        this.castTime = 30;
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
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        int summonTimer = (int) (20 * (20 * getSpellPower(spellLevel, entity)));

        BlockPos pos = new BlockPos((int) entity.getX(), (int) entity.getY(), (int) entity.getZ());

        spawnForsakenAid(pos.getX(), pos.getY(), pos.getZ(), entity, level, summonTimer, spellLevel);

        entity.addEffect(new MobEffectInstance(DTEPotionEffectRegistry.FORSAKEN_TIMER, summonTimer, 0, false, false, true));

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private void spawnForsakenAid(double x, double y, double z, LivingEntity caster, Level level, int timer, int spellLevel)
    {
        TheApostleEntity apostleEntity = new TheApostleEntity(level, caster);

        apostleEntity.addEffect(new MobEffectInstance(DTEPotionEffectRegistry.FORSAKEN_TIMER, timer, 0, false, false, true));

        apostleEntity.setPos(x, y, z);
        apostleEntity.setOldPosAndRot();

        var event = NeoForge.EVENT_BUS.post(new SpellSummonEvent<>(caster, apostleEntity, this.spellId, spellLevel));

        level.addFreshEntity(event.getCreature());
    }
}
