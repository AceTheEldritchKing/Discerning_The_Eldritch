package net.acetheeldritchking.discerning_the_eldritch.items.curios;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.spells.ExtendedEvokerFang;
import io.redspace.ironsspellbooks.entity.spells.ice_tomb.IceTombEntity;
import net.acetheeldritchking.aces_spell_utils.items.curios.SheathCurioItem;
import net.acetheeldritchking.aces_spell_utils.utils.ASRarities;
import net.acetheeldritchking.discerning_the_eldritch.entity.spells.razor_blade.RazorBlade;
import net.acetheeldritchking.discerning_the_eldritch.registries.ItemRegistries;
import net.acetheeldritchking.discerning_the_eldritch.utils.DTEConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import top.theillusivec4.curios.api.SlotContext;

@EventBusSubscriber
public class RazorSheathCurio extends SheathCurioItem {
    public static int COOLDOWN = DTEConfig.razorSheathCooldown * 20;

    public RazorSheathCurio() {
        super(new Properties().stacksTo(1).rarity(ASRarities.ACCURSED_RARITY_PROXY.getValue()).fireResistant(), null);
    }

    @Override
    protected int getCooldownTicks() {
        return DTEConfig.razorSheathCooldown * 20;
    }

    @SubscribeEvent
    public static void handleAbility(LivingDamageEvent.Pre event)
    {
        var sheath = ((RazorSheathCurio) ItemRegistries.RAZOR_SHEATH.get());
        Entity attacker = event.getSource().getDirectEntity();

        if (attacker instanceof ServerPlayer player)
        {
            if (sheath.isEquippedBy(player))
            {
                if (sheath.tryProcCooldown(player))
                {
                    var victim = event.getEntity();

                    Vec3 victimPos = victim.getEyePosition();
                    int count = 7;
                    int rings = 2;

                    for (int r = 0; r < rings; r++)
                    {
                        float fangs = count + r * r;
                        for (int i = 0; i < fangs; i++)
                        {
                            Vec3 spawn = victimPos.add(new Vec3(0, 0, 0.5 * (r + 1)).yRot(victim.getYRot() * Mth.DEG_TO_RAD + ((6.281f / fangs) * i)));
                            spawn = Utils.moveToRelativeGroundLevel(player.level(), spawn, 2);
                            if (!player.level().getBlockState(BlockPos.containing(spawn).below()).isAir()) {
                                RazorBlade razorBlade = new RazorBlade(player.level(), player, (float) (2 + player.getAttributeValue(Attributes.ATTACK_DAMAGE)));
                                razorBlade.moveTo(spawn);
                                razorBlade.setOwner(player);
                                player.level().addFreshEntity(razorBlade);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        Multimap<Holder<Attribute>, AttributeModifier> attr = LinkedHashMultimap.create();
        attr.put(AttributeRegistry.BLOOD_SPELL_POWER, new AttributeModifier(id, 0.10, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        attr.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(id, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        attr.put(Attributes.ATTACK_SPEED, new AttributeModifier(id, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));

        return attr;
    }
}
