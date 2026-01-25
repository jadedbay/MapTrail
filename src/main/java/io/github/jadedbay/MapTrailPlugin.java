package io.github.jadedbay;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.protocol.Packet;
import com.hypixel.hytale.protocol.packets.player.ClientMovement;
import com.hypixel.hytale.server.core.auth.PlayerAuthentication;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.io.PacketHandler;
import com.hypixel.hytale.server.core.io.adapter.PacketAdapters;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.events.AddWorldEvent;
import com.hypixel.hytale.server.core.util.Config;
import io.github.jadedbay.Commands.MapTrailCommand;
import io.github.jadedbay.Config.MapTrailConfig;
import io.github.jadedbay.Config.PlayerConfig;
import io.github.jadedbay.Config.PlayerConfigManager;
import io.github.jadedbay.PlayerTrail.PlayerTrailMarkerProvider;
import io.github.jadedbay.PlayerTrail.PlayerTrailTracker;

import javax.annotation.Nonnull;
import java.util.UUID;

public class MapTrailPlugin extends JavaPlugin {
    private static Config<MapTrailConfig> config;
    private static PlayerConfigManager playerConfig;

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public MapTrailPlugin(@Nonnull JavaPluginInit init) {
        super(init);
        config = this.withConfig("MapTrail", MapTrailConfig.CODEC);
        playerConfig = new PlayerConfigManager(this.getDataDirectory());
    }

    @Override
    protected void setup() {
        LOGGER.atInfo().log("Setting up plugin " + this.getName());

        this.getCommandRegistry().registerCommand(new MapTrailCommand());

        this.getEventRegistry().registerGlobal(AddWorldEvent.class, this::onWorldAdd);
        this.getEventRegistry().registerGlobal(PlayerDisconnectEvent.class, this::onPlayerDisconnect);
        this.getEventRegistry().registerGlobal(PlayerConnectEvent.class, this::onPlayerConnect);

        PacketAdapters.registerInbound(this::handleClientPacket);
    }

    private void onWorldAdd(AddWorldEvent event) {
        event.getWorld().getWorldMapManager().addMarkerProvider("playerPath", new PlayerTrailMarkerProvider());
    }

    private void onPlayerDisconnect(PlayerDisconnectEvent event) {
        PlayerTrailTracker.removePlayerPath(event.getPlayerRef().getUuid());
        playerConfig.unloadPlayerConfig(event.getPlayerRef().getUuid());
    }

    private void onPlayerConnect(PlayerConnectEvent event) {
        UUID playerId = event.getPlayerRef().getUuid();

        playerConfig.loadPlayerConfig(playerId).thenAccept(config -> {
            LOGGER.atInfo().log("Loaded player config: " + playerId);
        });
    }

    private void handleClientPacket(PacketHandler handler, Packet packet) {
        if (packet instanceof ClientMovement movementPacket) {
            PlayerAuthentication auth = handler.getAuth();
            if (auth == null) return;

            PlayerTrailTracker.checkAndCreateMarker(
                    handler.getAuth().getUuid(),
                    movementPacket.absolutePosition
            );
        }
    }

    public static Config<MapTrailConfig> getConfig() { return config; }
    public static Config<PlayerConfig> getPlayerConfig(UUID playerId) {
        return playerConfig.getConfig(playerId);
    }
}