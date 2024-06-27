package io.github.defective4.minecraft.voidbox.data;

public enum GameMode {
    ADVENTURE(2), CREATIVE(1), NONE(-1), SPECTATOR(3), SURVIVAL(0);

    private final int id;

    private GameMode(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static GameMode getForID(int id) {
        for (GameMode gm : values()) if (gm.id == id) return gm;
        return NONE;
    }

}
