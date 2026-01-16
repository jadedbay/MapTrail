package io.github.jadedbay;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.events.AddWorldEvent;

import javax.annotation.Nonnull;

/**
 * This class serves as the entrypoint for your plugin. Use the setup method to register into game registries or add
 * event listeners.
 */
public class MapTrailPlugin extends JavaPlugin {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public MapTrailPlugin(@Nonnull JavaPluginInit init) {super(init);}

    @Override
    protected void setup() {
        LOGGER.atInfo().log("Setting up plugin " + this.getName());
        this.getCommandRegistry().registerCommand(new ExampleCommand(this.getName(), this.getManifest().getVersion().toString()));
        this.getEventRegistry().registerGlobal(AddWorldEvent.class, this::onWorldAdd);

    }

    private void onWorldAdd(AddWorldEvent event) {
        event.getWorld().getWorldMapManager().addMarkerProvider("playerPath", new PlayerPathMarkerProvider());
    }
}