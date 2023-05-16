package io.github.defective4.minecraft.voidbox.packets;

import io.github.defective4.minecraft.voidbox.data.CraftDataTypes;

import java.io.*;

/**
 * A container class for all kinds of packets.
 */
public class Packet {
    private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    private final DataOutputStream wrapper = new DataOutputStream(buffer);

    /**
     * A constructor for incoming packets
     *
     * @param data
     */
    protected Packet(byte[] data) throws IOException {
        wrapper.write(data);
    }

    /**
     * A constructor for outgoing packets
     *
     * @param id
     */
    protected Packet(int id) throws IOException {
        CraftDataTypes.writeVarInt(wrapper, id);
    }

    protected DataInputStream getIStream() {
        return new DataInputStream(new ByteArrayInputStream(buffer.toByteArray()));
    }

    protected DataOutputStream getWrapper() {
        return wrapper;
    }

    public byte[] getData() throws IOException {
        byte[] raw = buffer.toByteArray();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        CraftDataTypes.writeVarInt(buffer, raw.length);
        buffer.write(raw);
        return buffer.toByteArray();
    }
}
