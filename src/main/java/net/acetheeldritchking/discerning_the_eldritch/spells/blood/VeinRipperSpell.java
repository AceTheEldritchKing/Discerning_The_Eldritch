package net.acetheeldritchking.discerning_the_eldritch.spells.blood;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.particle.TraceParticleOptions;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.particle.DTEParticleHelper;
import net.acetheeldritchking.discerning_the_eldritch.particle.VeinRipperVerticalParticleOptions;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEPotionEffectRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTESoundRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class VeinRipperSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "vein_ripper");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.damage", getDamageText(spellLevel, caster))
        );
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.COMMON)
            .setSchoolResource(SchoolRegistry.BLOOD_RESOURCE)
            .setMaxLevel(5)
            .setCooldownSeconds(20)
            .build();

    public VeinRipperSpell()
    {
        this.manaCostPerLevel = 15;
        this.baseSpellPower = 6;
        this.spellPowerPerLevel = 2;
        this.castTime = 0;
        this.baseManaCost = 30;
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
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(DTESoundRegistry.MAGIC_SWORD_BLOOD_CAST.get());
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.ONE_HANDED_VERTICAL_UPSWING_ANIMATION;
    }

    @Override
    public void onClientCast(Level level, int spellLevel, LivingEntity entity, ICastData castData) {
        super.onClientCast(level, spellLevel, entity, castData);

        entity.setYBodyRot(entity.getYRot());
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        float distance = 7 + spellLevel;
        Vec3 forward = entity.getForward();
        Vec3 end = Utils.raycastForBlock(level, entity.getEyePosition(), entity.getEyePosition().add(forward.scale(distance)), ClipContext.Fluid.NONE).getLocation();
        AABB hit = entity.getHitbox().expandTowards(end.subtract(entity.getEyePosition())).inflate(2);

        var targetedEntities = level.getEntities(entity, hit, e ->
                !e.isSpectator() && e instanceof LivingEntity && e.getBoundingBox().getCenter().subtract(entity.getBoundingBox().getCenter()).normalize().dot(entity.getForward()) >= .85);
        targetedEntities.sort(Comparator.comparingDouble(e -> e.distanceToSqr(entity)));

        if (!targetedEntities.isEmpty() && targetedEntities.getFirst().distanceToSqr(entity) < distance * distance)
        {
            var closest = targetedEntities.getFirst();

            float radius = 4.5F;
            AABB aoe = AABB.ofSize(closest.getBoundingBox().getCenter(), radius, radius  + 1, radius).move(forward.scale(radius / 2));
            end = aoe.getCenter().add(end).scale(0.5);
            var damagedEntities = level.getEntities(entity, aoe);
            var damageSource = this.getDamageSource(entity);

            for (Entity target : damagedEntities)
            {
                if (target.isAlive() && target.isPickable() && Utils.hasLineOfSight(level, entity.getEyePosition(), target.getBoundingBox().getCenter(), true))
                {
                    if (DamageSources.applyDamage(target, getDamage(spellLevel, entity), damageSource))
                    {
                        MagicManager.spawnParticles(level, ParticleHelper.BLOOD, target.getX(), target.getY() + target.getBbHeight() * .5f, target.getZ(), 15, target.getBbWidth() * .5f, target.getBbHeight() * .5f, target.getBbWidth() * .5f, .25, false);
                        EnchantmentHelper.doPostAttackEffects((ServerLevel) level, target, damageSource);

                        Vec3 kb = target.position().subtract(entity.position()).normalize().add(0, 0.15, 0).normalize();
                        kb.scale(Utils.random.nextIntBetweenInclusive(70, 100) / 100F * Utils.clampedKnockbackResistanceFactor(target, 0.2F, 1F) * 0.1F);
                        target.setDeltaMovement(target.getDeltaMovement().add(kb));

                        target.hurtMarked = true;
                        if (target instanceof LivingEntity livingTarget)
                        {
                            livingTarget.addEffect(new MobEffectInstance(DTEPotionEffectRegistry.BLOOD_ROT_EFFECT, 3 * 20, 0, true, true, true));
                        }
                    }
                }
            }
        }

        Vec3 rayVec = end.subtract(entity.getEyePosition());
        Vec3 impulse = rayVec.scale(1 / 6f).add(0, 0.1, 0);

        entity.setDeltaMovement(entity.getDeltaMovement().scale(0.1).add(impulse));
        entity.hurtMarked = true;
        entity.addEffect(new MobEffectInstance(MobEffectRegistry.FALL_DAMAGE_IMMUNITY, 20, 0, false, false, true));

        forward = impulse.normalize();
        Vec3 up = new Vec3(0, 1, 0);
        if (forward.dot(up) > .999) {
            up = new Vec3(1, 0, 0);
        }
        Vec3 right = up.cross(forward);
        Vec3 particlePos = end.subtract(forward.scale(3)).add(right.scale(-0.3));

        MagicManager.spawnParticles(level,
                new VeinRipperVerticalParticleOptions(
                        (float) forward.x,
                        (float) forward.y,
                        (float) forward.z,
                        (float) right.x,
                        (float) right.y,
                        (float) right.z,
                        1f),
                particlePos.x, particlePos.y + .3, particlePos.z, 1, 0, 0, 0, 0, true);
        int trailParticles = 15;
        double speed = rayVec.length() / 12 * .75;
        for (int i = 0; i < trailParticles; i++) {
            Vec3 particleStart = entity.getBoundingBox().getCenter().add(Utils.getRandomVec3(1 + entity.getBbWidth()));
            Vec3 particleEnd = particleStart.add(rayVec);
            MagicManager.spawnParticles(level, new TraceParticleOptions(Utils.v3f(particleEnd), new Vector3f(0.941F,0.169F,0.231F)), particleStart.x, particleStart.y, particleStart.z, 1, 0, 0, 0, speed, false);
        }

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    @Override
    public SpellDamageSource getDamageSource(Entity projectile, Entity attacker) {
        return super.getDamageSource(projectile, attacker).setLifestealPercent(0.15F);
    }

    private float getDamage(int spellLevel, LivingEntity entity) {
        return getSpellPower(spellLevel, entity) + Utils.getWeaponDamage(entity);
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
