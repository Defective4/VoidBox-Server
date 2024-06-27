package io.github.defective4.minecraft.voidbox;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dev.dewy.nbt.api.registry.TagTypeRegistry;
import dev.dewy.nbt.tags.collection.CompoundTag;

/**
 * The core of our new Minecraft server. All "low-level" logic will be handled
 * here
 */
public class MinecraftServer implements AutoCloseable {

    /**
     * Version information of our server
     */
    public static final int PROTOCOL = 754;
    public static final String VERSION = "VoidBox 1.16.5";

    private final String description = "§6A custom Minecraft server!";
    private final CompoundTag dimensionCodec;
    private final String host;

    private final int maxPlayers = 2023;
    /**
     * All clients will be handled in separate threads, so let's create a thread
     * pool.
     */
    private final ExecutorService pool = Executors.newCachedThreadPool();

    private final int port;

    private final ServerSocket server;

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
        server = new ServerSocket();
        try (Reader reader = new InputStreamReader(getClass().getResourceAsStream("/codec.json"))) {
            JsonObject parsed = JsonParser.parseReader(reader).getAsJsonObject();
            dimensionCodec = new CompoundTag().fromJson(parsed, 0, new TagTypeRegistry());
        }
    }

    @Override
    public void close() throws Exception {
        server.close();
    }

    public String getDescription() {
        return description;
    }

    public CompoundTag getDimensionCodec() {
        return dimensionCodec;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public boolean isClosed() {
        return server.isClosed() || !server.isBound();
    }

    /**
     * A method to bind and start the server.
     */
    public void start() throws IOException {
        server.bind(new InetSocketAddress(host, port));
        while (!isClosed()) {
            Socket client = server.accept(); // Accept incoming connections
            pool.submit(() -> {
                try (ClientSession session = new ClientSession(client, this)) {
                    session.handle();
                } catch (Exception ignored) {}
            });
        }
    }
}
