package io.github.defective4.minecraft.voidbox.data;

import com.google.gson.Gson;

/**
 * A container for status returned in Server List Ping sequence
 */
public class ServerStatusResponse {

    final Players players;
    final Version version;
    final String description;

    public ServerStatusResponse(Players players, Version version, String description) {
        this.players = players;
        this.version = version;
        this.description = description;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public static class Players {
        final int online, max;

        public Players(int online, int max) {
            this.online = online;
            this.max = max;
        }
    }

    public static class Version {
        final int protocol;
        final String name;

        public Version(int protocol, String name) {
            this.protocol = protocol;
            this.name = name;
        }
    }
}
