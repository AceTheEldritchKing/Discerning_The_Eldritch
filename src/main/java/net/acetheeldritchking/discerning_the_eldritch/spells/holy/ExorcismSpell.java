package net.acetheeldritchking.discerning_the_eldritch.spells.holy;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.Optional;

import static net.acetheeldritchking.discerning_the_eldritch.registries.DTEAttachmentRegistry.INSANITY_METER;

@AutoSpellConfig
public class ExorcismSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "exorcism");

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.EPIC)
            .setSchoolResource(SchoolRegistry.HOLY_RESOURCE)
            .setMaxLevel(1)
            .setCooldownSeconds(25)
            .build();

    public ExorcismSpell()
    {
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 1;
        this.castTime = 10;
        this.baseManaCost = 75;
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
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundRegistry.CLEANSE_CAST.get());
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.CAST_KNEELING_PRAYER;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (entity.hasData(INSANITY_METER))
        {
            if (entity.getData(INSANITY_METER) <= 0)
            {
                if (entity instanceof ServerPlayer player)
                {
                    player.connection.send(new ClientboundSetActionBarTextPacket(Component.translatable("display.discerning_the_eldritch.exorcism_no_insanity")
                            .withStyle(s -> s.withColor(TextColor.fromRgb(0xF35F5F)))));
                }
            }
            else
            {
                entity.setData(INSANITY_METER, 0);
            }
        }
        else
        {
            if (entity instanceof ServerPlayer player)
            {
                player.connection.send(new ClientboundSetActionBarTextPacket(Component.translatable("display.discerning_the_eldritch.exorcism_no_insanity")
                        .withStyle(s -> s.withColor(TextColor.fromRgb(0xF35F5F)))));
            }
        }

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
}
