package net.acetheeldritchking.discerning_the_eldritch.items.curios;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import io.redspace.ironsspellbooks.api.events.SpellOnCastEvent;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.compat.Curios;
import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.acetheeldritchking.aces_spell_utils.items.curios.FlatCooldownPassiveAbilityCurio;
import net.acetheeldritchking.aces_spell_utils.registries.ASAttributeRegistry;
import net.acetheeldritchking.aces_spell_utils.utils.ASUtils;
import net.acetheeldritchking.discerning_the_eldritch.items.custom.CursedItem;
import net.acetheeldritchking.discerning_the_eldritch.registries.ItemRegistries;
import net.acetheeldritchking.discerning_the_eldritch.utils.DTERarities;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import top.theillusivec4.curios.api.SlotContext;

@EventBusSubscriber
public class TheThornRingCurio extends FlatCooldownPassiveAbilityCurio implements CursedItem {
    public TheThornRingCurio() {
        super(ItemPropertiesHelper.equipment().stacksTo(1).fireResistant().rarity(DTERarities.APOTHIC_RARITY_PROXY.getValue()), Curios.RING_SLOT);
    }

    @Override
    protected int getCooldownTicks() {
        return 15 * 20;
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        Multimap<Holder<Attribute>, AttributeModifier> attr = LinkedHashMultimap.create();
        attr.put(AttributeRegistry.ELDRITCH_SPELL_POWER, new AttributeModifier(id, 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        attr.put(AttributeRegistry.ICE_SPELL_POWER, new AttributeModifier(id, 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        attr.put(ASAttributeRegistry.MANA_REND, new AttributeModifier(id, 0.10, AttributeModifier.Operation.ADD_VALUE));
        attr.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(id, 2.5, AttributeModifier.Operation.ADD_VALUE));
        attr.put(Attributes.MAX_HEALTH, new AttributeModifier(id, -0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));

        return attr;
    }

    @SubscribeEvent
    public static void refundMana(SpellOnCastEvent event)
    {
        var originalManaCost = event.getOriginalManaCost();
        var spell = SpellRegistry.getSpell(event.getSpellId());
        var caster = event.getEntity();

        var THORNRING = ((TheThornRingCurio) ItemRegistries.THORN_RING.get());
        if (THORNRING.isEquippedBy(caster))
        {
            if (spell.getSchoolType() == SchoolRegistry.ELDRITCH.get() || spell.getSchoolType() == SchoolRegistry.ELDRITCH.get())
            {
                int newManaCost = originalManaCost / 2;

                event.setManaCost(newManaCost);

                //DiscerningTheEldritch.LOGGER.debug("OG Mana Cost: " + originalManaCost);
                //DiscerningTheEldritch.LOGGER.debug("New Mana Cost: " + event.getManaCost());
            }
        }
    }

    @SubscribeEvent
    public static void dealBonusDamage(LivingIncomingDamageEvent event)
    {
        var source = event.getSource();
        var attacker = event.getSource().getEntity();

        if (attacker instanceof Player player)
        {
            if (ASUtils.hasCurio(player, ItemRegistries.THORN_RING.get()))
            {
                if (source.is(ISSDamageTypes.ELDRITCH_MAGIC) || source.is(ISSDamageTypes.ICE_MAGIC))
                {
                    float baseDamage = event.getOriginalAmount();
                    float newDamage = baseDamage * 1.5F;
                    event.setAmount(newDamage);

                    //DiscerningTheEldritch.LOGGER.debug("OG Damage: " + baseDamage);
                    //DiscerningTheEldritch.LOGGER.debug("New Damage: " + event.getAmount());
                }
            }
        }
    }

    @Override
    public String getIdentifier() {
        return "ring";
    }
}
