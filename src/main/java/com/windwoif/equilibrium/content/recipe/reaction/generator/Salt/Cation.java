package com.windwoif.equilibrium.content.recipe.reaction.generator.Salt;

import com.windwoif.equilibrium.content.recipe.chemical.structure.ion.Ion;
import com.windwoif.equilibrium.content.recipe.reaction.generator.Feature;

public class Cation implements Feature {
    private final Ion ion;
    public final static String TYPE = "Cation";

    public Cation(Ion cation) {
        if (cation.getCharge() < 1) throw new IllegalArgumentException("Anion charge cannot be negative");
        this.ion = cation;
    }

    public Ion getIon() {
        return ion;
    }

    @Override
    public String type() {
        return TYPE;
    }
}
