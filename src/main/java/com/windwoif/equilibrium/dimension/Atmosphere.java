package com.windwoif.equilibrium.dimension;

import com.windwoif.equilibrium.content.recipe.chemical.Chemical;

import java.util.HashMap;
import java.util.Map;

public record Atmosphere(int temperature, HashMap<Chemical, Integer> atmosphere, double pressure) {

    public Atmosphere(int temperature, HashMap<Chemical, Integer> atmosphere) {
        this(temperature, atmosphere, calculatePressure(temperature, atmosphere));
    }

    private static double calculatePressure(int temperature, Map<Chemical, Integer> atmosphere) {
        if (atmosphere == null || atmosphere.isEmpty()) {
            return 0.0;
        }
        double totalMoles = atmosphere.values().stream()
                .mapToLong(Long::valueOf)
                .sum() / 1000.0;
        return totalMoles * 8.314 * temperature;
    }

    public double getPressure() {
        return pressure;
    }
}