package net.acetheeldritchking.discerning_the_eldritch.spells.ritual;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.acetheeldritchking.aces_spell_utils.registries.ASSchoolRegistry;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.bosses.ascended_one.AscendedOneBoss;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTESchoolRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class CallAscendedOneSpell extends AbstractRitualSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "call_ascended_one");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.hp", Utils.stringTruncation(getAscendedHealth(spellLevel, null), 2)),
                Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation(getAscendedDamage(spellLevel, null), 2)),
                Component.translatable("ui.discerning_the_eldritch.spell_power", Utils.stringTruncation(getAscendedSpellPower(spellLevel, null), 2)),
                Component.translatable("ui.discerning_the_eldritch.spell_resist", Utils.stringTruncation(getAscendedSpellResist(spellLevel, null), 2))
        );
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(ASSchoolRegistry.RITUAL_RESOURCE)
            .setMaxLevel(1)
            .setCooldownSeconds(1000)
            .build();

    public CallAscendedOneSpell()
    {
        this.manaCostPerLevel = 100;
        this.baseSpellPower = 10;
        this.spellPowerPerLevel = 10;
        this.castTime = 100;
        this.baseManaCost = 1000;
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
    public boolean isSuperComplex() {
        return true;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        Vec3 vec = entity.getEyePosition();

        double randomNearbyX = vec.x + entity.getRandom().nextGaussian() * 3;
        double randomNearbyZ = vec.z + entity.getRandom().nextGaussian() * 3;

        spawnAscendedOneBoss(randomNearbyX, vec.y, randomNearbyZ, entity, level, spellLevel);

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private void spawnAscendedOneBoss(double x, double y, double z, LivingEntity caster, Level level, int spellLevel)
    {
        AscendedOneBoss ascendedOneBoss = new AscendedOneBoss(level, false);

        ascendedOneBoss.setPos(x, y, z);
        ascendedOneBoss.setOldPosAndRot();
        ascendedOneBoss.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(getAscendedDamage(spellLevel, caster));
        ascendedOneBoss.getAttributes().getInstance(AttributeRegistry.SPELL_POWER).setBaseValue(getAscendedSpellPower(spellLevel, caster));
        ascendedOneBoss.getAttributes().getInstance(AttributeRegistry.SPELL_RESIST).setBaseValue(getAscendedSpellResist(spellLevel, caster));
        ascendedOneBoss.getAttributes().getInstance(Attributes.MAX_HEALTH).setBaseValue(getAscendedHealth(spellLevel, caster));
        ascendedOneBoss.setHealth(ascendedOneBoss.getMaxHealth());

        ascendedOneBoss.finalizeSpawn((ServerLevelAccessor) level,
                level.getCurrentDifficultyAt(ascendedOneBoss.getOnPos()),
                MobSpawnType.MOB_SUMMONED, null);


        level.addFreshEntity(ascendedOneBoss);
    }

    private float getAscendedDamage(int spellLevel, LivingEntity caster)
    {
        return getSpellPower(spellLevel, caster) * 2.5f;
    }

    private float getAscendedHealth(int spellLevel, LivingEntity caster)
    {
        return getSpellPower(spellLevel, caster) * 60.5f;
    }

    private float getAscendedSpellPower(int spellLevel, LivingEntity caster)
    {
        return getSpellPower(spellLevel, caster) * 0.75f;
    }

    private float getAscendedSpellResist(int spellLevel, LivingEntity caster)
    {
        return getSpellPower(spellLevel, caster) * 0.075f;
    }
}
