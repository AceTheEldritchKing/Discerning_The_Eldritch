package net.acetheeldritchking.discerning_the_eldritch.networking.devour;

import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.networking.DTEAttachmentSync;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncDevourStacksPacket implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SyncDevourStacksPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "devour_sync"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncDevourStacksPacket> STREAM_CODEC = CustomPacketPayload.codec(SyncDevourStacksPacket::write, SyncDevourStacksPacket::new);

    private int devourStacks = 0;

    public SyncDevourStacksPacket()
    {

    }

    public SyncDevourStacksPacket(RegistryFriendlyByteBuf buf)
    {
        devourStacks = buf.readInt();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(DTEAttachmentSync.getDevour());
    }

    public static void handle(SyncDevourStacksPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> DTEAttachmentSync.setDevour(packet.devourStacks));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
