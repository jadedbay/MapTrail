package io.github.jadedbay;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import io.github.jadedbay.PlayerTrail.PlayerTrailTracker;

public class MapTrailConfig {
    public static final BuilderCodec<MapTrailConfig> CODEC =
            BuilderCodec.builder(MapTrailConfig.class, MapTrailConfig::new)
                    .append(new KeyedCodec<>("MaxMarkers", Codec.INTEGER),
                            (config, value) -> config.maxMarkers = value,
                            config -> config.maxMarkers)
                    .add()
                    .append(new KeyedCodec<Double>("DistanceThreshold", Codec.DOUBLE),
                            (config, value) -> config.distanceThreshold = value,
                            config -> config.distanceThreshold)
                    .add()
                    .append(new KeyedCodec<Double>("SmallThreshold", Codec.DOUBLE),
                            (config, value) -> config.distanceThreshold = value,
                            config -> config.distanceThreshold)
                    .add()
                    .append(new KeyedCodec<Double>("MediumThreshold", Codec.DOUBLE),
                            (config, value) -> config.distanceThreshold = value,
                            config -> config.distanceThreshold)
                    .add()
                    .build();

    private int maxMarkers = 80;
    private double distanceThreshold = 8.0f;
    private double smallThreshold = 0.15f;
    private double mediumThreshold = 0.45f;

    public int getMaxMarkers() { return maxMarkers; }
    public void setMaxMarkers(int maxMarkers) {
        this.maxMarkers = maxMarkers;
        PlayerTrailTracker.updateMaxMarkers(maxMarkers);
    }

    public double getDistanceThreshold() { return distanceThreshold; }
    public void setDistanceThreshold(double distanceThreshold) { this.distanceThreshold = distanceThreshold; }
}
