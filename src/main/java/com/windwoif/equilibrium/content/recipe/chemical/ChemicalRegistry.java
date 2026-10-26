package com.windwoif.equilibrium.content.recipe.chemical;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class ChemicalRegistry {
    private static final Map<String, Chemical> staticRegistry = new HashMap<>();

    private static final Cache<String, Chemical> dynamicRegistry = CacheBuilder.newBuilder()
            .weakValues()
            .maximumSize(2000)
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .build();

    public static Chemical register(Chemical chemical) {
        staticRegistry.put(chemical.name(), chemical);
        return chemical;
    }

    public static Chemical registerDynamic(Chemical chemical) {
        dynamicRegistry.asMap().put(chemical.name(), chemical);
        return chemical;
    }

    public static Optional<Chemical> get(String key) {
        Chemical chemical = staticRegistry.get(key);
        if (chemical != null) return Optional.of(chemical);
        return Optional.ofNullable(dynamicRegistry.getIfPresent(key));
    }
}
