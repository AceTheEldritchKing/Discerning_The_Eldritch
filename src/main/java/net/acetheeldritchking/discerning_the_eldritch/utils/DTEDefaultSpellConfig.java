package net.acetheeldritchking.discerning_the_eldritch.utils;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import net.acetheeldritchking.discerning_the_eldritch.spells.DTESpellRarities;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public class DTEDefaultSpellConfig extends DefaultConfig {
    public DTESpellRarities minRarity;
    public ResourceLocation schoolResource;
    public int maxLevel = -1;
    public boolean enabled = true;
    public double cooldownInSeconds = -1;
    public boolean allowCrafting = true;

    public DTEDefaultSpellConfig(Consumer<DefaultConfig> initialize) throws RuntimeException
    {
        initialize.accept(this);
        build();
    }

    public DTEDefaultSpellConfig() {}

    public DefaultConfig setMaxLevel(int i) {
        this.maxLevel = i;
        return this;
    }

    public DefaultConfig setDeprecated(boolean deprecated) {
        this.enabled = !deprecated;
        return this;
    }

    public DefaultConfig setMinRarity(DTESpellRarities i) {
        this.minRarity = i;
        return this;
    }

    public DefaultConfig setCooldownSeconds(double i) {
        this.cooldownInSeconds = i;
        return this;
    }

    public DefaultConfig setSchoolResource(ResourceLocation schoolResource) {
        this.schoolResource = schoolResource;
        return this;
    }

    public DefaultConfig setAllowCrafting(boolean allowCrafting) {
        this.allowCrafting = allowCrafting;
        return this;
    }

    public DefaultConfig build() throws RuntimeException {
        if (!this.validate())
            throw new RuntimeException("You didn't define all config attributes!");

        return this;
    }

    private boolean validate() {
        return minRarity != null && maxLevel >= 0 && schoolResource != null && cooldownInSeconds >= 0;
    }
}
