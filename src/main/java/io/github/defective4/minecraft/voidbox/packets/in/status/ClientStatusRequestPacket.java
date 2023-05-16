package io.github.defective4.minecraft.voidbox.packets.in.status;

import io.github.defective4.minecraft.voidbox.packets.Packet;

import java.io.IOException;

public class ClientStatusRequestPacket extends Packet {
    /**
     * A constructor for incoming packets
     *
     * @param data
     */
    public ClientStatusRequestPacket(byte[] data) throws IOException {
        super(data);
    }
}
