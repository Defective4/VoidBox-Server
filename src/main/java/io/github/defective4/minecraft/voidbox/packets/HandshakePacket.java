package io.github.defective4.minecraft.voidbox.packets;

import io.github.defective4.minecraft.voidbox.data.CraftDataTypes;

import java.io.DataInputStream;
import java.io.IOException;

public class HandshakePacket extends Packet {

    private final int protocol, state, port;
    private final String host;

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

    public int getProtocol() {
        return protocol;
    }

    public int getState() {
        return state;
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }
}
