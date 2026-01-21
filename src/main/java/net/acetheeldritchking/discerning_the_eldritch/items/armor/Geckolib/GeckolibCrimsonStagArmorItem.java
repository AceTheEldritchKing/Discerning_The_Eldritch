package net.acetheeldritchking.discerning_the_eldritch.items.armor.Geckolib;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import net.acetheeldritchking.aces_spell_utils.entity.render.armor.EmissiveGenericCustomArmorRenderer;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.entity.armor.Geckolib.GeckolibCrimsonStagArmorModel;
import net.acetheeldritchking.discerning_the_eldritch.items.armor.DTEArmorMaterialRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEAttributeRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class GeckolibCrimsonStagArmorItem extends ImbuableGeckolibDTEArmorItem{
    public GeckolibCrimsonStagArmorItem(Type slot, Properties settings) {
        super(DTEArmorMaterialRegistry.CRIMSON_STAG, slot, settings,
                schoolAttributesWithResistance(
                        AttributeRegistry.BLOOD_SPELL_POWER,
                        DTEAttributeRegistry.RITUAL_MAGIC_POWER,
                        150,
                        0.15F,
                        0.05F,
                        0.05F
                ));
    }

    private static final ResourceLocation LAYER = ResourceLocation.fromNamespaceAndPath(
            DiscerningTheEldritch.MOD_ID,
            "textures/models/armor/geckolib/crimson_stag_gecko_glowmask.png");

    @Override
    @OnlyIn(Dist.CLIENT)
    public GeoArmorRenderer<?> supplyRenderer() {
        RenderType GLOW_RENDER_TYPE = RenderType.breezeEyes(LAYER);

        return new EmissiveGenericCustomArmorRenderer<>(new GeckolibCrimsonStagArmorModel(), LAYER, GLOW_RENDER_TYPE);
    }
}
