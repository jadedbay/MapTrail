package io.github.jadedbay;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.protocol.Packet;
import com.hypixel.hytale.protocol.packets.player.ClientMovement;
import com.hypixel.hytale.server.core.auth.PlayerAuthentication;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.io.PacketHandler;
import com.hypixel.hytale.server.core.io.adapter.PacketAdapters;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.events.AddWorldEvent;
import com.hypixel.hytale.server.core.util.Config;
import io.github.jadedbay.Commands.MapTrailCommand;
import io.github.jadedbay.PlayerTrail.PlayerTrailMarkerProvider;
import io.github.jadedbay.PlayerTrail.PlayerTrailTracker;

import javax.annotation.Nonnull;

public class MapTrailPlugin extends JavaPlugin {
    private static Config<MapTrailConfig> config;

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public MapTrailPlugin(@Nonnull JavaPluginInit init) {
        super(init);
        config = this.withConfig("MapTrail", MapTrailConfig.CODEC);
    }

    @Override
    protected void setup() {
        LOGGER.atInfo().log("Setting up plugin " + this.getName());

        this.getCommandRegistry().registerCommand(new MapTrailCommand());

        this.getEventRegistry().registerGlobal(AddWorldEvent.class, this::onWorldAdd);
        this.getEventRegistry().registerGlobal(PlayerDisconnectEvent.class, this::onPlayerDisconnect);

        PacketAdapters.registerInbound(this::handleClientPacket);
    }

    private void onWorldAdd(AddWorldEvent event) {
        event.getWorld().getWorldMapManager().addMarkerProvider("playerPath", new PlayerTrailMarkerProvider());
    }

    private void onPlayerDisconnect(PlayerDisconnectEvent event) {
        PlayerTrailTracker.removePlayerPath(event.getPlayerRef().getUuid());
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

    public static Config<MapTrailConfig> getConfig() {
        return config;
    }

    public static HytaleLogger logger() {
        return LOGGER;
    }
}