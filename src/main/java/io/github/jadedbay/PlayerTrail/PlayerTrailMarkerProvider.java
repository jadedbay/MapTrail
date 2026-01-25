package io.github.jadedbay.PlayerTrail;

import com.hypixel.hytale.protocol.Direction;
import com.hypixel.hytale.protocol.Transform;
import com.hypixel.hytale.protocol.packets.worldmap.MapMarker;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.WorldMapTracker;
import com.hypixel.hytale.server.core.universe.world.worldmap.WorldMapManager;
import com.hypixel.hytale.server.core.universe.world.worldmap.markers.MapMarkerTracker;
import io.github.jadedbay.MapTrailPlugin;
import io.github.jadedbay.Util.ReflectionUtil;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

public class PlayerTrailMarkerProvider implements WorldMapManager.MarkerProvider {
    public static final PlayerTrailMarkerProvider INSTANCE = new PlayerTrailMarkerProvider();

    @Override
    public void update(@Nonnull World world, @Nonnull MapMarkerTracker mapMarkerTracker, int chunkViewRadius, int playerChunkX, int playerChunkZ) {
        WorldMapTracker worldMapTracker = ReflectionUtil.getPrivateField(mapMarkerTracker, "worldMapTracker", WorldMapTracker.class, MapMarkerTracker.class);
        if (Boolean.FALSE.equals(ReflectionUtil.getPrivateField(worldMapTracker, "clientHasWorldMapVisible", Boolean.class, WorldMapTracker.class))) return;

        Player player = mapMarkerTracker.getPlayer();
        UUID playerUuid = player.getUuid();

        List<PlayerTrailTracker.MarkerEntry> playerMarkers = PlayerTrailTracker.getPlayerMarkers(playerUuid);
        for (int i = 0; i < playerMarkers.size(); i++) {
            final PlayerTrailTracker.MarkerEntry markerEntry = playerMarkers.get(i);
//            final String markerTexture = getMarkerTextureLength(i, playerMarkers.size());
            final String markerTexture = getMarkerTextureHeight(markerEntry.position.y);

            final MapMarker marker = new MapMarker(
                    markerEntry.getMarkerId(playerUuid) + "_" + markerTexture,
                    "",
                    markerTexture,
                    new Transform(markerEntry.position, new Direction()),
                    null
            );

            mapMarkerTracker.trySendMarker(
                    chunkViewRadius,
                    playerChunkX,
                    playerChunkZ,
                    marker
            );
        }
    }

//    private static String getMarkerTextureLength(int index, int markerCount) {
//        float percentage = (float)(index + 1) / markerCount;
//
//        MapTrailConfig config = MapTrailPlugin.getConfig().get();
//        if (percentage < config.getSizeSmallThreshold()) return "MapTrail_4.png";
//        if (percentage < config.getSizeMediumThreshold()) return "MapTrail_5.png";
//        return "MapTrail_6.png";
//    }

    private static String getMarkerTextureHeight(double y) {
        if (y < 115) return "MapTrail_4.png";
        if (y < 130) return "MapTrail_5.png";
        return "MapTrail_6.png";
    }
}