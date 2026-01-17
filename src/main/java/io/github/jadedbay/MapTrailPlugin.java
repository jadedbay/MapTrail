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
import com.hypixel.hytale.server.core.universe.world.events.AddWorldEvent;

import javax.annotation.Nonnull;

public class MapTrailPlugin extends JavaPlugin {

    static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public MapTrailPlugin(@Nonnull JavaPluginInit init) {super(init);}

    @Override
    protected void setup() {
        LOGGER.atInfo().log("Setting up plugin " + this.getName());

        this.getCommandRegistry().registerCommand(new ExampleCommand(this.getName(), this.getManifest().getVersion().toString()));

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

            PlayerTrailTracker.checkAndCreateMarker(handler.getAuth().getUuid(), movementPacket.absolutePosition);
        }
    }
}