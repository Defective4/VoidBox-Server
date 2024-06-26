package io.github.defective4.minecraft.voidbox.packets.out.status;

import java.io.IOException;

import io.github.defective4.minecraft.voidbox.data.CraftDataTypes;
import io.github.defective4.minecraft.voidbox.packets.Packet;

public class ServerStatusResponsePacket extends Packet {

    public ServerStatusResponsePacket(String json) throws IOException {
        super(0x00);
        CraftDataTypes.writeString(getWrapper(), json);
    }
}
