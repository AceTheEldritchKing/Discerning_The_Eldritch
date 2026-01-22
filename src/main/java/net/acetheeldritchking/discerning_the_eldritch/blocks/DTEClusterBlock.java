package net.acetheeldritchking.discerning_the_eldritch.blocks;

import net.minecraft.world.level.block.AmethystClusterBlock;

public class DTEClusterBlock extends AmethystClusterBlock {
    public enum GrowthStage
    {
        SMALL(3, 4),
        MEDIUM(4, 3),
        LARGE(5, 3),
        CLUSTER(7, 3);

        public final int height;
        public final int offset;

        GrowthStage(int height, int offset)
        {
            this.height = height;
            this.offset = offset;
        }
    }

    protected final GrowthStage growthStage;

    public DTEClusterBlock(Properties properties, GrowthStage growthStage) {
        super(growthStage.height, growthStage.offset, properties);
        this.growthStage = growthStage;
    }

    public GrowthStage getGrowthStage()
    {
        return growthStage;
    }
}
