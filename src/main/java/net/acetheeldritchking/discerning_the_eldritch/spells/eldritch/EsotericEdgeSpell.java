package net.acetheeldritchking.discerning_the_eldritch.spells.eldritch;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.damage.DamageSources;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.entity.spells.esoteric_edge.EsotericEdge;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

@AutoSpellConfig
public class EsotericEdgeSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "esoteric_edge");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.damage", getDamageText(spellLevel, caster))
        );
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(SchoolRegistry.ELDRITCH_RESOURCE)
            .setMaxLevel(10)
            .setCooldownSeconds(20)
            .build();

    public EsotericEdgeSpell()
    {
        this.manaCostPerLevel = 15;
        this.baseSpellPower = 10;
        this.spellPowerPerLevel = 5;
        this.castTime = 0;
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
        return CastType.INSTANT;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        EsotericEdge esotericEdge = new EsotericEdge(level, entity);
        esotericEdge.setPos(entity.getEyePosition());
        esotericEdge.shootFromRotation(entity, entity.getXRot(), entity.getYHeadRot(), 0, 1, 1);

        esotericEdge.setDamage(getDamage(spellLevel, entity));

        /*float radius = 2.0f;
        float distance = 2.0f;
        Vec3 esotericEdgeSlashLocation = esotericEdge.position().add(esotericEdge.getForward().multiply(distance, 0.3f, distance));
        var entities = level.getEntities(entity,
                AABB.ofSize(esotericEdgeSlashLocation, radius * 2, radius, radius *2));
        for (Entity targetEntity : entities)
        {
            if (entity.isPickable() && entity.distanceToSqr(targetEntity) < radius * radius &&
                    Utils.hasLineOfSight(level, entity.getEyePosition(), targetEntity.getBoundingBox().getCenter(),
                            true))
            {
                if (DamageSources.applyDamage(targetEntity, getDamage(spellLevel, entity), this.getDamageSource(esotericEdge)))
                {
                    if (targetEntity instanceof LivingEntity livingTarget)
                    {
                        if (DamageSources.applyDamage(livingTarget, getDamage(spellLevel, entity), this.getDamageSource(esotericEdge)))
                        {
                            esotericEdge.setDamage(getDamage(spellLevel, entity));
                            EnchantmentHelper.doPostAttackEffects((ServerLevel) level, targetEntity, this.getDamageSource(esotericEdge));
                        }
                    }
                }
            }
        }*/

        level.addFreshEntity(esotericEdge);

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private float getDamage(int spellLevel, LivingEntity caster)
    {
        return getSpellPower(spellLevel, caster) + Utils.getWeaponDamage(caster);
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
