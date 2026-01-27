package net.acetheeldritchking.discerning_the_eldritch.items.armor.Geckolib;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.entity.armor.GenericCustomArmorRenderer;
import net.acetheeldritchking.discerning_the_eldritch.entity.armor.Geckolib.StarvoidArmorModel;
import net.acetheeldritchking.discerning_the_eldritch.items.armor.DTEArmorMaterialRegistry;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class StarvoidArmorItem extends ImbuableGeckolibDTEArmorItem {
    public StarvoidArmorItem(Type type, Properties properties) {
        super(DTEArmorMaterialRegistry.STARVOID_ARMOR, type, properties,
                schoolAttributesWithResistance(
                        AttributeRegistry.ENDER_SPELL_POWER,
                        AttributeRegistry.ENDER_MAGIC_RESIST,
                        150,
                        0.15F,
                        0.05F,
                        0.05F
                ));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GeoArmorRenderer<?> supplyRenderer() {
        return new GenericCustomArmorRenderer<>(new StarvoidArmorModel());
    }
}
