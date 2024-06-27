package io.github.defective4.minecraft.voidbox.packets.out.login;

import java.io.IOException;
import java.util.UUID;

import io.github.defective4.minecraft.voidbox.data.CraftDataTypes;
import io.github.defective4.minecraft.voidbox.packets.Packet;

public class ServerLoginSuccessPacket extends Packet {

    public ServerLoginSuccessPacket(UUID uuid, String username) throws IOException {
        super(0x02);
        CraftDataTypes.writeUUID(this, uuid);
        CraftDataTypes.writeString(getWrapper(), username);
    }

}
