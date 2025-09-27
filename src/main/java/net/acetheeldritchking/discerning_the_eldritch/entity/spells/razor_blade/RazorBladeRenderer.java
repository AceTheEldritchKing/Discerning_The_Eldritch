package net.acetheeldritchking.discerning_the_eldritch.entity.spells.razor_blade;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RazorBladeRenderer extends GeoEntityRenderer<RazorBlade> {
    public RazorBladeRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new RazorBladeModel());
    }
}
