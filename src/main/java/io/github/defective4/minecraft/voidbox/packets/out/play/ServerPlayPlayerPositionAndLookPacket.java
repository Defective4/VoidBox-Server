package io.github.defective4.minecraft.voidbox.packets.out.play;

import java.io.IOException;

import io.github.defective4.minecraft.voidbox.packets.Packet;

public class ServerPlayPlayerPositionAndLookPacket extends Packet {

    public ServerPlayPlayerPositionAndLookPacket(double x, double y, double z, float yaw, float pitch)
            throws IOException {
        super(0x34);
        writeDouble(x);
        writeDouble(y);
        writeDouble(z);
        writeFloat(yaw);
        writeFloat(pitch);

        // We don't need to set teleport ID or flags
        writeByte(0);
        writeByte(0);
    }

}
