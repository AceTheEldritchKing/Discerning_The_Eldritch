package net.acetheeldritchking.discerning_the_eldritch.spells.fire;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import net.acetheeldritchking.aces_spell_utils.spells.ASSpellAnimations;
import net.acetheeldritchking.aces_spell_utils.utils.ASUtils;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.entity.spells.soul_eruption.SoulEruptionAoe;
import net.acetheeldritchking.discerning_the_eldritch.particle.SoulFireSlashParticleOptions;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEDataComponentRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTESchoolRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTESoundRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class SoulSetAblazeSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "soul_set_ablaze");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.damage", getDamageText(spellLevel, caster)),
                Component.translatable("ui.discerning_the_eldritch.scorched_soul_effect"),
                Component.translatable("ui.discerning_the_eldritch.soul_set_ablaze_spending")
        );
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(SchoolRegistry.FIRE_RESOURCE)
            .setMaxLevel(1)
            .setCooldownSeconds(210)
            .build();

    public SoulSetAblazeSpell()
    {
        this.manaCostPerLevel = 20;
        this.baseSpellPower = 40;
        this.spellPowerPerLevel = 1;
        this.castTime = 60;
        this.baseManaCost = 150;
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
    public boolean canBeCraftedBy(Player player) {
        return false;
    }

    @Override
    public boolean allowLooting() {
        return false;
    }

    @Override
    public boolean allowCrafting() {
        return false;
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundEvents.WITHER_AMBIENT);
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(DTESoundRegistry.SOUL_SLAM_ECHO.get());
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return ASSpellAnimations.ANIMATION_OVERHEAD_SWING_START;
    }

    @Override
    public AnimationHolder getCastFinishAnimation() {
        return ASSpellAnimations.ANIMATION_OVERHEAD_SWING_FINISH;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        float radius = 12;
        float range = 2.7F;
        Vec3 forward = entity.getForward();
        Vec3 hitLoc = Utils.moveToRelativeGroundLevel(level, Utils.raycastForBlock(level, entity.getEyePosition(), entity.getEyePosition().add(entity.getForward().multiply(range, 0, range)), ClipContext.Fluid.NONE).getLocation(), 5);
        Vec3 slashLoc = entity.position().add(0, entity.getBbHeight() * 0.3F, 0).add(forward.scale(1.9F));

        SoulEruptionAoe soulEruptionAoe = new SoulEruptionAoe(level, radius);
        soulEruptionAoe.setOwner(entity);

        ItemStack mainhandItem = entity.getMainHandItem();
        Integer soulFireStacks = mainhandItem.get(DTEDataComponentRegistry.SOUL_FIRE_STACKS);
        if (soulFireStacks != null && soulFireStacks >= 50)
        {
            soulEruptionAoe.setDamage((getDamage(spellLevel, entity) * 2));
            mainhandItem.set(DTEDataComponentRegistry.SOUL_FIRE_STACKS, soulFireStacks - 50);
        } else
        {
            soulEruptionAoe.setDamage(getDamage(spellLevel, entity));
        }

        soulEruptionAoe.moveTo(hitLoc);
        level.addFreshEntity(soulEruptionAoe);

        boolean isMirrored = playerMagicData.getCastingEquipmentSlot().equals(SpellSelectionManager.OFFHAND);
        MagicManager.spawnParticles(level, new SoulFireSlashParticleOptions((float) forward.x, (float) forward.y, (float) forward.z, isMirrored, false, 1F), slashLoc.x, slashLoc.y, slashLoc.z, 1, 0, 0, 0, 0, true);
        MagicManager.spawnParticles(level, new BlastwaveParticleOptions(ASUtils.rbgToVector3F(39, 166, 245), radius), entity.getX(), entity.getY() + 0.35F, entity.getZ(), 1, 0, 0, 0, 0, true);

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    @Override
    public SpellDamageSource getDamageSource(Entity projectile, Entity attacker) {
        return super.getDamageSource(projectile, attacker).setFireTicks(0).setLifestealPercent(0.25F);
    }

    private float getDamage(int spellLevel, LivingEntity caster)
    {
        return ((getSpellPower(spellLevel, caster) * 1.3F) + getWeaponDamage(caster));
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
}
