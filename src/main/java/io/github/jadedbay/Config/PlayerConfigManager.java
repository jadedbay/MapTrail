package io.github.jadedbay.Config;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.util.Config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerConfigManager {
    private final Path configDirectory;
    private final Map<UUID, Config<PlayerConfig>> configCache = new ConcurrentHashMap<>();

    public PlayerConfigManager(Path dataDirectory) {
        this.configDirectory = dataDirectory.resolve("player_config");
        try {
            Files.createDirectories(this.configDirectory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<PlayerConfig> loadPlayerConfig(UUID playerId) {
        Config<PlayerConfig> config = new Config<>(configDirectory, playerId.toString(), PlayerConfig.CODEC);
        configCache.put(playerId, config);
        return config.load();
    }

    public void unloadPlayerConfig(UUID playerId) {
        Config<PlayerConfig> config = configCache.remove(playerId);
        if (config == null) { config.save().join(); }
    }

    public PlayerConfig getConfig(UUID playerId) {
        Config<PlayerConfig> config = configCache.get(playerId);
        if (config == null) { throw new IllegalStateException("Config not loaded for player: " + playerId); }

        return config.get();
    }
}
