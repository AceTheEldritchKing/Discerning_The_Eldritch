package net.acetheeldritchking.discerning_the_eldritch.mixins.entity;
import io.redspace.ironsspellbooks.entity.mobs.wizards.cultist.CultistEntity;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.apothic_cultists.ApothicAcolyteEntity;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.apothic_cultists.ApothicCrusaderEntity;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.apothic_cultists.ApothicSummonerEntity;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.bosses.ascended_one.AscendedOneBoss;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CultistEntity.class)
public class CultistEntityMixin {
    @Inject(method = "registerGoals", at = @At("HEAD"))
    void iHateApothicCultists(CallbackInfo ci)
    {
        CultistEntity self = (CultistEntity) (Object) this;

        self.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(self, ApothicCrusaderEntity.class, true));
        self.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(self, ApothicAcolyteEntity.class, true));
        self.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(self, ApothicSummonerEntity.class, true));
        self.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(self, AscendedOneBoss.class, true));
    }
}
