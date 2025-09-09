package net.acetheeldritchking.discerning_the_eldritch.spells.eldritch;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.spells.ender.TeleportSpell;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEPotionEffectRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

@AutoSpellConfig
public class OtherworldlyPresenceSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "otherworldly_presence");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.distance", Utils.stringTruncation(getDistance(spellLevel, caster), 1)));
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(SchoolRegistry.ELDRITCH_RESOURCE)
            .setMaxLevel(3)
            .setCooldownSeconds(10)
            .build();

    public OtherworldlyPresenceSpell()
    {
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 12;
        this.spellPowerPerLevel = 4;
        this.castTime = 0;
        this.baseManaCost = 40;
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
        Vec3 destination = null;
        var teleportData = (TeleportSpell.TeleportData) playerMagicData.getAdditionalCastData();

        if (teleportData != null)
        {
            var potentialTarget = teleportData.getTeleportTargetPosition();
            if (potentialTarget != null)
            {
                destination = potentialTarget;
                Utils.handleSpellTeleport(this, entity, destination);
            }
        }

        if (destination == null)
        {
            destination = TeleportSpell.findTeleportLocation(level, entity, getDistance(spellLevel, entity));
            Utils.handleSpellTeleport(this, entity, destination);
        }

        if (entity.isPassenger())
        {
            entity.stopRiding();
        }

        entity.resetFallDistance();
        entity.addEffect(new MobEffectInstance(DTEPotionEffectRegistry.METAPHYSICAL_POTION_EFFECT, 20*10, 0, false, false, true));

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private float getDistance(int spellLevel, LivingEntity entity)
    {
        return (float) (Utils.softCapFormula(getEntityPowerMultiplier(entity)) * getSpellPower(spellLevel, null));
    }
}
