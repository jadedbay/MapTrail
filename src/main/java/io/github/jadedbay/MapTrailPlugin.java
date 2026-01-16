package io.github.jadedbay;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.protocol.Packet;
import com.hypixel.hytale.protocol.packets.player.ClientMovement;
import com.hypixel.hytale.server.core.event.events.player.DrainPlayerFromWorldEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.io.PacketHandler;
import com.hypixel.hytale.server.core.io.adapter.PacketAdapters;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.events.AddWorldEvent;

import javax.annotation.Nonnull;

/**
 * This class serves as the entrypoint for your plugin. Use the setup method to register into game registries or add
 * event listeners.
 */
public class MapTrailPlugin extends JavaPlugin {

    static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public MapTrailPlugin(@Nonnull JavaPluginInit init) {super(init);}

    @Override
    protected void setup() {
        LOGGER.atInfo().log("Setting up plugin " + this.getName());
        this.getCommandRegistry().registerCommand(new ExampleCommand(this.getName(), this.getManifest().getVersion().toString()));
        this.getEventRegistry().registerGlobal(AddWorldEvent.class, this::onWorldAdd);
        this.getEventRegistry().registerGlobal(PlayerDisconnectEvent.class, this::onPlayerDisconnect);

        PacketAdapters.registerInbound((PacketHandler handler, Packet packet) -> {
            if (packet instanceof ClientMovement movementPacket) {
                PlayerPathTracker.checkAndCreateMarker(handler.getAuth().getUuid(), movementPacket.absolutePosition);
            }
        });

    }

    private void onWorldAdd(AddWorldEvent event) {
        event.getWorld().getWorldMapManager().addMarkerProvider("playerPath", new PlayerPathMarkerProvider());
    }

    private void onPlayerDisconnect(PlayerDisconnectEvent event) {
        PlayerPathTracker.removePlayerPath(event.getPlayerRef().getUuid());
    }
}