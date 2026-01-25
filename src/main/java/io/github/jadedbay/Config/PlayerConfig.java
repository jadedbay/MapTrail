package io.github.jadedbay.Config;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class PlayerConfig {
    public static final BuilderCodec<PlayerConfig> CODEC =
            BuilderCodec.builder(PlayerConfig.class, PlayerConfig::new)
                    .append(new KeyedCodec<>("Enabled", Codec.BOOLEAN),
                            (config,  value) -> config.enabled = value,
                            config -> config.enabled).add()
                    .append(new KeyedCodec<>("MarkerCount", Codec.INTEGER),
                            (config,  value) -> config.markerCount = value,
                            config -> config.markerCount).add()
                    .append(new KeyedCodec<>("DistanceThreshold", Codec.DOUBLE),
                            (config,  value) -> config.distanceThreshold = value,
                            config -> config.distanceThreshold).add()
                    .build();


    private boolean enabled = true;
    private int markerCount = 80;
    private double distanceThreshold = 8.0;

    public boolean getEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public int getMarkerCount() { return markerCount; }
    public void setMarkerCount(int markerCount) { this.markerCount = markerCount; }

    public double getDistanceThreshold() { return distanceThreshold; }
    public void setDistanceThreshold(double distanceThreshold) { this.distanceThreshold = distanceThreshold; }
}
