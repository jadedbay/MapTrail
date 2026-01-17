package io.github.jadedbay.Commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.github.jadedbay.MapTrailPlugin;

import javax.annotation.Nonnull;

public class MapTrailCommand extends AbstractPlayerCommand {
    public MapTrailCommand(@Nonnull MapTrailPlugin plugin) {
        super("maptrail", "Configure map trail settings");

        this.addSubCommand(new MarkersSubCommand(plugin));
        this.addSubCommand(new DistanceSubCommand(plugin));
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
        if (value <= 0) {
            playerRef.sendMessage(Message.raw("[MapTrail] Value must not be negative").color("#ff5555"));
            return;
        }

        plugin.getConfig().get().setMaxMarkers(value);
        plugin.getConfig().save();
        playerRef.sendMessage(Message.raw("[MapTrail] Set Max Markers: " + value).color("#55ff55"));
    }
}

class DistanceSubCommand extends AbstractPlayerCommand {
    private final RequiredArg<Double> valueArg;
    private final MapTrailPlugin plugin;

    public DistanceSubCommand(MapTrailPlugin plugin) {
        super("distance", "Set distance between markers");
        this.plugin = plugin;
        this.valueArg = this.withRequiredArg("value", "Distance between markers", ArgTypes.DOUBLE);
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        double value = commandContext.get(valueArg);

        if (value <= 0) {
            playerRef.sendMessage(Message.raw("[MapTrail] Value must be above 0").color("#ff5555"));
            return;
        }

        plugin.getConfig().get().setDistanceThreshold(value);
        plugin.getConfig().save();
        playerRef.sendMessage(Message.raw("[MapTrail] Set Distance Threshold: " + value).color("#55ff55"));
    }
}
