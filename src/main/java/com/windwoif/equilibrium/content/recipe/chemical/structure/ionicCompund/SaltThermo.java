package com.windwoif.equilibrium.content.recipe.chemical.structure.ionicCompund;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public final class SaltThermo {
    private SaltThermo() {}

    public record Data(int enthalpy, float entropy) {}

    private static final String FILE = "data/equilibrium/chemistry/salt_thermo.csv";
    private static final Map<String, Map<String, Data>> TABLE = new HashMap<>();

    public static final int DEFAULT_DISSOLUTION_ENTHALPY = 39900;
    public static final float DEFAULT_DISSOLUTION_ENTROPY = 50f;

    public static void load() {
        TABLE.clear();
        var stream = SaltThermo.class.getClassLoader().getResourceAsStream(FILE);
        if (stream == null) {
            throw new IllegalStateException("Missing resource: " + FILE);
        }
        try (BufferedReader r = new BufferedReader(new InputStreamReader(stream, java.nio.charset.StandardCharsets.UTF_8))) {
            String header = r.readLine();
            if (header == null) return;
            String[] cols = header.split(",", -1);
            String line;
            while ((line = r.readLine()) != null) {
                if (line.isBlank() || line.startsWith("#")) continue;
                String[] parts = line.split(",", -1);
                if (parts.length < 2) continue;
                String cation = parts[0].trim();
                if (cation.isEmpty()) continue;
                Map<String, Data> row = getRow(parts, cols);
                if (!row.isEmpty()) TABLE.put(cation, row);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load " + FILE, e);
        }
    }

    private static @NotNull Map<String, Data> getRow(String[] parts, String[] cols) {
        Map<String, Data> row = new HashMap<>();
        for (int i = 1; i < parts.length && i < cols.length; i++) {
            String cell = parts[i].trim();
            if (cell.isEmpty()) continue;
            String anion = cols[i].trim();
            if (anion.isEmpty()) continue;
            String[] pair = cell.split("\\|");
            if (pair.length != 2) continue;
            try {
                row.put(anion, new Data(
                        Integer.parseInt(pair[0].trim()),
                        Float.parseFloat(pair[1].trim())));
            } catch (NumberFormatException ignored) {
            }
        }
        return row;
    }

    public static Data get(String cation, String anion) {
        Map<String, Data> row = TABLE.get(cation);
        return row == null ? null : row.get(anion);
    }
}