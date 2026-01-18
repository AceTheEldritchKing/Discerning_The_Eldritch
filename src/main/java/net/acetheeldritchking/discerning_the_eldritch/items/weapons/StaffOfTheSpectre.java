package net.acetheeldritchking.discerning_the_eldritch.items.weapons;

import io.redspace.ironsspellbooks.api.item.weapons.ExtendedSwordItem;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.item.UniqueItem;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.acetheeldritchking.aces_spell_utils.items.weapons.ActiveAndPassiveAbilityMagicSwordItem;
import net.acetheeldritchking.aces_spell_utils.utils.ASRarities;
import net.acetheeldritchking.discerning_the_eldritch.items.custom.DTEItemDispatcher;

public class StaffOfTheSpectre extends ActiveAndPassiveAbilityMagicSwordItem implements UniqueItem {
    public static final int PASSIVE_COOLDOWN = 15 * 20;
    public static final int ACTIVE_COOLDOWN = 15 * 20;
    public final DTEItemDispatcher dispatcher;

    public StaffOfTheSpectre() {
        super(
                DTEWeaponTiers.STAFF_OF_THE_SPECTRE,
                ItemPropertiesHelper.equipment(1).fireResistant().rarity(ASRarities.SCULK_RARITY_PROXY.getValue()).attributes(ExtendedSwordItem.createAttributes(DTEWeaponTiers.STAFF_OF_THE_SPECTRE)),
                SpellDataRegistryHolder.of(
                        new SpellDataRegistryHolder(SpellRegistry.SONIC_BOOM_SPELL, 10))
        );
        this.dispatcher = new DTEItemDispatcher();
    }

    @Override
    protected int getActiveCooldownTicks() {
        return ACTIVE_COOLDOWN;
    }

    @Override
    protected int getPassiveCooldownTicks() {
        return PASSIVE_COOLDOWN;
    }
}
