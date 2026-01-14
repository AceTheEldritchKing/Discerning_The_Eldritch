package net.acetheeldritchking.discerning_the_eldritch.entity.render.armor;

import mod.azure.azurelib.common.cache.texture.AzAbstractTexture;
import mod.azure.azurelib.common.model.AzBone;
import mod.azure.azurelib.common.render.AzRendererPipelineContext;
import mod.azure.azurelib.common.render.layer.AzRenderLayer;
import mod.azure.azurelib.common.util.client.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.opengl.GL;

import java.util.UUID;

public class CustomAZEmissiveLayer<K, T> implements AzRenderLayer<K, T> {
    public final ResourceLocation GLOW_LAYER;

    public CustomAZEmissiveLayer(ResourceLocation glowLayer)
    {
        this.GLOW_LAYER = glowLayer;
    }

    @Override
    public void preRender(AzRendererPipelineContext<K, T> context) {

    }

    @Override
    public void render(AzRendererPipelineContext<K, T> context) {
        var renderPipeline = context.rendererPipeline();
        var renderType = determineRenderType(context);
        RenderType glowy = RenderType.breezeEyes(GLOW_LAYER);

        if (renderType != null) {
            context.setRenderType(glowy);
            context.setPackedLight(getPackedLight(context));
            context.setVertexConsumer(context.multiBufferSource().getBuffer(renderType));

            renderPipeline.reRender(context);
        }

        /*var renderPipeline = context.rendererPipeline();

        if (context.renderType() != null)
        {
            RenderType glowy = RenderType.breezeEyes(GLOW_LAYER);

            context.setPackedLight(LightTexture.FULL_BRIGHT);
            context.setRenderType(glowy);
            context.setVertexConsumer(context.multiBufferSource().getBuffer(glowy));

            renderPipeline.reRender(context);
        }*/
    }

    protected int getPackedLight(AzRendererPipelineContext<K, T> context) {
        return LightTexture.FULL_SKY;
    }

    @Override
    public void renderForBone(AzRendererPipelineContext<K, T> context, AzBone bone) {

    }

    protected RenderType determineRenderType(AzRendererPipelineContext<K, T> context) {
        var animatable = context.animatable();
        var config = context.rendererPipeline().config();
        var textureLocation = config.textureLocation(context.currentEntity(), animatable);

        if (!(animatable instanceof Entity entity)) {
            return AzAbstractTexture.getRenderType(textureLocation);
        }

        var isInvisible = entity.isInvisible();
        var appearsGlowing = Minecraft.getInstance().shouldEntityAppearGlowing(entity);
        var isPlayerInvisible = entity.isInvisibleTo(ClientUtils.getClientPlayer());

        if (isInvisible) {
            if (!isPlayerInvisible) {
                return RenderType.itemEntityTranslucentCull(AzAbstractTexture.getEmissiveResource(textureLocation));
            }
            if (appearsGlowing) {
                return RenderType.outline(AzAbstractTexture.getEmissiveResource(textureLocation));
            }
            return null;
        }

        if (appearsGlowing) {
            return AzAbstractTexture.getOutlineRenderType(textureLocation);
        }

        return AzAbstractTexture.getRenderType(textureLocation);
    }
}
