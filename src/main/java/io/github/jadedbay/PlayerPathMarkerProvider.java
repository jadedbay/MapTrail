package io.github.jadedbay;

import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.protocol.packets.worldmap.MapMarker;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.asset.type.gameplay.GameplayConfig;
import com.hypixel.hytale.server.core.universe.world.WorldMapTracker;
import com.hypixel.hytale.server.core.universe.world.worldmap.WorldMapManager;
import com.hypixel.hytale.server.core.util.PositionUtil;

import javax.annotation.Nonnull;
import java.util.List;

public class PlayerPathMarkerProvider implements WorldMapManager.MarkerProvider {
    public static final PlayerPathMarkerProvider INSTANCE = new PlayerPathMarkerProvider();

    @Override
    public void update(@Nonnull World world, @Nonnull GameplayConfig gameplayConfig, @Nonnull WorldMapTracker tracker, int chunkViewRadius, int playerChunkX, int playerChunkZ) {
        Player player = tracker.getPlayer();
        PlayerRef playerRef = player.getPlayerRef();
        if (playerRef == null || !playerRef.isValid()) return;

        final Transform transform = playerRef.getTransform();
        final Vector3d currentPos = transform.getPosition();

        PlayerPathTracker.checkAndCreateMarker(player, currentPos);

        List<PlayerPathTracker.MarkerEntry> pathMarkers = PlayerPathTracker.getPlayerPath(player.getUuid());

        for (int i = 0; i < pathMarkers.size(); i++) {
            final PlayerPathTracker.MarkerEntry markerEntry = pathMarkers.get(i);

            tracker.trySendMarker(
                    chunkViewRadius,
                    playerChunkX,
                    playerChunkZ,
                    markerEntry.position,
                    0.0f,
                    markerEntry.getMarkerId(player),
                    "Path Point " + (i + 1),
                    playerRef,
                    (id, name, ref) -> {
                        Transform markerTransform = new Transform(markerEntry.position, Vector3f.ZERO);
                        return new MapMarker(
                                id,
                                name,
                                "Home.png",
                                PositionUtil.toTransformPacket(markerTransform),
                                null
                        );
                    }
            );
        }
    }
}