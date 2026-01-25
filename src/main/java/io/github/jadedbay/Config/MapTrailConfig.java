package io.github.jadedbay.Config;

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
                    .append(new KeyedCodec<Double>("SizeSmallThreshold", Codec.DOUBLE),
                            (config, value) -> config.sizeSmallThreshold = value,
                            config -> config.sizeSmallThreshold)
                    .add()
                    .append(new KeyedCodec<Double>("SizeMediumThreshold", Codec.DOUBLE),
                            (config, value) -> config.sizeMediumThreshold = value,
                            config -> config.sizeMediumThreshold)
                    .add()
                    .build();

    private int maxMarkers = 80;
    private double distanceThreshold = 8.0f;
    private double sizeSmallThreshold = 0.125f;
    private double sizeMediumThreshold = 0.425f;

    public int getMaxMarkers() { return maxMarkers; }
    public void setMaxMarkers(int maxMarkers) {
        this.maxMarkers = maxMarkers;
        PlayerTrailTracker.updateMaxMarkers(maxMarkers);
    }

    public double getDistanceThreshold() { return distanceThreshold; }
    public void setDistanceThreshold(double distanceThreshold) { this.distanceThreshold = distanceThreshold; }

    public double getSizeSmallThreshold() { return sizeSmallThreshold; }
    public void setSizeSmallThreshold(double sizeSmallThreshold) { this.sizeSmallThreshold = sizeSmallThreshold; }

    public double getSizeMediumThreshold() { return sizeMediumThreshold; }
    public void setSizeMediumThreshold(double sizeMediumThreshold) { this.sizeMediumThreshold = sizeMediumThreshold; }
}
