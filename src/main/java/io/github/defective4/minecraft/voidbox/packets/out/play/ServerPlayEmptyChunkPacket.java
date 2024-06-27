package io.github.defective4.minecraft.voidbox.packets.out.play;

import java.io.IOException;

import dev.dewy.nbt.Nbt;
import dev.dewy.nbt.tags.collection.CompoundTag;
import io.github.defective4.minecraft.voidbox.data.CraftDataTypes;
import io.github.defective4.minecraft.voidbox.packets.Packet;

public class ServerPlayEmptyChunkPacket extends Packet {
    public ServerPlayEmptyChunkPacket(int x, int z) throws IOException {
        super(0x20);
        writeInt(x);
        writeInt(z);
        writeBoolean(true);
        writeByte(0);

        CompoundTag tag = new CompoundTag();
        Nbt nbt = new Nbt();
        tag.putLongArray("MOTION_BLOCKING", new long[256]);
        nbt.toStream(tag, getWrapper());
        CraftDataTypes.writeVarInt(getWrapper(), 1024);
        write(new byte[16 * 16 * 16]);
        writeByte(0);
    }
}
