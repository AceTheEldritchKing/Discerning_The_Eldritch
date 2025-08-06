package net.acetheeldritchking.discerning_the_eldritch.spells.eldritch;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.events.SpellSummonEvent;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.*;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.SightlessMawEntity;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.TheApostleEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.Nullable;

@AutoSpellConfig
public class ConjureForsakenAidSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "conjure_forsaken_aid");

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(SchoolRegistry.ELDRITCH_RESOURCE)
            .setMaxLevel(5)
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
    public int getRecastCount(int spellLevel, @Nullable LivingEntity entity) {
        return 2;
    }

    @Override
    public void onRecastFinished(ServerPlayer serverPlayer, RecastInstance recastInstance, RecastResult recastResult, ICastDataSerializable castDataSerializable) {
        if (SummonManager.recastFinishedHelper(serverPlayer, recastInstance, recastResult, castDataSerializable)) {
            super.onRecastFinished(serverPlayer, recastInstance, recastResult, castDataSerializable);
        }
    }

    @Override
    public ICastDataSerializable getEmptyCastData() {
        return new SummonedEntitiesCastData();
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        PlayerRecasts recasts = playerMagicData.getPlayerRecasts();

        if (!recasts.hasRecastForSpell(this))
        {
            SummonedEntitiesCastData summonedEntitiesCastData = new SummonedEntitiesCastData();
            int summonTimer = (int) (20 * (20 * getSpellPower(spellLevel, entity)));

            for (int i = 0; i < spellLevel; i++)
            {
                Vec3 vec = entity.getEyePosition();

                double randomNearbyX = vec.x + entity.getRandom().nextGaussian() * 3;
                double randomNearbyZ = vec.z + entity.getRandom().nextGaussian() * 3;

                spawnForsakenAid(randomNearbyX, vec.y, randomNearbyZ, entity, level, summonTimer, spellLevel, summonedEntitiesCastData);
            }

            RecastInstance recastInstance = new RecastInstance(this.getSpellId(), spellLevel, getRecastCount(spellLevel, entity), summonTimer, castSource, summonedEntitiesCastData);
            recasts.addRecast(recastInstance, playerMagicData);
        }

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private void spawnForsakenAid(double x, double y, double z, LivingEntity caster, Level level, int timer, int spellLevel, SummonedEntitiesCastData castData)
    {
        boolean isMaw = Utils.random.nextDouble() < 0.5;
        boolean isBehemoth = Utils.random.nextDouble() < 0.4;
        boolean isApostle = Utils.random.nextDouble() < 0.2;

        boolean isBase = Utils.random.nextDouble() < 0.6;

        TheApostleEntity apostleEntity = new TheApostleEntity(level, caster);
        SightlessMawEntity sightlessMaw = new SightlessMawEntity(level, caster);

        AbstractSpellCastingMob isMeleeMobs = isMaw ? sightlessMaw : apostleEntity;
        AbstractSpellCastingMob isCaster = isApostle ? apostleEntity : isMeleeMobs;

        AbstractSpellCastingMob baseArmy = isBase ? isMeleeMobs : isCaster;

        baseArmy.setPos(x, y, z);
        baseArmy.setOldPosAndRot();

        var event = NeoForge.EVENT_BUS.post(new SpellSummonEvent<>(caster, baseArmy, this.spellId, spellLevel)).getCreature();

        level.addFreshEntity(event);

        SummonManager.initSummon(caster, event, timer, castData);
    }
}
