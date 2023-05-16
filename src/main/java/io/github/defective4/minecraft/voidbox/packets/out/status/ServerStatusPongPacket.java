package io.github.defective4.minecraft.voidbox.packets.out.status;

import io.github.defective4.minecraft.voidbox.packets.Packet;

import java.io.IOException;

public class ServerStatusPongPacket extends Packet {
    public ServerStatusPongPacket(long payload) throws IOException {
        super(0x01);
        getWrapper().writeLong(payload);
    }
}
