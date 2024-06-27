package io.github.defective4.minecraft.voidbox.packets.out.play;

import java.io.IOException;

import dev.dewy.nbt.Nbt;
import dev.dewy.nbt.tags.collection.CompoundTag;
import io.github.defective4.minecraft.voidbox.data.CraftDataTypes;
import io.github.defective4.minecraft.voidbox.data.GameMode;
import io.github.defective4.minecraft.voidbox.packets.Packet;

public class ServerPlayJoinGamePacket extends Packet {

    public ServerPlayJoinGamePacket(int entityID, boolean hardcore, GameMode gamemode, String world, int maxPlayers,
            int viewDistance, boolean reducedDebugInfo, boolean enableRespawnScreen, boolean debug, boolean flat,
            CompoundTag codec) throws IOException {
        super(0x24);
        writeInt(entityID);
        writeBoolean(hardcore);
        writeByte(gamemode.getId());
        writeByte(-1);
        CraftDataTypes.writeVarInt(getWrapper(), 1);
        CraftDataTypes.writeString(getWrapper(), world);

        CompoundTag dimension = (CompoundTag) codec.getCompound("minecraft:dimension_type").getList("value").get(0);

        Nbt nbt = new Nbt();
        nbt.toStream(codec, getWrapper());
        nbt.toStream(dimension, getWrapper());

        CraftDataTypes.writeString(getWrapper(), world);
        writeLong(0);
        CraftDataTypes.writeVarInt(getWrapper(), maxPlayers);
        CraftDataTypes.writeVarInt(getWrapper(), viewDistance);
        writeBoolean(reducedDebugInfo);
        writeBoolean(enableRespawnScreen);
        writeBoolean(debug);
        writeBoolean(flat);
    }

}
