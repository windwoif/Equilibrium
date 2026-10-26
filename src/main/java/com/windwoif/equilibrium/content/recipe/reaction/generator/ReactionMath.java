package com.windwoif.equilibrium.content.recipe.reaction.generator;

import com.windwoif.equilibrium.content.recipe.chemical.Chemical;

import java.util.HashMap;
import java.util.Map;

public final class ReactionMath {
    private ReactionMath() {
    }

    public static int gcd(int a, int b) {
        while (b != 0) {
            int t = a % b;
            a = b;
            b = t;
        }
        return a;
    }

    public static int lcm(int a, int b) {
        return a / gcd(a, b) * b;
    }

    /**
     * Scales both sides by their factors, merges them and drops non-positive coefficients.
     */
    public static Map<Chemical, Integer> merge(Map<Chemical, Integer> first, int firstFactor,
                                               Map<Chemical, Integer> second, int secondFactor) {
        Map<Chemical, Integer> result = new HashMap<>();
        first.forEach((chemical, n) -> result.merge(chemical, n * firstFactor, Integer::sum));
        second.forEach((chemical, n) -> result.merge(chemical, n * secondFactor, Integer::sum));
        result.values().removeIf(n -> n <= 0);
        return Map.copyOf(result);
    }
}
