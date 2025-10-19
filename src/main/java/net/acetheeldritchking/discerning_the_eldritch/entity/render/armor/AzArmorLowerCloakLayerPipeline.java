package net.acetheeldritchking.discerning_the_eldritch.entity.render.armor;

import mod.azure.azurelib.common.model.AzBakedModel;
import mod.azure.azurelib.common.model.AzBone;
import mod.azure.azurelib.common.render.AzRendererPipeline;
import mod.azure.azurelib.common.render.armor.AzArmorRendererPipelineContext;
import mod.azure.azurelib.common.render.armor.bone.AzArmorBoneContext;
import mod.azure.azurelib.common.render.armor.bone.AzArmorBoneProvider;
import mod.azure.azurelib.common.util.client.RenderUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class AzArmorLowerCloakLayerPipeline extends AzArmorRendererPipelineContext {
    public AzArmorLowerCloakLayerPipeline(AzRendererPipeline<UUID, ItemStack> rendererPipeline) {
        super(rendererPipeline);
    }

    @Override
    public AzArmorBoneContext boneContext() {
        return new AzArmorBoneContext(){
            protected AzBone armorLowerCloakLeftBone;
            protected AzBone armorLowerCloakRightBone;
            protected AzBone armorLeggingTorsoBone;

            public AzBone getArmorLeggingTorsoBone(AzBakedModel model) {
                return model.getBone("armorLeggingTorsoLayer").orElse(null);
            }

            public AzBone getArmorLowerCloakLeftBoneBone(AzBakedModel model) {
                return model.getBone("armorLeftLowerCloakLayer").orElse(null);
            }

            public AzBone getArmorLowerCloakRightBone(AzBakedModel model) {
                return model.getBone("armorRightLowerCloakLayer").orElse(null);
            }

            @Override
            public void grabRelevantBones(AzBakedModel model, AzArmorBoneProvider boneProvider) {
                super.grabRelevantBones(model, boneProvider);
                this.armorLowerCloakLeftBone = this.getArmorLowerCloakLeftBoneBone(model);
                this.armorLowerCloakRightBone = this.getArmorLowerCloakRightBone(model);
                this.armorLeggingTorsoBone = this.getArmorLeggingTorsoBone(model);
            }

            @Override
            public void applyBoneVisibilityBySlot(EquipmentSlot currentSlot) {
                setAllVisible(false);

                // Hide the legging torso bone initially
                this.setBoneVisible(this.armorLowerCloakLeftBone, false);
                this.setBoneVisible(this.armorLowerCloakRightBone, false);
                this.setBoneVisible(this.armorLeggingTorsoBone, false);

                switch (currentSlot) {
                    case HEAD -> setBoneVisible(this.head, true);
                    case CHEST -> {
                        setBoneVisible(this.body, true);
                        setBoneVisible(this.rightArm, true);
                        setBoneVisible(this.leftArm, true);
                        this.setBoneVisible(this.armorLowerCloakLeftBone, true);
                        this.setBoneVisible(this.armorLowerCloakRightBone, true);
                    }
                    case LEGS -> {
                        this.setBoneVisible(this.armorLeggingTorsoBone, true);
                        setBoneVisible(this.rightLeg, true);
                        setBoneVisible(this.leftLeg, true);
                    }
                    case FEET -> {
                        setBoneVisible(this.rightBoot, true);
                        setBoneVisible(this.leftBoot, true);
                    }
                    case MAINHAND, OFFHAND -> { /* NO-OP */ }
                }
            }

            @Override
            public void applyBaseTransformations(HumanoidModel<?> baseModel)
            {
                super.applyBaseTransformations(baseModel);
                if (this.armorLeggingTorsoBone != null)
                {
                    ModelPart modelPart = baseModel.body;
                    RenderUtils.matchModelPartRot(modelPart, this.armorLeggingTorsoBone);
                    this.armorLeggingTorsoBone.updatePosition(modelPart.x, -modelPart.y, modelPart.z);
                }

                if (this.armorLowerCloakLeftBone != null)
                {
                    ModelPart modelPart = baseModel.leftLeg;
                    RenderUtils.matchModelPartRot(modelPart, this.armorLowerCloakLeftBone);
                    this.armorLowerCloakLeftBone.updatePosition(0, 0, modelPart.z);
                }

                if (this.armorLowerCloakRightBone != null)
                {
                    ModelPart modelPart = baseModel.rightLeg;
                    RenderUtils.matchModelPartRot(modelPart, this.armorLowerCloakRightBone);
                    this.armorLowerCloakRightBone.updatePosition(0, 0, modelPart.z);
                }
            }
        };
    }
}
