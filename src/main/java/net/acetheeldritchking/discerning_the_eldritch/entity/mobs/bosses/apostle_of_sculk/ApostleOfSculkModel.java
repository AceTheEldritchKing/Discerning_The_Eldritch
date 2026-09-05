package net.acetheeldritchking.discerning_the_eldritch.entity.mobs.bosses.apostle_of_sculk;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobModel;
import io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss.FireBossEntity;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.util.DefaultBipedBoneIdents;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.registries.ItemRegistries;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.WalkAnimationState;
import org.joml.Vector2f;
import org.joml.Vector3f;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;

public class ApostleOfSculkModel extends AbstractSpellCastingMobModel {
    @Override
    public ResourceLocation getTextureResource(AbstractSpellCastingMob mob) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "textures/entity/apostle_of_sculk/apostle_of_sculk.png");
    }

    @Override
    public ResourceLocation getAnimationResource(AbstractSpellCastingMob object) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "animations/casting_animations.json");
    }

    @Override
    public void setCustomAnimations(AbstractSpellCastingMob entity, long instanceId, AnimationState<AbstractSpellCastingMob> animationState) {
        if (Minecraft.getInstance().isPaused())
        {
            return;
        }

        if (entity instanceof ApostleOfSculkBoss apostle)
        {
            float partialTick = animationState.getPartialTick();
            Vector2f limbSwing = getLimbSwing(entity, entity.walkAnimation, partialTick);

            if (entity.isAnimating())
            {
                apostle.animDampener = Mth.lerp(.15F * partialTick, apostle.animDampener, 0);
            } else
            {
                apostle.animDampener = Mth.lerp(.05F * partialTick, apostle.animDampener, 1);
            }

            if (entity.getMainHandItem().is(ItemRegistries.STAFF_OF_THE_SPECTRE))
            {
                GeoBone rightArm = this.getAnimationProcessor().getBone(PartNames.RIGHT_ARM);
                GeoBone rightHand = this.getAnimationProcessor().getBone(DefaultBipedBoneIdents.RIGHT_HAND_BONE_IDENT);

                if (entity.walkAnimation.isMoving())
                {
                    Vector3f armPose = new Vector3f(-15, -25, 15);
                    armPose.mul(Mth.DEG_TO_RAD * apostle.animDampener);
                    transformStack.pushRotation(rightArm, armPose);
                } else
                {
                    Vector3f armPose = new Vector3f(-10, -20, 10);
                    armPose.mul(Mth.DEG_TO_RAD * apostle.animDampener);
                    transformStack.pushRotation(rightArm, armPose);
                }

                if (entity.walkAnimation.isMoving())
                {
                    Vector3f staffPos = new Vector3f(5, -20, -48);
                    staffPos.mul(Mth.DEG_TO_RAD * apostle.animDampener);
                    transformStack.pushRotation(rightHand, staffPos);
                } else
                {
                    Vector3f staffPos = new Vector3f(5, -10, -48);
                    staffPos.mul(Mth.DEG_TO_RAD * apostle.animDampener);
                    transformStack.pushRotation(rightHand, staffPos);
                }

                if (!entity.isAnimating())
                {
                    float walkDampener = (Mth.cos(limbSwing.y() * 0.6662F + (float) Math.PI) * 2.0F * limbSwing.x() * 0.5F) * -.75f;
                    transformStack.pushRotation(rightArm, walkDampener, 0, 0);
                }
            }
        }

        super.setCustomAnimations(entity, instanceId, animationState);
    }

    @Override
    protected Vector2f getLimbSwing(AbstractSpellCastingMob entity, WalkAnimationState walkAnimationState, float partialTick) {
        Vector2f limbSwing = super.getLimbSwing(entity, walkAnimationState, partialTick);
        limbSwing.mul(0.6F, 1F);
        return limbSwing;
    }
}
