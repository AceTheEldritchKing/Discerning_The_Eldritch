package net.acetheeldritchking.discerning_the_eldritch.entity.spells.esoteric_edge;

import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class EsotericEdgeRenderer extends EntityRenderer<EsotericEdge> {
    private static final ResourceLocation[] TEXTURES = {
            DiscerningTheEldritch.id("textures/entity/esoteric_edge/esoteric_edge_1.png"),
            DiscerningTheEldritch.id("textures/entity/esoteric_edge/esoteric_edge_2.png"),
            DiscerningTheEldritch.id("textures/entity/esoteric_edge/esoteric_edge_3.png"),
            DiscerningTheEldritch.id("textures/entity/esoteric_edge/esoteric_edge_4.png")
    };

    public EsotericEdgeRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(EsotericEdge esotericEdge) {
        int frame = (esotericEdge.tickCount / 4) % TEXTURES.length;
        return TEXTURES[frame];
    }
}
