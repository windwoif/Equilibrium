package com.windwoif.equilibrium.content.recipe.chemical.structure;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class StructureRegistry {
    private static final Map<String, Structure<?>> staticRegistry = new HashMap<>();

    private static final Cache<String, Structure<?>> dynamicRegistry = CacheBuilder.newBuilder()
            .weakValues()
            .maximumSize(2000)
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .build();

    public static Structure<?> register(String name, Structure<?> structure) {
        staticRegistry.put(name, structure);
        return structure;
    }

    public static Structure<?> registerDynamic(String name, Structure<?> structure) {
        dynamicRegistry.asMap().put(name, structure);
        return structure;
    }

    public static Optional<Structure<?>> get(String name) {
        Structure<?> structure = staticRegistry.get(name);
        if (structure != null) return Optional.of(structure);
        return Optional.ofNullable(dynamicRegistry.getIfPresent(name));
    }
}
