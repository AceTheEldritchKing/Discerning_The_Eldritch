package net.acetheeldritchking.discerning_the_eldritch.blocks;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AmethystBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class DTEGemstoneBlock extends AmethystBlock {
    // Referencing Pastel's custom gemstone stuff which seems like it's just amethyst stuff
    public static final MapCodec<DTEGemstoneBlock> CODEC =
            RecordCodecBuilder.mapCodec(i -> i.group(
                    propertiesCodec(),
                    SoundEvent.DIRECT_CODEC.fieldOf("hit_sound_event")
                            .forGetter(b -> b.hitSoundEvent),
                    SoundEvent.DIRECT_CODEC.fieldOf("chime_sound_event")
                            .forGetter(b -> b.chimeSoundEvent)
            )
            .apply(
                    i, DTEGemstoneBlock::new
            ));

    private final SoundEvent hitSoundEvent;
    private final SoundEvent chimeSoundEvent;

    public DTEGemstoneBlock(Properties properties, SoundEvent hitSoundEvent, SoundEvent chimeSoundEvent) {
        super(properties);
        this.chimeSoundEvent = chimeSoundEvent;
        this.hitSoundEvent = hitSoundEvent;
    }

    @Override
    public MapCodec<? extends DTEGemstoneBlock> codec() {
        return CODEC;
    }

    @Override
    protected void onProjectileHit(Level level, BlockState blockState, BlockHitResult hitResult, Projectile projectile) {
        if (!level.isClientSide) {
            BlockPos blockpos = hitResult.getBlockPos();
            level.playSound(null, blockpos, hitSoundEvent, SoundSource.BLOCKS, 1.0F, 0.5F + level.random.nextFloat() * 1.2F);
            level.playSound(null, blockpos, chimeSoundEvent, SoundSource.BLOCKS, 1.0F, 0.5F + level.random.nextFloat() * 1.2F);
        }
    }
}
