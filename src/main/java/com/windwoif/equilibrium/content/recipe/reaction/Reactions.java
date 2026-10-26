package com.windwoif.equilibrium.content.recipe.reaction;

import java.util.Map;

import static com.windwoif.equilibrium.content.recipe.chemical.Oxides.*;
import static com.windwoif.equilibrium.content.recipe.reaction.Reaction.State.SURFACE;

public class Reactions {
    public static void init() {}

    static {
        new Reaction(
                Map.of(H2O_s, 1), H2O_s,
                Map.of(H2O_l, 1), H2O_l,
                20000.0,
                1.0e6,
                SURFACE
        );

        new Reaction(
                Map.of(H2O_l, 1), H2O_l,
                Map.of(H2O_g, 1), H2O_g,
                44000.0,
                1.0e4,
                SURFACE
        );

        new Reaction(
                Map.of(H2O_s, 1), H2O_s,
                Map.of(H2O_g, 1), H2O_g,
                51000.0,
                1.0e2,
                SURFACE
        );
    }
}