package com.windwoif.equilibrium.content.recipe.reaction.generator.Salt;

import com.windwoif.equilibrium.content.recipe.chemical.structure.ion.Ion;
import com.windwoif.equilibrium.content.recipe.reaction.generator.Feature;

public class Anion implements Feature {
    private final Ion ion;
    public final static String TYPE = "Anion";

    public Anion(Ion anion) {
        if (anion.getCharge() > -1) throw new IllegalArgumentException("Anion charge cannot be positive");
        this.ion = anion;
    }

    public Ion getIon() {
        return ion;
    }

    @Override
    public String type()  {
        return TYPE;
    }
}
