package io.github.jadedbay;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class MapTrailConfig {
    public static final BuilderCodec<MapTrailConfig> CODEC =
            BuilderCodec.builder(MapTrailConfig.class, MapTrailConfig::new)
                    .append(new KeyedCodec<>("MaxMarkers", Codec.INTEGER),
                            (config, value) -> config.maxMarkers = value,
                            config -> config.maxMarkers)
                    .add()
                    .build();

    // Max amount of markers for each player shown at once
    private int maxMarkers = 100;

    public int getMaxMarkers() { return maxMarkers; }
}
