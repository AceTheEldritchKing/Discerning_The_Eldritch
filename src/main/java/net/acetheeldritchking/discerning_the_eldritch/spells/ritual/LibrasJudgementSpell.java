package net.acetheeldritchking.discerning_the_eldritch.spells.ritual;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AutoSpellConfig;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.entity.spells.mourning_star.MourningStarProjectile;
import net.acetheeldritchking.discerning_the_eldritch.entity.spells.ritual_burn.RitualBurnAoE;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEPotionEffectRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTESchoolRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@AutoSpellConfig
public class LibrasJudgementSpell extends AbstractRitualSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "libras_judgement");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.damage", getDamageText(spellLevel, caster)),
                Component.translatable("ui.discerning_the_eldritch.libras_chance_1"),
                Component.translatable("ui.discerning_the_eldritch.libras_chance_2")
        );
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(DTESchoolRegistry.RITUAL_RESOURCE)
            .setMaxLevel(1)
            .setCooldownSeconds(45)
            .build();

    public LibrasJudgementSpell()
    {
        this.manaCostPerLevel = 15;
        this.baseSpellPower = 35;
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
    public boolean isSuperComplex() {
        return false;
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        return Utils.preCastTargetHelper(level, entity, playerMagicData, this, 32, .15f);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        //
        if (playerMagicData.getAdditionalCastData() instanceof TargetEntityCastData targetEntityCastData)
        {
            var targetEntity = targetEntityCastData.getTarget((ServerLevel) level);
            if (targetEntity != null)
            {
                double targetEye = targetEntity.getEyeHeight();

                // Mourning star or burn
                final float MAX_HEALTH = targetEntity.getMaxHealth();
                float baseHealth = targetEntity.getHealth();
                double percent = (baseHealth/MAX_HEALTH) * 100;

                if (percent >= 50)
                {
                    float damage = getDamage(spellLevel, entity);

                    Vec3 center = targetEntity.position().add(0, targetEye / 2, 0);
                    Vec3 spawn = center.add(0, 10, 0);
                    Vec3 motion = center.subtract(spawn).normalize();

                    MourningStarProjectile mourningStarProjectile = new MourningStarProjectile(level, entity);

                    mourningStarProjectile.moveTo(spawn);
                    mourningStarProjectile.shoot(motion);
                    mourningStarProjectile.setDamage(damage);

                    level.addFreshEntity(mourningStarProjectile);

                    if (entity instanceof ServerPlayer serverPlayer)
                    {
                        serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(Component.translatable("display.discerning_the_eldritch.libras_judgement_1")
                                .withStyle(s -> s.withColor(TextColor.fromRgb(0xF35F5F)))));
                    }
                } else if (percent < 50)
                {
                    // Curse of Ra 2.0
                    if (targetEntity instanceof LivingEntity livingTarget)
                    {
                        livingTarget.addEffect(new MobEffectInstance(DTEPotionEffectRegistry.ACCURSED_EFFECT, 15*20, 0, true, true, true));
                    }

                    RitualBurnAoE aoE = new RitualBurnAoE(level);
                    aoE.setOwner(entity);
                    aoE.setDuration(100);
                    aoE.setDamage(1.5F);
                    aoE.setRadius(3.0F);
                    aoE.setCircular();
                    aoE.moveTo(targetEntity.position());
                    level.addFreshEntity(aoE);

                    if (entity instanceof ServerPlayer serverPlayer)
                    {
                        serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(Component.translatable("display.discerning_the_eldritch.libras_judgement_2")
                                .withStyle(s -> s.withColor(TextColor.fromRgb(0xF35F5F)))));
                    }
                }
            }
        }

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private float getDamage(int spellLevel, LivingEntity caster)
    {
        return (getSpellPower(spellLevel, caster) * 1.1f) + getWeaponDamage(caster);
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
    public SpellDamageSource getDamageSource(@Nullable Entity projectile, Entity attacker) {
        return super.getDamageSource(projectile, attacker).setIFrames(0);
    }
}
