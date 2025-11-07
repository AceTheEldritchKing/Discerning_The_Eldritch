package net.acetheeldritchking.discerning_the_eldritch.items.armor.Geckolib;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.armor.IArmorCapeProvider;
import net.acetheeldritchking.aces_spell_utils.entity.render.armor.EmissiveGenericCustomArmorRenderer;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.entity.armor.Geckolib.AscendedArmorModel;
import net.acetheeldritchking.discerning_the_eldritch.items.armor.DTEArmorMaterialRegistry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class AscendedArmorItem extends ImbuableGeckolibDTEArmorItem implements IArmorCapeProvider {
    public AscendedArmorItem(Type type, Properties properties) {
        super(DTEArmorMaterialRegistry.ELDRITCH_WARLOCK, type, properties, schoolAttributesWithResistance(AttributeRegistry.ELDRITCH_SPELL_POWER, AttributeRegistry.MANA_REGEN, 150, 0.10F, 0.05F, 0.05F));
    }

    @Override
    public ResourceLocation getCapeResourceLocation() {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "textures/models/armor/geckolib/ascended_armor_cape.png");
    }

    private static final ResourceLocation LAYER = ResourceLocation.fromNamespaceAndPath(
            DiscerningTheEldritch.MOD_ID,
            "textures/models/armor/geckolib/ascended_armor_glowmask.png");

    @Override
    @OnlyIn(Dist.CLIENT)
    public GeoArmorRenderer<?> supplyRenderer() {
        return new EmissiveGenericCustomArmorRenderer<>(new AscendedArmorModel(), LAYER);
    }
}
