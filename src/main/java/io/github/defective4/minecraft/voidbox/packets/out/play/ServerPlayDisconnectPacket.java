package io.github.defective4.minecraft.voidbox.packets.out.play;

import java.io.IOException;

import com.google.gson.JsonObject;

import io.github.defective4.minecraft.voidbox.data.CraftDataTypes;
import io.github.defective4.minecraft.voidbox.packets.Packet;

public class ServerPlayDisconnectPacket extends Packet {

    public ServerPlayDisconnectPacket(String message) throws IOException {
        super(0x19);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("text", message);
        CraftDataTypes.writeString(getWrapper(), jsonObject.toString());
    }

}
