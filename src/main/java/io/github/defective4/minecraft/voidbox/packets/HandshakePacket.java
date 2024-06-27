package io.github.defective4.minecraft.voidbox.packets;

import java.io.DataInputStream;
import java.io.IOException;

import io.github.defective4.minecraft.voidbox.data.CraftDataTypes;

public class HandshakePacket extends Packet {

    private final String host;
    private final int protocol, state, port;

    /**
     * A constructor for incoming packets
     *
     * @param data
     */
    public HandshakePacket(byte[] data) throws IOException {
        super(data);
        DataInputStream in = getIStream();
        protocol = CraftDataTypes.readVarInt(in);
        host = CraftDataTypes.readString(in);
        port = in.readShort();
        state = in.readByte();
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public int getProtocol() {
        return protocol;
    }

    public int getState() {
        return state;
    }
}
