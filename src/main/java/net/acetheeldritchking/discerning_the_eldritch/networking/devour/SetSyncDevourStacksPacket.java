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

public class SetSyncDevourStacksPacket implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SetSyncDevourStacksPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "set_devour_sync"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SetSyncDevourStacksPacket> STREAM_CODEC = CustomPacketPayload.codec(SetSyncDevourStacksPacket::write, SetSyncDevourStacksPacket::new);

    private int devourStacks;

    public SetSyncDevourStacksPacket(LivingEntity entity)
    {
        devourStacks = DTEAttachmentSync.getDevour(entity);
    }

    public SetSyncDevourStacksPacket(RegistryFriendlyByteBuf buf)
    {
        devourStacks = buf.readInt();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(devourStacks);
    }

    public static void handle(SetSyncDevourStacksPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> DTEAttachmentSync.setDevour(1, context.player()));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
