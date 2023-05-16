package io.github.defective4.minecraft.voidbox;

public class Main {
    /**
     * This will be our application entry point
     *
     * @param args
     */
    public static void main(String[] args) {
        try (MinecraftServer server = new MinecraftServer("localhost", 25565)) {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
