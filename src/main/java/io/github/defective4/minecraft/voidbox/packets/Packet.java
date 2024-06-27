package io.github.defective4.minecraft.voidbox.packets;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;

import io.github.defective4.minecraft.voidbox.data.CraftDataTypes;

/**
 * A container class for all kinds of packets.
 */
public class Packet implements DataOutput {
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

    public byte[] getData() throws IOException {
        byte[] raw = buffer.toByteArray();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        CraftDataTypes.writeVarInt(buffer, raw.length);
        buffer.write(raw);
        return buffer.toByteArray();
    }

    @Override
    public void write(byte[] b) throws IOException {
        wrapper.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        wrapper.write(b, off, len);
    }

    @Override
    public void write(int b) throws IOException {
        wrapper.write(b);
    }

    @Override
    public void writeBoolean(boolean v) throws IOException {
        wrapper.writeBoolean(v);
    }

    @Override
    public void writeByte(int v) throws IOException {
        wrapper.writeByte(v);
    }

    @Override
    public void writeBytes(String s) throws IOException {
        wrapper.writeBytes(s);
    }

    @Override
    public void writeChar(int v) throws IOException {
        wrapper.writeChar(v);
    }

    @Override
    public void writeChars(String s) throws IOException {
        wrapper.writeChars(s);
    }

    @Override
    public void writeDouble(double v) throws IOException {
        wrapper.writeDouble(v);
    }

    @Override
    public void writeFloat(float v) throws IOException {
        wrapper.writeFloat(v);
    }

    @Override
    public void writeInt(int v) throws IOException {
        wrapper.writeInt(v);
    }

    @Override
    public void writeLong(long v) throws IOException {
        wrapper.writeLong(v);
    }

    @Override
    public void writeShort(int v) throws IOException {
        wrapper.writeShort(v);
    }

    @Override
    public void writeUTF(String s) throws IOException {
        wrapper.writeUTF(s);
    }

    protected DataInputStream getIStream() {
        return new DataInputStream(new ByteArrayInputStream(buffer.toByteArray()));
    }

    protected DataOutputStream getWrapper() {
        return wrapper;
    }
}
