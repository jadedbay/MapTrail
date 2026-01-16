package io.github.jadedbay;

import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.entity.entities.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerPathTracker {
    static class MarkerEntry {
        final Vector3d position;
        final long timestamp;

        MarkerEntry(Vector3d position) {
            this.position = position;
            this.timestamp = System.currentTimeMillis();
        }

        public String getMarkerId(Player player) {
            return "path_marker_" + player.getUuid() + "_" + timestamp;
        }
    }

    private static final Map<UUID, List<MarkerEntry>> markerPositions = new ConcurrentHashMap<>();
    private static final double DISTANCE_THRESHOLD = 10.0;
    private static final int MAX_MARKERS = 20;

    public static void checkAndCreateMarker(Player player, Vector3d currentPos) {
        UUID playerUuid = player.getUuid();
        List<MarkerEntry> playerMarkers = markerPositions.computeIfAbsent(playerUuid, k -> new ArrayList<>());

        if (playerMarkers.isEmpty()) {
            playerMarkers.add(new MarkerEntry(new Vector3d(currentPos)));
            return;
        }

        Vector3d lastPos = playerMarkers.getLast().position;

        double dx = currentPos.x - lastPos.x;
        double dz = currentPos.z - lastPos.z;
        if (dx * dx + dz * dz >= DISTANCE_THRESHOLD * DISTANCE_THRESHOLD) {
            playerMarkers.add(new MarkerEntry(new Vector3d(currentPos)));
            if (playerMarkers.size() > MAX_MARKERS) {
                playerMarkers.removeFirst();
            }
        }
    }

    public static List<MarkerEntry> getPlayerPath(UUID playerUuid) {
        return markerPositions.getOrDefault(playerUuid, new ArrayList<>());
    }

    public static void removePlayerPath(UUID playerUuid) {
        markerPositions.remove(playerUuid);
    }
}