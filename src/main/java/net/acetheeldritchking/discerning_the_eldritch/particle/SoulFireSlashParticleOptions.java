package net.acetheeldritchking.discerning_the_eldritch.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEParticleRegistry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.StreamCodec;

public class SoulFireSlashParticleOptions implements ParticleOptions {
    public final float scale;
    public final float xf;
    public final float yf;
    public final float zf;
    public final boolean mirror, vertical;

    public SoulFireSlashParticleOptions(float xf, float yf, float zf, boolean mirror, boolean vertical, float scale) {
        this.scale = scale;
        this.xf = xf;
        this.yf = yf;
        this.zf = zf;
        this.mirror = mirror;
        this.vertical = vertical;
    }

    public static StreamCodec<? super ByteBuf, SoulFireSlashParticleOptions> STREAM_CODEC = StreamCodec.of(
            (buf, option) ->
            {
                buf.writeFloat(option.xf);
                buf.writeFloat(option.yf);
                buf.writeFloat(option.zf);
                buf.writeBoolean(option.mirror);
                buf.writeBoolean(option.vertical);
                buf.writeFloat(option.scale);
            },
            (byteBuf -> new SoulFireSlashParticleOptions(byteBuf.readFloat(), byteBuf.readFloat(), byteBuf.readFloat(), byteBuf.readBoolean(), byteBuf.readBoolean(), byteBuf.readFloat()))
    );

    public static MapCodec<SoulFireSlashParticleOptions> MAP_CODEC = RecordCodecBuilder.mapCodec(object ->
            object.group(
                    Codec.FLOAT.fieldOf("xf").forGetter(p -> (p).xf),
                    Codec.FLOAT.fieldOf("xf").forGetter(p -> (p).yf),
                    Codec.FLOAT.fieldOf("xf").forGetter(p -> (p).zf),
                    Codec.BOOL.fieldOf("xf").forGetter(p -> (p).mirror),
                    Codec.BOOL.fieldOf("xf").forGetter(p -> (p).vertical),
                    Codec.FLOAT.fieldOf("xf").forGetter(p -> (p).scale)
            ).apply(object, SoulFireSlashParticleOptions::new)
    );

    @Override
    public ParticleType<?> getType() {
        return DTEParticleRegistry.SOUL_FIRE_SLASH_PARTICLE.get();
    }
}
