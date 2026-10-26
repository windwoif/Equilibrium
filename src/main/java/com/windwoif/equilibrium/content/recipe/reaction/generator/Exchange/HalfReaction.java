package com.windwoif.equilibrium.content.recipe.reaction.generator.Exchange;

import com.windwoif.equilibrium.content.recipe.chemical.Chemical;
import com.windwoif.equilibrium.content.recipe.chemical.structure.atom.Atom;
import com.windwoif.equilibrium.content.recipe.reaction.generator.Feature;

import java.util.Map;

/**
 * A half reaction written in the direction that releases the exchanged atom; the builder reverses
 * the partner when pairing.
 */
public record HalfReaction(Map<Chemical, Integer> consumes, Map<Chemical, Integer> produces, Atom atom)
        implements Feature {
    public static final String PREFIX = "Exchange:";

    @Override
    public String type() {
        return PREFIX + atom.getSymbol();
    }

    public int released() {
        return atomCount(consumes) - atomCount(produces);
    }

    private int atomCount(Map<Chemical, Integer> side) {
        return side.entrySet().stream()
                .mapToInt(entry -> entry.getKey().structure().getAtoms().getOrDefault(atom, 0) * entry.getValue())
                .sum();
    }
}
