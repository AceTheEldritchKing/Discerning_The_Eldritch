package net.acetheeldritchking.discerning_the_eldritch.registries;

import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;

public class DTEDamageTypes {
    public static ResourceKey<DamageType> register(String name)
    {
        return ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.parse(ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, name).toString()));
    }

    public static final ResourceKey<DamageType> RITUAL_MAGIC = register("ritual_magic");

    public static void bootstrap(BootstrapContext<DamageType> context)
    {
        context.register(RITUAL_MAGIC, new DamageType(RITUAL_MAGIC.location().getPath(), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0F));
    }
}
