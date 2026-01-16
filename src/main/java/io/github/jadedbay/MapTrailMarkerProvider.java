package io.github.jadedbay;

import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.protocol.packets.worldmap.MapMarker;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.asset.type.gameplay.GameplayConfig;
import com.hypixel.hytale.server.core.universe.world.WorldMapTracker;
import com.hypixel.hytale.server.core.universe.world.worldmap.WorldMapManager;
import com.hypixel.hytale.server.core.util.PositionUtil;

import javax.annotation.Nonnull;

public class MapTrailMarkerProvider implements WorldMapManager.MarkerProvider {
    public static final MapTrailMarkerProvider INSTANCE = new MapTrailMarkerProvider();

    @Override
    public void update(@Nonnull World world, @Nonnull GameplayConfig gameplayConfig, @Nonnull WorldMapTracker tracker, int chunkViewRadius, int playerChunkX, int playerChunkZ) {
        Player player = tracker.getPlayer();
        Vector3d position = player.getPlayerRef().getTransform().getPosition();

        tracker.trySendMarker(
            chunkViewRadius,
            playerChunkX,
            playerChunkZ,
            new MapMarker(
                "maptrail_marker",
                "MapTrail",
                "Home.png",
                PositionUtil.toTransformPacket(new Transform(position)),
                null
            )
        );
    }
}
