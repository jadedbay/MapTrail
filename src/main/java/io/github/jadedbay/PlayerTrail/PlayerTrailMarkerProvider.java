package io.github.jadedbay.PlayerTrail;

import com.hypixel.hytale.protocol.Direction;
import com.hypixel.hytale.protocol.Transform;
import com.hypixel.hytale.protocol.packets.worldmap.MapMarker;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.asset.type.gameplay.GameplayConfig;
import com.hypixel.hytale.server.core.universe.world.WorldMapTracker;
import com.hypixel.hytale.server.core.universe.world.worldmap.WorldMapManager;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

public class PlayerTrailMarkerProvider implements WorldMapManager.MarkerProvider {
    public static final PlayerTrailMarkerProvider INSTANCE = new PlayerTrailMarkerProvider();

    @Override
    public void update(@Nonnull World world, @Nonnull GameplayConfig gameplayConfig, @Nonnull WorldMapTracker tracker, int chunkViewRadius, int playerChunkX, int playerChunkZ) {
        if (!tracker.shouldUpdatePlayerMarkers()) return;

        Player player = tracker.getPlayer();
        UUID playerUuid = player.getUuid();

        List<PlayerTrailTracker.MarkerEntry> playerMarkers = PlayerTrailTracker.getPlayerMarkers(playerUuid);
        for (int i = 0; i < playerMarkers.size(); i++) {
            final PlayerTrailTracker.MarkerEntry markerEntry = playerMarkers.get(i);
            final String markerTexture = getMarkerTexture(i, playerMarkers.size());

            final MapMarker marker = new MapMarker(
                    markerEntry.getMarkerId(playerUuid) + "_" + markerTexture,
                    "",
                    markerTexture,
                    new Transform(markerEntry.position, new Direction()),
                    null
            );

            tracker.trySendMarker(
                    chunkViewRadius,
                    playerChunkX,
                    playerChunkZ,
                    marker
            );
        }
    }

    private static String getMarkerTexture(int index, int markerCount) {
        float percentage = (float)(index + 1) / markerCount;

        if (percentage < 0.15f) return "MapTrail_4.png";
        if (percentage < 0.35f) return "MapTrail_5.png";
        return "MapTrail_6.png";
    }
}