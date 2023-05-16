package io.github.defective4.minecraft.voidbox.data;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * An "utility" class that will contain all methods required to write
 * and read Minecraft-specific data types.
 */
public class CraftDataTypes {

    public static int getVarIntSize(int value) {
        int size = 0;
        do {
            value >>>= 7;
            size++;
        } while (value != 0);
        return size;
    }

    public static void writeString(OutputStream os, String value) throws IOException {
        byte[] data = value.getBytes(StandardCharsets.UTF_8);
        writeVarInt(os, data.length);
        os.write(data);
    }

    public static String readString(DataInputStream is) throws IOException {
        byte[] data = new byte[readVarInt(is)];
        is.readFully(data);
        return new String(data, StandardCharsets.UTF_8);
    }

    public static int readVarInt(DataInputStream in) throws IOException {
        int numRead = 0;
        int result = 0;
        byte read;
        do {
            read = in.readByte();
            int value = (read & 0b01111111);
            result |= (value << (7 * numRead));
            numRead++;
            if (numRead > 5) {
                throw new RuntimeException("VarInt is too big");
            }
        } while ((read & 0b10000000) != 0);
        return result;
    }

    public static void writeVarInt(OutputStream out, int value) throws IOException {
        do {
            byte temp = (byte) (value & 0b01111111);
            value >>>= 7;
            if (value != 0) {
                temp |= 0b10000000;
            }
            out.write(temp);
        } while (value != 0);
    }
}
