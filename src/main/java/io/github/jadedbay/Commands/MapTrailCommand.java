package io.github.jadedbay.Commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.FlagArg;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.Config;
import io.github.jadedbay.Config.PlayerConfig;
import io.github.jadedbay.MapTrailPlugin;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.UUID;

public class MapTrailCommand extends AbstractPlayerCommand {
    private final FlagArg defaultFlag;

    public MapTrailCommand() {
        super("maptrail", "Configure map trail settings");

        this.defaultFlag = this.withFlagArg("default", "Configure default map trail settings");

        this.addSubCommand(new ConfigSubCommand());

        this.addSubCommand(new EnableSubCommand());
        this.addSubCommand(new DisableSubCommand());

        this.addSubCommand(new MarkersSubCommand());
        this.addSubCommand(new DistanceSubCommand());
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        playerRef.sendMessage(Message.raw("Usage: /maptrail <markers|distance>").color(Color.YELLOW));
    }

    public FlagArg getDefaultFlag() { return defaultFlag; }

    public static Config<PlayerConfig> getConfig(boolean isDefault, UUID playerId) {
        return isDefault
                ? MapTrailPlugin.getDefaultPlayerConfig()
                : MapTrailPlugin.getPlayerConfig(playerId);
    }
}

abstract class MapTrailConfigCommand extends AbstractPlayerCommand {
    protected final FlagArg defaultFlag;

    public MapTrailConfigCommand(String name, String description) {
        super(name, description);
        this.defaultFlag = this.withFlagArg("default", "Configure map trail settings");
    }

    protected Config<PlayerConfig> getConfig(CommandContext context, UUID playerId) {
        return context.get(defaultFlag)
                ? MapTrailPlugin.getDefaultPlayerConfig()
                : MapTrailPlugin.getPlayerConfig(playerId);
    }

    protected void sendMessage(PlayerRef playerRef, CommandContext context, String message) {
        String target = context.get(defaultFlag) ? "Default" : "Player";
        playerRef.sendMessage(Message.raw("[MapTrail] (" + target + ") " + message).color(Color.YELLOW));
    }
}

class EnableSubCommand extends MapTrailConfigCommand {
    public EnableSubCommand() {
        super("enable", "Enable the map trail markers");
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        Config<PlayerConfig> config = getConfig(commandContext, playerRef.getUuid());
        config.get().setEnabled(true);
        config.save();

        sendMessage(playerRef, commandContext, "Map trail: Enabled");
    }
}

class DisableSubCommand extends MapTrailConfigCommand {
    public DisableSubCommand() {
        super("disable", "Disable the map trail markers");
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        Config<PlayerConfig> config = getConfig(commandContext, playerRef.getUuid());
        config.get().setEnabled(false);
        config.save();

        sendMessage(playerRef, commandContext, "Map trail: Disabled");
    }
}

class MarkersSubCommand extends MapTrailConfigCommand {
    private final RequiredArg<Integer> valueArg;

    public MarkersSubCommand() {
        super("markers", "Set max number of trail markers displayed on map at once");
        this.valueArg = this.withRequiredArg("value", "Max number of markers", ArgTypes.INTEGER);
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        int value = Math.max(0, commandContext.get(valueArg));

        Config<PlayerConfig> config = getConfig(commandContext, playerRef.getUuid());
        config.get().setMarkerCount(value);
        config.save();

        sendMessage(playerRef, commandContext, "Set marker count: " + value);
    }
}

class DistanceSubCommand extends MapTrailConfigCommand {
    private final RequiredArg<Double> valueArg;

    public DistanceSubCommand() {
        super("distance", "Set distance between markers");
        this.valueArg = this.withRequiredArg("value", "Distance between markers", ArgTypes.DOUBLE);
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        double value = Math.max(0.1, commandContext.get(valueArg));

        Config<PlayerConfig> config = getConfig(commandContext, playerRef.getUuid());
        config.get().setDistanceThreshold(value);
        config.save();

        sendMessage(playerRef, commandContext, "Set distance threshold: " + value);
    }
}

class ConfigSubCommand extends MapTrailConfigCommand {

    public ConfigSubCommand() {
        super("config", "View current config values");
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        PlayerConfig config = getConfig(commandContext, playerRef.getUuid()).get();

        sendMessage(playerRef, commandContext, "Config Values: \n" +
                "   Enabled = " + config.getEnabled() + "\n" +
                "   MarkerCount = " + config.getMarkerCount() + "\n" +
                "   DistanceThreshold = " + config.getDistanceThreshold()
        );
    }
}
