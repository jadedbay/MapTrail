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
import io.github.jadedbay.MapTrailConfig;
import io.github.jadedbay.MapTrailPlugin;

import javax.annotation.Nonnull;
import java.awt.*;

public class MapTrailCommand extends AbstractPlayerCommand {
    public MapTrailCommand() {
        super("maptrail", "Configure map trail settings");

        this.addSubCommand(new ConfigSubCommand());
        this.addSubCommand(new MarkersSubCommand());
        this.addSubCommand(new DistanceSubCommand());
        this.addSubCommand(new SizeSubCommand());
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        playerRef.sendMessage(Message.raw("Usage: /maptrail <markers|distance|sizeratio>").color(Color.YELLOW));
    }
}

class MarkersSubCommand extends AbstractPlayerCommand {
    private final RequiredArg<Integer> valueArg;

    public MarkersSubCommand() {
        super("markers", "Set max number of trail markers displayed on map at once");
        this.valueArg = this.withRequiredArg("value", "Max number of markers", ArgTypes.INTEGER);
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        int value = Math.max(0, commandContext.get(valueArg));

        MapTrailPlugin.getConfig().get().setMaxMarkers(value);
        MapTrailPlugin.getConfig().save();

        playerRef.sendMessage(Message.raw("[MapTrail] Set Max Markers: " + value).color(Color.YELLOW));
    }
}

class DistanceSubCommand extends AbstractPlayerCommand {
    private final RequiredArg<Double> valueArg;

    public DistanceSubCommand() {
        super("distance", "Set distance between markers");
        this.valueArg = this.withRequiredArg("value", "Distance between markers", ArgTypes.DOUBLE);
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        double value = Math.max(0.1, commandContext.get(valueArg));

        MapTrailPlugin.getConfig().get().setDistanceThreshold(value);
        MapTrailPlugin.getConfig().save();

        playerRef.sendMessage(Message.raw("[MapTrail] Set Distance Threshold: " + value).color(Color.YELLOW));
    }
}

enum MarkerSize {
    SMALL,
    MEDIUM,
}

class SizeSubCommand extends AbstractPlayerCommand {
    private final RequiredArg<MarkerSize> sizeArg;
    private final RequiredArg<Double> valueArg;

    public SizeSubCommand() {
        super("size", "Set threshold between marker sizes");
        this.sizeArg = this.withRequiredArg("size", "Marker size to change", ArgTypes.forEnum("size", MarkerSize.class));
        this.valueArg = this.withRequiredArg("value", "Percentage of markers of size", ArgTypes.DOUBLE);
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        MarkerSize size = commandContext.get(sizeArg);
        double value = Math.max(0.0, Math.min(1.0, commandContext.get(valueArg)));

        MapTrailConfig config = MapTrailPlugin.getConfig().get();
        switch (size) {
            case SMALL:
                config.setSizeSmallThreshold(value);
                break;
            case MEDIUM:
                config.setSizeMediumThreshold(value);
                break;
        }
        MapTrailPlugin.getConfig().save();

        playerRef.sendMessage(Message.raw("[MapTrail] Set Size (" + size +"): " + value).color(Color.YELLOW));
    }
}

class ConfigSubCommand extends AbstractPlayerCommand {
    public ConfigSubCommand() {
        super("config", "View current config values");
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        MapTrailConfig config = MapTrailPlugin.getConfig().get();

        playerRef.sendMessage(Message.raw(
                "[MapTrail] Config Values: \n" +
                "   MaxMarkers = " + config.getMaxMarkers() + "\n" +
                "   DistanceThreshold = " + config.getDistanceThreshold() + "\n" +
                "   Size Thresholds:\n" +
                "       Small = " + config.getSizeSmallThreshold() + "\n" +
                "       Medium = " + config.getSizeMediumThreshold()
        ).color(Color.YELLOW));
    }
}
