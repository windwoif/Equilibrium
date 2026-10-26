package com.windwoif.equilibrium.dimension;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.windwoif.equilibrium.Equilibrium;
import com.windwoif.equilibrium.content.recipe.chemical.Chemical;
import com.windwoif.equilibrium.content.recipe.chemical.ChemicalRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 What the ****
 */
public class DimensionAtmosphere {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_DIR = Path.of("config", Equilibrium.MODID, "dimensions");
    private static final Map<ResourceKey<Level>, Atmosphere> ATMOSPHERES = new HashMap<>();

    private static Atmosphere defaultAtmosphere = null;

    private static final int TOTAL_MILLIMOLES = 40840;

    public static void init() {
        buildDefaultAtmosphere();
        loadConfigs();
        registerDefault(Level.OVERWORLD, defaultAtmosphere);
        registerDefault(Level.NETHER, createNetherAtmosphere());
        registerDefault(Level.END, createEndAtmosphere());
    }

    private static void buildDefaultAtmosphere() {
        HashMap<Chemical, Integer> composition = new HashMap<>();
        Chemical oxygen = getChemical("balance:oxygen");
        Chemical nitrogen = getChemical("balance:nitrogen");
        Chemical carbonDioxide = getChemical("balance:carbon_dioxide");

        if (oxygen != null) composition.put(oxygen, (int) (TOTAL_MILLIMOLES * 0.21));
        if (nitrogen != null) composition.put(nitrogen, (int) (TOTAL_MILLIMOLES * 0.78));
        if (carbonDioxide != null) composition.put(carbonDioxide, (int) (TOTAL_MILLIMOLES * 0.0004));
        // 剩余为氩气等忽略，简化处理

        defaultAtmosphere = new Atmosphere(298, composition);
        Equilibrium.LOGGER.info("Default atmosphere built: {}K, {} components", 298, composition.size());
    }

    private static Atmosphere createNetherAtmosphere() {
        return new Atmosphere(800, new HashMap<>());
    }

    private static Atmosphere createEndAtmosphere() {
        return new Atmosphere(50, new HashMap<>());
    }

    private static void registerDefault(ResourceKey<Level> dimension, Atmosphere atmosphere) {
        ATMOSPHERES.putIfAbsent(dimension, atmosphere);
    }

    private static Chemical getChemical(String id) {
        String key = id;
        if (key.contains(":")) {
            key = key.substring(key.indexOf(':') + 1);
        }
        Chemical chemical = ChemicalRegistry.get(key).orElse(null);
        if (chemical == null) {
            Equilibrium.LOGGER.warn("Unknown chemical: {}", id);
        }
        return chemical;
    }

    private static void loadConfigs() {
        try {
            Files.createDirectories(CONFIG_DIR);
        } catch (IOException e) {
            Equilibrium.LOGGER.error("Failed to create config directory", e);
            return;
        }

        try (var stream = Files.list(CONFIG_DIR)) {
            stream.filter(p -> p.toString().endsWith(".json"))
                    .forEach(DimensionAtmosphere::loadConfig);
        } catch (IOException e) {
            Equilibrium.LOGGER.error("Failed to list config files", e);
        }
    }

    private static void loadConfig(Path path) {
        try (Reader reader = Files.newBufferedReader(path)) {
            ConfigData data = GSON.fromJson(reader, ConfigData.class);
            ResourceKey<Level> dimension = parseDimensionKey(path.getFileName().toString());
            if (dimension == null) {
                Equilibrium.LOGGER.warn("Invalid dimension key from file: {}", path);
                return;
            }

            HashMap<Chemical, Integer> composition = new HashMap<>();
            for (Map.Entry<String, Integer> entry : data.atmosphere.entrySet()) {
                Chemical chemical = getChemical(entry.getKey());
                if (chemical != null) {
                    composition.put(chemical, entry.getValue());
                }
            }
            Atmosphere atmosphere = new Atmosphere(data.temperature, composition);
            ATMOSPHERES.put(dimension, atmosphere);
            Equilibrium.LOGGER.info("Loaded atmosphere for dimension {}: {}K, {} components",
                    dimension.location(), data.temperature, composition.size());
        } catch (Exception e) {
            Equilibrium.LOGGER.error("Failed to load config: {}", path, e);
        }
    }

    private static ResourceKey<Level> parseDimensionKey(String fileName) {
        if (!fileName.endsWith(".json")) return null;
        String id = fileName.substring(0, fileName.length() - 5);
        // 支持 "minecraft:overworld.json" 或 "overworld.json" 格式
        if (!id.contains(":")) {
            id = "minecraft:" + id;
        }
        try {
            return ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(id));
        } catch (Exception e) {
            return null;
        }
    }

    public static Atmosphere getAtmosphere(ResourceKey<Level> dimension) {
        // 确保默认大气已构建（如果 init 未被调用，则主动构建）
        if (defaultAtmosphere == null) {
            buildDefaultAtmosphere();
        }
        return ATMOSPHERES.getOrDefault(dimension, defaultAtmosphere);
    }

    public static Atmosphere getAtmosphere(Level level) {
        return getAtmosphere(level.dimension());
    }

    public static void reload() {
        ATMOSPHERES.clear();
        buildDefaultAtmosphere();
        loadConfigs();
        registerDefault(Level.OVERWORLD, defaultAtmosphere);
        registerDefault(Level.NETHER, createNetherAtmosphere());
        registerDefault(Level.END, createEndAtmosphere());
        Equilibrium.LOGGER.info("Dimension atmospheres reloaded");
    }

    private static class ConfigData {
        int temperature;
        Map<String, Integer> atmosphere;
    }
}