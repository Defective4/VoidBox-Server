package io.github.defective4.minecraft.voidbox.packets.in.status;

import java.io.IOException;

import io.github.defective4.minecraft.voidbox.packets.Packet;

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
