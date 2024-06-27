package io.github.defective4.minecraft.voidbox.packets.out.login;

import java.io.IOException;

import com.google.gson.JsonObject;

import io.github.defective4.minecraft.voidbox.data.CraftDataTypes;
import io.github.defective4.minecraft.voidbox.packets.Packet;

public class ServerLoginDisconnectPacket extends Packet {

    public ServerLoginDisconnectPacket(String message) throws IOException {
        super(0x00);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("text", message);
        CraftDataTypes.writeString(getWrapper(), jsonObject.toString());
    }

}
