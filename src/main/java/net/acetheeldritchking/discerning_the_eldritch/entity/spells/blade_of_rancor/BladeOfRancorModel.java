package net.acetheeldritchking.discerning_the_eldritch.entity.spells.blade_of_rancor;

import io.redspace.ironsspellbooks.entity.spells.fiery_dagger.FieryDaggerEntity;
import io.redspace.ironsspellbooks.render.RenderHelper;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;

public class BladeOfRancorModel extends GeoModel<BladeOfRancorProjectile> {
    @Override
    public ResourceLocation getModelResource(BladeOfRancorProjectile bladeOfRancorProjectile) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "geo/rancor_blade.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BladeOfRancorProjectile bladeOfRancorProjectile) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "textures/entity/rancor_blade/rancor_blade.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BladeOfRancorProjectile bladeOfRancorProjectile) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "animations/blade_of_rancor.animation.json");
    }

    @Override
    public @Nullable RenderType getRenderType(BladeOfRancorProjectile animatable, ResourceLocation texture) {
        return RenderHelper.CustomerRenderType.magic(ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "textures/entity/rancor_blade/rancor_blade.png"));
    }
}
