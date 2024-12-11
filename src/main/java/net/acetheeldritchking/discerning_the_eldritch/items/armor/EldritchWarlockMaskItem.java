package net.acetheeldritchking.discerning_the_eldritch.items.armor;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import mod.azure.azurelib.common.internal.client.RenderProvider;
import net.acetheeldritchking.discerning_the_eldritch.entity.render.armor.EldritchWarlockMaskRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class EldritchWarlockMaskItem extends ImbuableDTEArmorItem {
    public EldritchWarlockMaskItem(Type slot, Properties settings) {
        super(DTEArmorMaterialRegistry.ELDRITCH_WARLOCK, slot, settings, schoolAttributesWithResistance(AttributeRegistry.ELDRITCH_SPELL_POWER, AttributeRegistry.SUMMON_DAMAGE, 150, 0.10F, 0.05F, 0.05F));
    }

    @Override
    public void createRenderer(Consumer<RenderProvider> consumer) {
        consumer.accept(new RenderProvider() {
            private EldritchWarlockMaskRenderer renderer;

            @Override
            public HumanoidModel<LivingEntity> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<LivingEntity> original) {
                if (renderer == null)
                {
                    renderer = new EldritchWarlockMaskRenderer();
                }
                renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);

                return this.renderer;
            }
        });
    }
}
