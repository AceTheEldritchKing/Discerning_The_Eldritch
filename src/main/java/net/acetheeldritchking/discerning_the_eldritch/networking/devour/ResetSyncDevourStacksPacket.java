package net.acetheeldritchking.discerning_the_eldritch.networking.devour;

import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.networking.DTEAttachmentSync;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ResetSyncDevourStacksPacket implements CustomPacketPayload {
    public static final Type<ResetSyncDevourStacksPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "reset_devour_sync"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ResetSyncDevourStacksPacket> STREAM_CODEC = CustomPacketPayload.codec(ResetSyncDevourStacksPacket::write, ResetSyncDevourStacksPacket::new);

    private int devourStacks;

    public ResetSyncDevourStacksPacket(LivingEntity entity)
    {
        devourStacks = DTEAttachmentSync.getDevour(entity);
    }

    public ResetSyncDevourStacksPacket(RegistryFriendlyByteBuf buf)
    {
        devourStacks = buf.readInt();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(devourStacks);
    }

    public static void handle(ResetSyncDevourStacksPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> DTEAttachmentSync.setDevour(0, context.player()));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
