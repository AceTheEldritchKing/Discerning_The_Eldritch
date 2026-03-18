package net.acetheeldritchking.discerning_the_eldritch.items.curios;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import io.redspace.ironsspellbooks.api.events.SpellOnCastEvent;
import io.redspace.ironsspellbooks.api.events.SpellPreCastEvent;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.compat.Curios;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.acetheeldritchking.aces_spell_utils.items.curios.FlatCooldownPassiveAbilityCurio;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.registries.ItemRegistries;
import net.acetheeldritchking.discerning_the_eldritch.utils.DTERarities;
import net.acetheeldritchking.discerning_the_eldritch.utils.DTEServerConfig;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import top.theillusivec4.curios.api.SlotContext;

@EventBusSubscriber
public class ExorcistsAegisCurio extends FlatCooldownPassiveAbilityCurio {
    public ExorcistsAegisCurio() {
        super(ItemPropertiesHelper.equipment().stacksTo(1).fireResistant().rarity(DTERarities.DIVINE_RARITY_PROXY.getValue()), Curios.RING_SLOT);
    }

    @Override
    protected int getCooldownTicks() {
        return (0);
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        Multimap<Holder<Attribute>, AttributeModifier> attr = LinkedHashMultimap.create();
        attr.put(AttributeRegistry.ELDRITCH_SPELL_POWER, new AttributeModifier(id, -0.75, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));

        return attr;
    }

    @SubscribeEvent
    public static void doubleMana(SpellOnCastEvent event)
    {
        var originalManaCost = event.getOriginalManaCost();
        var spell = SpellRegistry.getSpell(event.getSpellId());
        var caster = event.getEntity();

        var AEGIS = ((ExorcistsAegisCurio) ItemRegistries.EXORCISTS_AEGIS.get());
        if (AEGIS.isEquippedBy(caster))
        {
            if (spell.getSchoolType() == SchoolRegistry.ELDRITCH.get())
            {
                int newManaCost = originalManaCost * 2;

                event.setManaCost(newManaCost);

                //DiscerningTheEldritch.LOGGER.debug("OG Mana Cost: " + originalManaCost);
                //DiscerningTheEldritch.LOGGER.debug("New Mana Cost: " + event.getManaCost());
            }
        }
    }

    // Have the chance be configurable
    @SubscribeEvent
    public static void failSpellCast(SpellPreCastEvent event)
    {
        var spell = SpellRegistry.getSpell(event.getSpellId());
        var caster = event.getEntity();

        var AEGIS = ((ExorcistsAegisCurio) ItemRegistries.EXORCISTS_AEGIS.get());
        if (AEGIS.isEquippedBy(caster))
        {
            if (spell.getSchoolType() == SchoolRegistry.ELDRITCH.get())
            {
                float chance = (caster.getRandom().nextFloat() * 100);

                if (chance <= DTEServerConfig.exorcistsAegisFailChance)
                {
                    if (caster instanceof ServerPlayer serverPlayer)
                    {
                        // display a message to the player
                        serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(Component.translatable("display.discerning_the_eldritch.eldritch_cast_fail")
                                .withStyle(s -> s.withColor(TextColor.fromRgb(0xF35F5F)))));
                        serverPlayer.level().playSound(null , caster.getX() , caster.getY() , caster.getZ() ,
                                SoundEvents.FIRE_EXTINGUISH , SoundSource.PLAYERS , 0.5f , 1f);
                    }

                    event.setCanceled(true);
                }
            }
        }
    }
}
