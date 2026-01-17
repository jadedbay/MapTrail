package io.github.jadedbay.Commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.github.jadedbay.MapTrailPlugin;

import javax.annotation.Nonnull;

/**
 * This is an example command that will simply print the name of the plugin in chat when used.
 */
public class MapTrailCommand extends AbstractPlayerCommand {
    public MapTrailCommand(@Nonnull MapTrailPlugin plugin) {
        super("maptrail", "Configure map trail settings");

        this.addSubCommand(new MarkersSubCommand(plugin));
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {

    }
}

class MarkersSubCommand extends AbstractPlayerCommand {
    private final RequiredArg<Integer> valueArg;
    private final MapTrailPlugin plugin;

    public MarkersSubCommand(MapTrailPlugin plugin) {
        super("markers", "Set max number of trail markers displayed on map at once");
        this.plugin = plugin;
        this.valueArg = this.withRequiredArg("value", "Max number of markers", ArgTypes.INTEGER);
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        int value = commandContext.get(valueArg);

        if (value < 0) {
            playerRef.sendMessage(Message.raw("Value must not be negative").color("#ff5555"));
            return;
        }

        plugin.getConfig().get().setMaxMarkers(value);

        playerRef.sendMessage(Message.raw("Max markers set to: " + value).color("#55ff55"));
    }
}