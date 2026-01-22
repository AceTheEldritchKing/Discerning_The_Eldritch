package net.acetheeldritchking.discerning_the_eldritch.items.curios;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.acetheeldritchking.aces_spell_utils.items.curios.SheathCurioItem;
import net.acetheeldritchking.aces_spell_utils.registries.ASAttributeRegistry;
import net.acetheeldritchking.aces_spell_utils.utils.ASRarities;
import net.acetheeldritchking.discerning_the_eldritch.entity.spells.stardust_aoe.StardustAoE;
import net.acetheeldritchking.discerning_the_eldritch.registries.ItemRegistries;
import net.acetheeldritchking.discerning_the_eldritch.utils.DTEServerConfig;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import top.theillusivec4.curios.api.SlotContext;

@EventBusSubscriber
public class StarstruckSheathCurio extends SheathCurioItem {
    public StarstruckSheathCurio() {
        super(new Properties().stacksTo(1).rarity(ASRarities.ARID_RARITY_PROXY.getValue()).fireResistant(), null);
    }

    @Override
    protected int getCooldownTicks() {
        return DTEServerConfig.pyriumSheathCooldown * 20;
    }

    @SubscribeEvent
    public static void handleAbility(LivingIncomingDamageEvent event)
    {
        var sheath = ((StarstruckSheathCurio) ItemRegistries.STARSTRUCK_SHEATH.get());
        Entity attacker = event.getSource().getEntity();

        if (attacker instanceof ServerPlayer player)
        {
            if (sheath.isEquippedBy(player))
            {
                if (sheath.tryProcCooldown(player))
                {
                    var victim = event.getEntity();

                    if (victim != attacker)
                    {
                        // Fuck your i-frames, LOSER!!!
                        /*victim.invulnerableTime = 0;
                        Comet comet = new Comet(player.level(), player);
                        comet.setDamage(5);
                        comet.setPos(victim.getX(), victim.getY() + 7, victim.getZ());
                        var trajectory = new Vec3(0.05F, -0.85F, 0).normalize();
                        comet.shoot(trajectory, 0.045F);
                        comet.setExplosionRadius(10.5F);

                        player.level().addFreshEntity(comet);*/

                        StardustAoE aoe = new StardustAoE(attacker.level());
                        aoe.setOwner(attacker);
                        aoe.moveTo(victim.position());
                        aoe.setRadius(5.5F);
                        aoe.setCircular();
                        aoe.setDuration(5*20);

                        attacker.level().addFreshEntity(aoe);
                    }
                }
            }
        }
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        Multimap<Holder<Attribute>, AttributeModifier> attr = LinkedHashMultimap.create();
        attr.put(AttributeRegistry.ENDER_SPELL_POWER, new AttributeModifier(id, 0.10, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        attr.put(AttributeRegistry.ENDER_MAGIC_RESIST, new AttributeModifier(id, 0.10, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        attr.put(ASAttributeRegistry.MAGIC_PROJECTILE_DAMAGE, new AttributeModifier(id, 0.10, AttributeModifier.Operation.ADD_VALUE));

        return attr;
    }
}
