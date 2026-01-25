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
import com.hypixel.hytale.server.core.util.Config;
import io.github.jadedbay.Config.MapTrailConfig;
import io.github.jadedbay.Config.PlayerConfig;
import io.github.jadedbay.MapTrailPlugin;

import javax.annotation.Nonnull;
import java.awt.*;

public class MapTrailCommand extends AbstractPlayerCommand {
    public MapTrailCommand() {
        super("maptrail", "Configure map trail settings");

        this.addSubCommand(new ConfigSubCommand());

        this.addSubCommand(new EnableSubCommand());
        this.addSubCommand(new DisableSubCommand());

        this.addSubCommand(new MarkersSubCommand());
        this.addSubCommand(new DistanceSubCommand());
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        playerRef.sendMessage(Message.raw("Usage: /maptrail <enable|disable|markers|distance>").color(Color.YELLOW));
    }
}

class EnableSubCommand extends AbstractPlayerCommand {
    public EnableSubCommand() {
        super("enable", "Enable the map trail markers");
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        Config<PlayerConfig> config = MapTrailPlugin.getPlayerConfig(playerRef.getUuid());
        config.get().setEnabled(true);
        config.save();

        playerRef.sendMessage(Message.raw("[MapTrail] Player map trail: Enabled").color(Color.YELLOW));
    }
}

class DisableSubCommand extends AbstractPlayerCommand {
    public DisableSubCommand() {
        super("disable", "Disable the map trail markers");
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        Config<PlayerConfig> config = MapTrailPlugin.getPlayerConfig(playerRef.getUuid());
        config.get().setEnabled(false);
        config.save();

        playerRef.sendMessage(Message.raw("[MapTrail] Player map trail: Disable").color(Color.YELLOW));
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

        Config<PlayerConfig> config = MapTrailPlugin.getPlayerConfig(playerRef.getUuid());
        config.get().setMarkerCount(value);
        config.save();

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

        Config<PlayerConfig> config = MapTrailPlugin.getPlayerConfig(playerRef.getUuid());
        config.get().setDistanceThreshold(value);
        config.save();

        playerRef.sendMessage(Message.raw("[MapTrail] Set Distance Threshold: " + value).color(Color.YELLOW));
    }
}

class ConfigSubCommand extends AbstractPlayerCommand {
    public ConfigSubCommand() {
        super("config", "View current config values");
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        PlayerConfig config = MapTrailPlugin.getPlayerConfig(playerRef.getUuid()).get();

        playerRef.sendMessage(Message.raw(
                "[MapTrail] Config Values: \n" +
                "   Enabled = " + config.getEnabled() + "\n" +
                "   MarkerCount = " + config.getMarkerCount() + "\n" +
                "   DistanceThreshold = " + config.getDistanceThreshold() + "\n"
        ).color(Color.YELLOW));
    }
}
