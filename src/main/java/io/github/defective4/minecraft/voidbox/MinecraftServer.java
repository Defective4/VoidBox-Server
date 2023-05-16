package io.github.defective4.minecraft.voidbox;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The core of our new Minecraft server.
 * All "low-level" logic will be handled here
 */
public class MinecraftServer implements AutoCloseable {

    /**
     * Version information of our server
     */
    public static final int PROTOCOL = 754;
    public static final String VERSION = "VoidBox 1.16.5";

    private final String host;
    private final int port;
    private final ServerSocket server;

    private final int maxPlayers = 2023;
    private final String description = "§6A custom Minecraft server!";

    /**
     * Out main constructor.
     *
     * @param host
     * @param port
     * @throws IOException
     */
    public MinecraftServer(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        this.server = new ServerSocket();
    }

    /**
     * All clients will be handled in separate threads,
     * so let's create a thred pool.
     */
    private final ExecutorService pool = Executors.newCachedThreadPool();

    /**
     * A method to bind and start the server.
     */
    public void start() throws IOException {
        this.server.bind(new InetSocketAddress(host, port));
        while (!isClosed()) {
            Socket client = this.server.accept(); // Accept incoming connections
            pool.submit(() -> {
                try (ClientSession session = new ClientSession(client, this)) {
                    session.handle();
                } catch (Exception ignored) {
                }
            });
        }
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public String getDescription() {
        return description;
    }

    public boolean isClosed() {
        return server.isClosed() || !server.isBound();
    }

    @Override
    public void close() throws Exception {
        server.close();
    }
}
