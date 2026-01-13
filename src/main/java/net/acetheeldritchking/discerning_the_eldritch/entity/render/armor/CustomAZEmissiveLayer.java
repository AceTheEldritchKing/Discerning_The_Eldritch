package net.acetheeldritchking.discerning_the_eldritch.entity.render.armor;

import mod.azure.azurelib.common.model.AzBone;
import mod.azure.azurelib.common.render.AzRendererPipelineContext;
import mod.azure.azurelib.common.render.layer.AzRenderLayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.opengl.GL;

import java.util.UUID;

public class CustomAZEmissiveLayer implements AzRenderLayer<UUID, ItemStack> {
    public final ResourceLocation GLOW_LAYER;

    public CustomAZEmissiveLayer(ResourceLocation glowLayer)
    {
        this.GLOW_LAYER = glowLayer;
    }

    @Override
    public void preRender(AzRendererPipelineContext<UUID, ItemStack> context) {

    }

    @Override
    public void render(AzRendererPipelineContext<UUID, ItemStack> context) {
        var renderPipeline = context.rendererPipeline();

        if (context.renderType() != null)
        {
            RenderType glowy = RenderType.breezeEyes(GLOW_LAYER);

            context.setPackedLight(LightTexture.FULL_BRIGHT);
            context.setRenderType(glowy);
            context.setVertexConsumer(context.multiBufferSource().getBuffer(glowy));

            renderPipeline.reRender(context);
        }
    }

    @Override
    public void renderForBone(AzRendererPipelineContext<UUID, ItemStack> context, AzBone bone) {

    }
}
