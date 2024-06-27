package io.github.defective4.minecraft.voidbox.packets.in.status;

import java.io.IOException;

import io.github.defective4.minecraft.voidbox.packets.Packet;

/**
 * Ping packet is sent by client after receiving server status in Server List
 * Ping sequence. It is used to determine latency between client and server.
 */
public class ClientStatusPingPacket extends Packet {

    private final long payload;

    public ClientStatusPingPacket(byte[] data) throws IOException {
        super(data);
        payload = getIStream().readLong();
    }

    public long getPayload() {
        return payload;
    }
}
