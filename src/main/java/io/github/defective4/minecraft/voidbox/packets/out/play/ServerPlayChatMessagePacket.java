package io.github.defective4.minecraft.voidbox.packets.out.play;

import java.io.IOException;
import java.util.UUID;

import io.github.defective4.minecraft.voidbox.data.ChatMessage;
import io.github.defective4.minecraft.voidbox.data.CraftDataTypes;
import io.github.defective4.minecraft.voidbox.packets.Packet;

public class ServerPlayChatMessagePacket extends Packet {

    public enum Position {
        CHAT(0), HOTBAR(2), SYSTEM(1);

        private final int id;

        private Position(int id) {
            this.id = id;
        }

    }

    public ServerPlayChatMessagePacket(ChatMessage message, Position pos, UUID sender) throws IOException {
        super(0x0E);
        CraftDataTypes.writeString(getWrapper(), message.toJson());
        writeByte(pos.id);
        CraftDataTypes.writeUUID(getWrapper(), sender == null ? new UUID(0, 0) : sender);
    }

}
