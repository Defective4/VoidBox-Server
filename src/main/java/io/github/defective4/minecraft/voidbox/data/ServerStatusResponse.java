package io.github.defective4.minecraft.voidbox.data;

import com.google.gson.Gson;

/**
 * A container for status returned in Server List Ping sequence
 */
public class ServerStatusResponse {

    public static class Players {
        final int online, max;

        public Players(int online, int max) {
            this.online = online;
            this.max = max;
        }
    }
    public static class Version {
        final String name;
        final int protocol;

        public Version(int protocol, String name) {
            this.protocol = protocol;
            this.name = name;
        }
    }
    final String description;

    final Players players;

    final Version version;

    public ServerStatusResponse(Players players, Version version, String description) {
        this.players = players;
        this.version = version;
        this.description = description;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}
