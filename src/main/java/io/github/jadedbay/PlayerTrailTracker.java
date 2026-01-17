package io.github.jadedbay;

import com.hypixel.hytale.protocol.Position;

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
    }

    private static final Map<UUID, List<MarkerEntry>> markers = new ConcurrentHashMap<>();
    private static final double DISTANCE_THRESHOLD = 10.0;
    private static final int MAX_MARKERS = 20;

    public static void checkAndCreateMarker(UUID playerUuid, Position currentPos) {
        if (currentPos == null) return;

        List<MarkerEntry> playerMarkers = markers.computeIfAbsent(playerUuid, _ -> new ArrayList<>());

        if (playerMarkers.isEmpty() || reachedDistanceThreshold(currentPos, playerMarkers.getLast().position)) {
            playerMarkers.add(new MarkerEntry(currentPos));
            if (playerMarkers.size() > MAX_MARKERS) {
                playerMarkers.removeFirst();
            }
        }
    }

    private static boolean reachedDistanceThreshold(Position currentPos, Position lastPos) {
        double dx = currentPos.x - lastPos.x;
        double dz = currentPos.z - lastPos.z;
        return dx * dx + dz * dz >= DISTANCE_THRESHOLD * DISTANCE_THRESHOLD;
    }

    public static List<MarkerEntry> getPlayerMarkers(UUID playerUuid) {
        return markers.getOrDefault(playerUuid, new ArrayList<>());
    }

    public static void removePlayerPath(UUID playerUuid) {
        markers.remove(playerUuid);
    }
}