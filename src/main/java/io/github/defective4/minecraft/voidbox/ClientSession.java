package io.github.defective4.minecraft.voidbox;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import io.github.defective4.minecraft.voidbox.data.ChatMessage;
import io.github.defective4.minecraft.voidbox.data.CraftDataTypes;
import io.github.defective4.minecraft.voidbox.data.GameState;
import io.github.defective4.minecraft.voidbox.packets.Packet;
import io.github.defective4.minecraft.voidbox.packets.PacketRegistry;
import io.github.defective4.minecraft.voidbox.packets.out.login.ServerLoginDisconnectPacket;
import io.github.defective4.minecraft.voidbox.packets.out.play.ServerPlayChatMessagePacket;
import io.github.defective4.minecraft.voidbox.packets.out.play.ServerPlayChatMessagePacket.Position;
import io.github.defective4.minecraft.voidbox.packets.out.play.ServerPlayDisconnectPacket;
import io.github.defective4.minecraft.voidbox.packets.out.play.ServerPlayKeepAlivePacket;

/**
 * This class represents a connection between client and our server. It will
 * contain all methods required to send and receive data from the client.
 */
public class ClientSession implements AutoCloseable {

    private final AnnotatedPacketHandler handler = new AnnotatedPacketHandler(this);
    private final DataInputStream in;

    private final Timer keepAliveTimer = new Timer(true);
    private final OutputStream out;

    private int protocol = -1;
    private boolean responsedToKeepAlive = true;
    private final MinecraftServer server;

    private final Socket socket;

    /**
     * Current connection state
     */
    private GameState state = GameState.HANDSHAKE;

    private String username;

    private UUID uuid;

    public ClientSession(Socket socket, MinecraftServer server) throws IOException {
        this.socket = socket;
        this.server = server;

        out = socket.getOutputStream();
        in = new DataInputStream(socket.getInputStream());
    }

    @Override
    public void close() throws Exception {
        keepAliveTimer.cancel();
        socket.close();
    }
    public void disconnect(String reason) throws Exception {
        if (state == GameState.PLAY) sendPacket(new ServerPlayDisconnectPacket(reason));
        else sendPacket(new ServerLoginDisconnectPacket(reason));
        close();
    }
    public int getProtocol() {
        return protocol;
    }

    public MinecraftServer getServer() {
        return server;
    }

    public GameState getState() {
        return state;
    }

    public String getUsername() {
        return username;
    }

    public UUID getUuid() {
        return uuid;
    }

    /**
     * All data will be received here in a loop
     */
    public void handle() throws IOException {
        while (!isClosed()) {
            int len = CraftDataTypes.readVarInt(in); // Read incoming packet length
            int id = CraftDataTypes.readVarInt(in); // Read incoming packet ID
            byte[] data = new byte[len - CraftDataTypes.getVarIntSize(id)];
            in.readFully(data); // Read remaining packet data

            Class<? extends Packet> packetClass = PacketRegistry.getPacketForID(state, id);
            if (packetClass != null) {
                try {
                    // Construct a new packet instance based on the class detemined earlier.
                    Packet packet = packetClass.getConstructor(byte[].class).newInstance(data);

                    // Handle received packet
                    handler.handle(packet);
                } catch (Exception e) {
                    throw new IOException(e);
                }
            }
        }
    }

    public boolean isClosed() {
        return socket.isClosed() || !socket.isConnected();
    }

    public void sendMessage(ChatMessage message) throws IOException {
        sendPacket(new ServerPlayChatMessagePacket(message, Position.CHAT, null));
    }

    public void sendPacket(Packet packet) throws IOException {
        out.write(packet.getData());
    }

    protected Socket getSocket() {
        return socket;
    }

    protected void scheduleKeepAlive() {
        keepAliveTimer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                try {
                    if (!responsedToKeepAlive) {
                        disconnect("Timed out");
                        cancel();
                        return;
                    }
                    sendPacket(new ServerPlayKeepAlivePacket(System.currentTimeMillis()));
                    responsedToKeepAlive = false;
                } catch (Exception e) {
                    cancel();
                    e.printStackTrace();
                    try {
                        close();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }, 1000, 1000);
    }

    protected void setProtocol(int protocol) {
        this.protocol = protocol;
    }

    protected void setResponsedToKeepAlive(boolean responsedToKeepAlive) {
        this.responsedToKeepAlive = responsedToKeepAlive;
    }

    protected void setState(GameState state) {
        this.state = state;
    }

    protected void setUsername(String username) {
        this.username = username;
    }

    protected void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

}
