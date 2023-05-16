package io.github.defective4.minecraft.voidbox;

import io.github.defective4.minecraft.voidbox.data.GameState;
import io.github.defective4.minecraft.voidbox.data.ServerStatusResponse;
import io.github.defective4.minecraft.voidbox.packets.HandshakePacket;
import io.github.defective4.minecraft.voidbox.packets.Packet;
import io.github.defective4.minecraft.voidbox.packets.in.status.ClientStatusPingPacket;
import io.github.defective4.minecraft.voidbox.packets.in.status.ClientStatusRequestPacket;
import io.github.defective4.minecraft.voidbox.packets.out.status.ServerStatusPongPacket;
import io.github.defective4.minecraft.voidbox.packets.out.status.ServerStatusResponsePacket;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * This class will handle all incoming packets.
 * We will make it use annotations, so the code is easier to read.
 */
public class AnnotatedPacketHandler {

    private final ClientSession client;

    public AnnotatedPacketHandler(ClientSession client) {
        this.client = client;
    }

    @PacketReceiver
    public void onPing(ClientStatusPingPacket p) throws Exception {
        client.sendPacket(new ServerStatusPongPacket(p.getPayload())); // Respond wind a pong packet
        client.close(); // We can safely close the connection now
    }

    @PacketReceiver
    public void onStatusRequest(ClientStatusRequestPacket p) throws IOException {
        client.sendPacket(new ServerStatusResponsePacket(
                new ServerStatusResponse(
                        new ServerStatusResponse.Players(0, client.getServer().getMaxPlayers()),
                        new ServerStatusResponse.Version(MinecraftServer.PROTOCOL, MinecraftServer.VERSION),
                        client.getServer().getDescription()
                ).toJson()
        )); // Respond with out server's status
    }

    @PacketReceiver
    public void onHandshake(HandshakePacket p) {
        client.setState(p.getState() == 2 ? GameState.LOGIN : GameState.STATUS);
        client.setProtocol(p.getProtocol());
    }

    public void handle(Packet packet) throws InvocationTargetException, IllegalAccessException {
        for (Method method : getClass().getMethods())
            if (method.isAnnotationPresent(PacketReceiver.class)) {
                Class<?>[] params = method.getParameterTypes();
                if (params.length == 1 && params[0] == packet.getClass())
                    method.invoke(this, packet);
            }
    }

    public ClientSession getClient() {
        return client;
    }
}
