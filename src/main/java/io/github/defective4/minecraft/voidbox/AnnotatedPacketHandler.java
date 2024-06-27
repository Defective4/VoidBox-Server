package io.github.defective4.minecraft.voidbox;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import io.github.defective4.minecraft.voidbox.data.ChatMessage;
import io.github.defective4.minecraft.voidbox.data.GameMode;
import io.github.defective4.minecraft.voidbox.data.GameState;
import io.github.defective4.minecraft.voidbox.data.ServerStatusResponse;
import io.github.defective4.minecraft.voidbox.packets.HandshakePacket;
import io.github.defective4.minecraft.voidbox.packets.Packet;
import io.github.defective4.minecraft.voidbox.packets.in.login.ClientLoginStartPacket;
import io.github.defective4.minecraft.voidbox.packets.in.play.ClientPlayChatMessagePacket;
import io.github.defective4.minecraft.voidbox.packets.in.play.ClientPlayKeepAlivePacket;
import io.github.defective4.minecraft.voidbox.packets.in.status.ClientStatusPingPacket;
import io.github.defective4.minecraft.voidbox.packets.in.status.ClientStatusRequestPacket;
import io.github.defective4.minecraft.voidbox.packets.out.login.ServerLoginDisconnectPacket;
import io.github.defective4.minecraft.voidbox.packets.out.login.ServerLoginSuccessPacket;
import io.github.defective4.minecraft.voidbox.packets.out.play.ServerPlayEmptyChunkPacket;
import io.github.defective4.minecraft.voidbox.packets.out.play.ServerPlayJoinGamePacket;
import io.github.defective4.minecraft.voidbox.packets.out.play.ServerPlayPlayerPositionAndLookPacket;
import io.github.defective4.minecraft.voidbox.packets.out.status.ServerStatusPongPacket;
import io.github.defective4.minecraft.voidbox.packets.out.status.ServerStatusResponsePacket;

/**
 * This class will handle all incoming packets. We will make it use annotations,
 * so the code is easier to read.
 */
@SuppressWarnings("unused")
public class AnnotatedPacketHandler {

    private final ClientSession client;

    public AnnotatedPacketHandler(ClientSession client) {
        this.client = client;
    }

    public ClientSession getClient() {
        return client;
    }

    public void handle(Packet packet) throws InvocationTargetException, IllegalAccessException {
        for (Method method : getClass().getMethods()) if (method.isAnnotationPresent(PacketReceiver.class)) {
            Class<?>[] params = method.getParameterTypes();
            if (params.length == 1 && params[0] == packet.getClass()) method.invoke(this, packet);
        }
    }

    @PacketReceiver
    public void onChatMessage(ClientPlayChatMessagePacket p) throws Exception {
        String message = p.getMessage();
        if (message.length() > 255) {
            client.disconnect("Your message is too long!");
            return;
        }

        if (message.contains("\u00a7")) {
            client.disconnect("Your message contains invalid characters!");
            return;
        }

        if (message.startsWith("/")) return;

        System.out.println(String.format("<%s> %s", client.getUsername(), message));
        client
                .sendMessage(ChatMessage
                        .translate("chat.type.text", ChatMessage.text(client.getUsername()),
                                ChatMessage.text(message)));
    }

    @PacketReceiver
    public void onHandshake(HandshakePacket p) {
        client.setState(p.getState() == 2 ? GameState.LOGIN : GameState.STATUS);
        client.setProtocol(p.getProtocol());
    }

    @PacketReceiver
    public void onKeepAlive(ClientPlayKeepAlivePacket p) {
        client.setResponsedToKeepAlive(true);
    }

    @PacketReceiver
    public void onLoginStart(ClientLoginStartPacket p) throws IOException {
        if (client.getProtocol() != MinecraftServer.PROTOCOL) { // Check if the client is using server's version (in
                                                                // this case it's 1.16.5)
            client.sendPacket(new ServerLoginDisconnectPacket("Please use version 1.16.5 to join this server!"));
            return;
        }

        String username = p.getUsername();
        if (!username.matches("[a-zA-Z0-9]{3,16}")) { // Check if the player is logging in using an alphanumeric
                                                      // username between 3 and 16 characters long
            client.sendPacket(new ServerLoginDisconnectPacket("Your username is invalid!"));
            return;
        }

        UUID uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes());

        client.setUsername(username);
        client.setUuid(uuid);

        client.sendPacket(new ServerLoginSuccessPacket(uuid, username));
        client.setState(GameState.PLAY);

        client
                .sendPacket(new ServerPlayJoinGamePacket(0, false, GameMode.CREATIVE, "minecraft:overworld",
                        client.getServer().getMaxPlayers(), 4, false, true, false, true,
                        client.getServer().getDimensionCodec()));

        client.sendPacket(new ServerPlayPlayerPositionAndLookPacket(8, 16, 8, 0f, 0f));
        client.scheduleKeepAlive();
        client.sendPacket(new ServerPlayEmptyChunkPacket(0, 0));
    }

    @PacketReceiver
    public void onPing(ClientStatusPingPacket p) throws Exception {
        client.sendPacket(new ServerStatusPongPacket(p.getPayload())); // Respond wind a pong packet
        client.close(); // We can safely close the connection now
    }

    @PacketReceiver
    public void onStatusRequest(ClientStatusRequestPacket p) throws IOException {
        client
                .sendPacket(new ServerStatusResponsePacket(new ServerStatusResponse(
                        new ServerStatusResponse.Players(0, client.getServer().getMaxPlayers()),
                        new ServerStatusResponse.Version(MinecraftServer.PROTOCOL, MinecraftServer.VERSION),
                        client.getServer().getDescription()).toJson())); // Respond with out server's status
    }
}
