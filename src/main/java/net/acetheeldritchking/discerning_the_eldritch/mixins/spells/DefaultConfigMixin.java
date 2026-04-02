package net.acetheeldritchking.discerning_the_eldritch.mixins.spells;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import net.acetheeldritchking.discerning_the_eldritch.spells.DTESpellRarities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(DefaultConfig.class)
public class DefaultConfigMixin {
    @Unique
    public DTESpellRarities discerningTheEldritch$minDTERarity;

    @Unique
    public DefaultConfig discerningTheEldritch$setMinRarity(DTESpellRarities i) {
        this.discerningTheEldritch$minDTERarity = i;
        return (DefaultConfig) (Object) this;
    }
}
