package io.github.jadedbay.PlayerTrail;

import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.protocol.Position;
import com.hypixel.hytale.protocol.packets.worldmap.MapMarker;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.world.worldmap.markers.MapMarkerBuilder;
import io.github.jadedbay.MapTrailConfig;
import io.github.jadedbay.MapTrailPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerTrailTracker {
    public static class MarkerEntry {
        final Position position;
        final long timestamp;

        MarkerEntry(Position position) {
            this.position = position;
            this.timestamp = System.currentTimeMillis();
        }

        public String getMarkerId(UUID playerUuid) {
            return "trail_marker_" + playerUuid + "_" + timestamp;
        }

        public MapMarker createMarker(Player player, String markerTexture) {
            return new MapMarkerBuilder(
                    getMarkerId(player.getUuid()) + "_" + markerTexture,
                    markerTexture,
                    new Transform(new Vector3d(position.x, position.y, position.z))
            ).build();
        }
    }

    private static final Map<UUID, List<MarkerEntry>> markers = new ConcurrentHashMap<>();

    public static void checkAndCreateMarker(UUID playerUuid, Position currentPos) {
        if (currentPos == null) return;

        List<MarkerEntry> playerMarkers = markers.computeIfAbsent(playerUuid, _ -> new ArrayList<>());

        if (playerMarkers.isEmpty() || reachedDistanceThreshold(currentPos, playerMarkers.getLast().position)) {
            playerMarkers.add(new MarkerEntry(currentPos));
            while (playerMarkers.size() > MapTrailPlugin.getConfig().get().getMaxMarkers()) {
                playerMarkers.removeFirst();
            }
        }
    }

    private static boolean reachedDistanceThreshold(Position currentPos, Position lastPos) {
        double distanceThreshold = MapTrailPlugin.getConfig().get().getDistanceThreshold();

        double dx = currentPos.x - lastPos.x;
        double dz = currentPos.z - lastPos.z;
        return dx * dx + dz * dz >= distanceThreshold * distanceThreshold;
    }

    public static List<MarkerEntry> getPlayerMarkers(UUID playerUuid) {
        return markers.getOrDefault(playerUuid, new ArrayList<>());
    }

    public static void removePlayerPath(UUID playerUuid) {
        markers.remove(playerUuid);
    }

    public static void updateMaxMarkers(int newMaxMarkers) {
        for (List<MarkerEntry> playerMarkers : markers.values()) {
            while (playerMarkers.size() > newMaxMarkers) {
                playerMarkers.removeFirst();
            }
        }
    }

    private static String getMarkerTexture(int index, int markerCount) {
        float percentage = (float)(index + 1) / markerCount;

        MapTrailConfig config = MapTrailPlugin.getConfig().get();
        if (percentage < config.getSizeSmallThreshold()) return "MapTrail_4.png";
        if (percentage < config.getSizeMediumThreshold()) return "MapTrail_5.png";
        return "MapTrail_6.png";
    }
}