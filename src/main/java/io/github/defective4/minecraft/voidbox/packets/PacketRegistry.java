package io.github.defective4.minecraft.voidbox.packets;

import io.github.defective4.minecraft.voidbox.data.GameState;
import io.github.defective4.minecraft.voidbox.packets.in.status.ClientStatusPingPacket;
import io.github.defective4.minecraft.voidbox.packets.in.status.ClientStatusRequestPacket;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A class containing all packet references by their ID
 */
public class PacketRegistry {
    private static final Map<GameState, Map<Integer, Class<? extends Packet>>> PACKETS = new ConcurrentHashMap<>();

    static {
        PACKETS.put(GameState.HANDSHAKE, new ConcurrentHashMap<Integer, Class<? extends Packet>>() {
            {
                put(0x00, HandshakePacket.class);
            }
        });

        PACKETS.put(GameState.PLAY, new ConcurrentHashMap<Integer, Class<? extends Packet>>() {
            {

            }
        });

        PACKETS.put(GameState.LOGIN, new ConcurrentHashMap<Integer, Class<? extends Packet>>() {
            {

            }
        });

        PACKETS.put(GameState.STATUS, new ConcurrentHashMap<Integer, Class<? extends Packet>>() {
            {
                put(0x00, ClientStatusRequestPacket.class);
                put(0x01, ClientStatusPingPacket.class);
            }
        });
    }

    public static Class<? extends Packet> getPacketForID(GameState state, int id) {
        return PACKETS.getOrDefault(state, new WeakHashMap<>()).get(id);
    }
}
