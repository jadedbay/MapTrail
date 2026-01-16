package io.github.jadedbay;

import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.entity.entities.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerPathTracker {
    private static final Map<UUID, List<Vector3d>> markerPositions = new ConcurrentHashMap<>();
    private static final double DISTANCE_THRESHOLD = 5.0;

    public static void checkAndCreateMatker(Player player, Vector3d currentPos) {
        UUID playerUuid = player.getUuid();
        List<Vector3d> playerMarkers = markerPositions.computeIfAbsent(playerUuid, k -> new ArrayList<>());

        if (playerMarkers.isEmpty()) {
            playerMarkers.add(new Vector3d(currentPos));
            return;
        }

        Vector3d lastPos = markerPositions.get(playerUuid).getLast();
        if (currentPos.distanceTo(lastPos) >= DISTANCE_THRESHOLD) {
            playerMarkers.add(new Vector3d(currentPos));
        }
    }

    public static List<Vector3d> getPlayerPath(UUID playerUuid) {
        return markerPositions.getOrDefault(playerUuid, new ArrayList<>());
    }
}