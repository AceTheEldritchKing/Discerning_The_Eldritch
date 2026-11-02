package net.acetheeldritchking.discerning_the_eldritch.items.weapons;

import io.redspace.ironslib.kinetic_weapon.AttackRange;
import io.redspace.ironslib.kinetic_weapon.KineticWeapon;
import io.redspace.ironsspellbooks.item.weapons.IronsWeaponTier;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.acetheeldritchking.aces_spell_utils.items.weapons.ExtendedSpearLikeItem;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;

import java.util.Optional;

public class GoreLanceItem extends ExtendedSpearLikeItem {
    public GoreLanceItem() {
        super(
                DTEWeaponTiers.GORE_LANCE,
                new AttackRange(2.0F, 4.5F, 2.0F, 6.5F, 0.125F, 0.5F),
                new KineticWeapon(
                        20,
                        10,
                        // Dismount
                        Optional.of(new KineticWeapon.Condition((int) (20*5.5), 7F, 6F)),
                        // Knockback
                        Optional.of(new KineticWeapon.Condition(20*8, 6F, 4.5F)),
                        // Damage
                        Optional.of(new KineticWeapon.Condition(20*10, 5F, 3.5F)),
                        1F,
                        2.5F,
                        Optional.of(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundRegistry.BLOOD_CAST.get())),
                        Optional.of(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundRegistry.BLOOD_NEEDLE_IMPACT.get())),
                        KineticWeapon.SPEAR_ANIMATION
                ),
                Rarity.EPIC,
                DTEWeaponTiers.GORE_LANCE,
                Optional.of(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundRegistry.BLOOD_CAST.get())),
                Optional.of(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundRegistry.BLOOD_NEEDLE_IMPACT.get()))
        );
    }
}
