package io.github.defective4.minecraft.voidbox.packets.out.play;

import java.io.IOException;

import io.github.defective4.minecraft.voidbox.packets.Packet;

public class ServerPlayKeepAlivePacket extends Packet {

    public ServerPlayKeepAlivePacket(long id) throws IOException {
        super(0x1F);
        writeLong(id);
    }

}
