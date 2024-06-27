package io.github.defective4.minecraft.voidbox.packets.in.login;

import java.io.IOException;

import io.github.defective4.minecraft.voidbox.data.CraftDataTypes;
import io.github.defective4.minecraft.voidbox.packets.Packet;

public class ClientLoginStartPacket extends Packet {

    private final String username;

    public ClientLoginStartPacket(byte[] data) throws IOException {
        super(data);
        username = CraftDataTypes.readString(getIStream());
    }

    public String getUsername() {
        return username;
    }

}
