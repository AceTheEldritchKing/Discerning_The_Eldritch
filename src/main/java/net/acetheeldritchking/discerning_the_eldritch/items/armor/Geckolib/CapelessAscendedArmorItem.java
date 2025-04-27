package net.acetheeldritchking.discerning_the_eldritch.items.armor.Geckolib;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.entity.armor.GenericCustomArmorRenderer;
import io.redspace.ironsspellbooks.item.armor.IArmorCapeProvider;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.entity.armor.Geckolib.AscendedArmorModel;
import net.acetheeldritchking.discerning_the_eldritch.entity.armor.Geckolib.CapelessAscendedArmorModel;
import net.acetheeldritchking.discerning_the_eldritch.items.armor.DTEArmorMaterialRegistry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class CapelessAscendedArmorItem extends ImbuableGeckolibDTEArmorItem {
    public CapelessAscendedArmorItem(Type type, Properties properties) {
        super(DTEArmorMaterialRegistry.ELDRITCH_WARLOCK, type, properties, schoolAttributesWithResistance(AttributeRegistry.ELDRITCH_SPELL_POWER, AttributeRegistry.MANA_REGEN, 150, 0.10F, 0.05F, 0.05F));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GeoArmorRenderer<?> supplyRenderer() {
        return new GenericCustomArmorRenderer<>(new CapelessAscendedArmorModel());
    }
}
