package net.acetheeldritchking.discerning_the_eldritch.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEParticleRegistry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.StreamCodec;

public class GlacialShadowParticleOptions implements ParticleOptions {
    public final float xf;
    public final float yf;
    public final float zf;

    public GlacialShadowParticleOptions (float xf, float yf, float zf)
    {
        this.xf = xf;
        this.yf = yf;
        this.zf = zf;
    }

    public static StreamCodec<? super ByteBuf, GlacialShadowParticleOptions> STREAM_CODEC = StreamCodec.of(
            (buf, option) ->
            {
                buf.writeFloat(option.xf);
                buf.writeFloat(option.yf);
                buf.writeFloat(option.zf);
            },
            (buf) -> new GlacialShadowParticleOptions(buf.readFloat(), buf.readFloat(), buf.readFloat())
    );

    public static MapCodec<GlacialShadowParticleOptions> MAP_CODEC = RecordCodecBuilder.mapCodec(object ->
            object.group(
                    Codec.FLOAT.fieldOf("xf").forGetter(p -> ((GlacialShadowParticleOptions) p).xf),
                    Codec.FLOAT.fieldOf("yf").forGetter(p -> ((GlacialShadowParticleOptions) p).yf),
                    Codec.FLOAT.fieldOf("zf").forGetter(p -> ((GlacialShadowParticleOptions) p).zf)
            ).apply(object, GlacialShadowParticleOptions::new));

    @Override
    public ParticleType<?> getType() {
        return DTEParticleRegistry.GLACIAL_SHADOW_PARTICLE.get();
    }
}
