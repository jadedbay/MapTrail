package io.github.jadedbay.Config;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.util.Config;
import io.github.jadedbay.MapTrailPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class PlayerConfigManager {
    private final Path configDirectory;
    private final Map<UUID, Config<PlayerConfig>> configCache = new ConcurrentHashMap<>();

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

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

        Path configFile = configDirectory.resolve(playerId.toString() + ".json");

        return config.load().thenApply(playerConfig -> {
            if (!Files.exists(configFile)) {
                playerConfig.copyFrom(MapTrailPlugin.getDefaultPlayerConfig().get());
                config.save();
                LOGGER.atInfo().log("Created new player config: " + playerId);
            }

            return playerConfig;
        });
    }

    public void unloadPlayerConfig(UUID playerId) {
        Config<PlayerConfig> config = configCache.remove(playerId);
        if (config == null) { config.save().join(); }
    }

    public Config<PlayerConfig> getConfig(UUID playerId) {
        Config<PlayerConfig> config = configCache.get(playerId);
        if (config == null) { throw new IllegalStateException("Config not loaded for player: " + playerId); }

        return config;
    }

    public void updateConfig(UUID playerId, Consumer<PlayerConfig> consumer) {
        Config<PlayerConfig> config = configCache.get(playerId);
        if (config == null) { throw new IllegalStateException("Config not loaded for player: " + playerId); }
        consumer.accept(config.get());
        config.save();
    }
}
