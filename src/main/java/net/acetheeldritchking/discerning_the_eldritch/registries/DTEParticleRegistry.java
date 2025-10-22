package net.acetheeldritchking.discerning_the_eldritch.registries;

import com.mojang.serialization.MapCodec;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.particle.GlacialShadowParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class DTEParticleRegistry {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, DiscerningTheEldritch.MOD_ID);

    // Glacial Shadow
    public static final Supplier<ParticleType<GlacialShadowParticleOptions>> GLACIAL_SHADOW_PARTICLE = PARTICLE_TYPES.register("glacial_shadow", () -> new ParticleType<>(true)
    {
        public MapCodec<GlacialShadowParticleOptions> codec()
        {
            return GlacialShadowParticleOptions.MAP_CODEC;
        }

        @Override
        public StreamCodec<? super RegistryFriendlyByteBuf, GlacialShadowParticleOptions> streamCodec() {
            return GlacialShadowParticleOptions.STREAM_CODEC;
        }
    });

    // Esoteric Sparks
    public static final Supplier<SimpleParticleType> ESOTERIC_SPARKS_PARTICLE = PARTICLE_TYPES.register("esoteric_sparks", () -> new SimpleParticleType(false));

    // Rift Slice
    public static final Supplier<SimpleParticleType> RIFT_SLICE_PARTICLE = PARTICLE_TYPES.register("rift_slice", () -> new SimpleParticleType(false));

    // Malignant Soul
    public static final Supplier<SimpleParticleType> MALIGNANT_SOUL = PARTICLE_TYPES.register("malignant_soul", () -> new SimpleParticleType(false));


    public static void register(IEventBus eventBus)
    {
        PARTICLE_TYPES.register(eventBus);
    }
}
