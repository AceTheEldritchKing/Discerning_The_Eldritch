package net.acetheeldritchking.discerning_the_eldritch.spells.ritual;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MultiTargetEntityCastData;
import io.redspace.ironsspellbooks.capabilities.magic.RecastInstance;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import net.acetheeldritchking.aces_spell_utils.registries.ASSchoolRegistry;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.entity.spells.blade_of_rancor.BladeOfRancorProjectile;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTESchoolRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTESoundRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class BladesOfRancorSpell extends AbstractRitualSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "blades_of_rancor");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.damage", getDamageText(spellLevel, caster)),
                Component.translatable("ui.discerning_the_eldritch.extra_damage")
        );
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(ASSchoolRegistry.RITUAL_RESOURCE)
            .setMaxLevel(5)
            .setCooldownSeconds(20)
            .build();

    public BladesOfRancorSpell()
    {
        this.manaCostPerLevel = 5;
        this.baseSpellPower = 5;
        this.spellPowerPerLevel = 2;
        this.castTime = 0;
        this.baseManaCost = 90;
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
    public boolean isComplex() {
        return false;
    }

    @Override
    public boolean isSuperComplex() {
        return false;
    }

    @Override
    public int getRecastCount(int spellLevel, @Nullable LivingEntity entity) {
        return spellLevel;
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(DTESoundRegistry.MAGIC_SWORD_CAST.get());
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        var recasts = playerMagicData.getPlayerRecasts();
        if (!recasts.hasRecastForSpell(getSpellId()))
        {
            playerMagicData.getPlayerRecasts().addRecast
                    (new RecastInstance(getSpellId(), spellLevel, getRecastCount(spellLevel, entity), 8*20, castSource, null), playerMagicData);
        }

        BladeOfRancorProjectile bladeOfRancor = new BladeOfRancorProjectile(level, entity);
        Vec3 origin = entity.getEyePosition().add(entity.getForward().normalize().scale(0.2F)).subtract(0, 0.15,0);
        bladeOfRancor.setPos(origin.subtract(0, bladeOfRancor.getBbHeight(), 0));
        Vec3 vec3 = entity.getForward().add(0, 0.2, 0).normalize();

        //bladeOfRancor.shootFromRotation(entity, entity.getXRot(), entity.getYHeadRot(), 0, 0.90F, 0.4F);
        bladeOfRancor.shoot(vec3.scale(0.5F), 0.4F);

        bladeOfRancor.setDamage(getDamage(spellLevel, entity));
        bladeOfRancor.setCursorHoming(true);
        bladeOfRancor.setNoGravity(true);
        level.addFreshEntity(bladeOfRancor);

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    @Override
    public SpellDamageSource getDamageSource(@Nullable Entity projectile, Entity attacker) {
        return super.getDamageSource(projectile, attacker).setIFrames(0);
    }

    private float getDamage(int spellLevel, LivingEntity caster)
    {
        return (float) ((getSpellPower(spellLevel, caster) * 0.45) + getWeaponDamage(caster));
    }

    private float getWeaponDamage(LivingEntity caster)
    {
        float weaponDamage = Utils.getWeaponDamage(caster);

        return weaponDamage;
    }

    private String getDamageText(int spellLevel, LivingEntity caster)
    {
        if (caster != null)
        {
            float weaponDamage = Utils.getWeaponDamage(caster);
            String plus = "";
            if (weaponDamage > 0)
            {
                plus = String.format(" (+%s)", Utils.stringTruncation(weaponDamage, 1));
            }
            String damage = Utils.stringTruncation(getDamage(spellLevel, caster), 1);
            return damage + plus;
        }
        return "" + getSpellPower(spellLevel, caster);
    }

    @Override
    public ICastDataSerializable getEmptyCastData() {
        return new MultiTargetEntityCastData();
    }
}
