package io.github.jadedbay.PlayerTrail;

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

    public static void checkAndCreateMarker(UUID playerUuid, Position currentPos, int maxMarkers, double distanceThreshold) {
        if (currentPos == null) return;

        List<MarkerEntry> playerMarkers = markers.computeIfAbsent(playerUuid, _ -> new ArrayList<>());

        if (playerMarkers.isEmpty() || reachedDistanceThreshold(currentPos, playerMarkers.getLast().position, distanceThreshold)) {
            playerMarkers.add(new MarkerEntry(currentPos));
            if (playerMarkers.size() > maxMarkers) {
                playerMarkers.removeFirst();
            }
        }
    }

    private static boolean reachedDistanceThreshold(Position currentPos, Position lastPos, double distanceThreshold) {
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
}