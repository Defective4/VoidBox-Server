package io.github.defective4.minecraft.voidbox.packets.in.play;

import java.io.IOException;

import io.github.defective4.minecraft.voidbox.data.CraftDataTypes;
import io.github.defective4.minecraft.voidbox.packets.Packet;

public class ClientPlayChatMessagePacket extends Packet {

    private final String message;

    public ClientPlayChatMessagePacket(byte[] data) throws IOException {
        super(data);
        message = CraftDataTypes.readString(getIStream());
    }

    public String getMessage() {
        return message;
    }

}
