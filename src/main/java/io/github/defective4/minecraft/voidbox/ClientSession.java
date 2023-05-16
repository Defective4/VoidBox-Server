package io.github.defective4.minecraft.voidbox;

import io.github.defective4.minecraft.voidbox.data.CraftDataTypes;
import io.github.defective4.minecraft.voidbox.data.GameState;
import io.github.defective4.minecraft.voidbox.packets.Packet;
import io.github.defective4.minecraft.voidbox.packets.PacketRegistry;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * This class represents a connection between client and our server.
 * It will contain all methods required to send and receive data from the client.
 */
public class ClientSession implements AutoCloseable {

    private final Socket socket;
    private final MinecraftServer server;

    private final OutputStream out;
    private final DataInputStream in;

    private final AnnotatedPacketHandler handler = new AnnotatedPacketHandler(this);

    public ClientSession(Socket socket, MinecraftServer server) throws IOException {
        this.socket = socket;
        this.server = server;

        out = socket.getOutputStream();
        in = new DataInputStream(socket.getInputStream());
    }

    /**
     * Current connection state
     */
    private GameState state = GameState.HANDSHAKE;

    private int protocol = -1;

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

    public void sendPacket(Packet packet) throws IOException {
        out.write(packet.getData());
    }

    public int getProtocol() {
        return protocol;
    }

    protected void setProtocol(int protocol) {
        this.protocol = protocol;
    }

    public GameState getState() {
        return state;
    }

    protected void setState(GameState state) {
        this.state = state;
    }

    protected Socket getSocket() {
        return socket;
    }

    public MinecraftServer getServer() {
        return server;
    }

    public boolean isClosed() {
        return socket.isClosed() || !socket.isConnected();
    }

    @Override
    public void close() throws Exception {
        socket.close();
    }
}
