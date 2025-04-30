package net.acetheeldritchking.discerning_the_eldritch.entity.render.armor;

import mod.azure.azurelib.rewrite.model.AzBakedModel;
import mod.azure.azurelib.rewrite.model.AzBone;
import mod.azure.azurelib.rewrite.render.armor.bone.AzDefaultArmorBoneProvider;
import org.jetbrains.annotations.Nullable;

public class DTEBoneProvider extends AzDefaultArmorBoneProvider {
    String armorLeggingTorsoLayer = "armorLeggingTorsoLayer";

    @Override
    public @Nullable AzBone getBodyBone(AzBakedModel model) {
        return model.getBone(armorLeggingTorsoLayer).orElse(null);
    }
}
