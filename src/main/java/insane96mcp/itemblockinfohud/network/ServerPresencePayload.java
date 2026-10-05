package insane96mcp.itemblockinfohud.network;

import insane96mcp.itemblockinfohud.ItemBlockInfoHud;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

/**
 * Never actually sent. Registered as optional so that the server doesn't kick clients without the mod,
 * while the client can check if the server has the channel (and so the mod) and disconnect if not.
 */
public record ServerPresencePayload() implements CustomPacketPayload {
    public static final ServerPresencePayload INSTANCE = new ServerPresencePayload();
    public static final Type<ServerPresencePayload> TYPE = new Type<>(ItemBlockInfoHud.id("server_presence"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ServerPresencePayload> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void register(RegisterPayloadHandlersEvent event) {
        event.registrar("1")
                .optional()
                .playToClient(TYPE, STREAM_CODEC, (payload, context) -> {});
    }
}
