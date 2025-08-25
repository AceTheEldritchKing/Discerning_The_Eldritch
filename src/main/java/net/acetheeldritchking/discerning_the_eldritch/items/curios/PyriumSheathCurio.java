package net.acetheeldritchking.discerning_the_eldritch.items.curios;

import io.redspace.ironsspellbooks.render.CinderousRarity;
import net.acetheeldritchking.aces_spell_utils.items.curios.SheathCurioItem;
import net.acetheeldritchking.discerning_the_eldritch.registries.ItemRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber
public class PyriumSheathCurio extends SheathCurioItem {
    public static final int COOLDOWN = 5 * 20;

    public PyriumSheathCurio() {
        super(new Properties().stacksTo(1).rarity(CinderousRarity.CINDEROUS_RARITY_PROXY.getValue()).fireResistant(), null);
    }

    @Override
    protected int getCooldownTicks() {
        return COOLDOWN;
    }

    @SubscribeEvent
    public static void handleAbility(LivingIncomingDamageEvent event)
    {
        var sheath = ((PyriumSheathCurio) ItemRegistries.PYRIUM_SHEATH.get());
        Entity attacker = event.getSource().getEntity();

        if (attacker instanceof ServerPlayer player)
        {
            if (sheath.isEquippedBy(player))
            {
                if (sheath.tryProcCooldown(player))
                {
                    var victim = event.getEntity();

                    victim.setRemainingFireTicks(6*20);
                    float getBaseDamage = event.getOriginalAmount();

                    if (victim.isOnFire())
                    {
                        event.setAmount(getBaseDamage * 1.5F);
                    }
                }
            }
        }
    }
}
