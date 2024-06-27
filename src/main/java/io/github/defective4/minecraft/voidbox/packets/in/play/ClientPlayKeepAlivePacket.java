package io.github.defective4.minecraft.voidbox.packets.in.play;

import java.io.IOException;

import io.github.defective4.minecraft.voidbox.packets.Packet;

public class ClientPlayKeepAlivePacket extends Packet {

    private final long id;

    public ClientPlayKeepAlivePacket(byte[] data) throws IOException {
        super(data);
        id = getIStream().readLong();
    }

    public long getId() {
        return id;
    }

}
