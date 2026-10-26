package com.windwoif.equilibrium.content.recipe.chemical;

import com.windwoif.equilibrium.content.recipe.chemical.structure.simpleMolecule.CentralAtomMolecule;

import java.util.List;

import static com.windwoif.equilibrium.content.recipe.chemical.Chemical.State.*;
import static com.windwoif.equilibrium.content.recipe.chemical.Chemical.lucent;
import static com.windwoif.equilibrium.content.recipe.chemical.Chemicals.*;
import static com.windwoif.equilibrium.content.recipe.chemical.structure.atom.Atoms.*;
import static com.windwoif.equilibrium.content.recipe.chemical.structure.group.LonePair.LP;

public class Oxides {
    public static void init() {}
    static final CentralAtomMolecule WATER_STRUCTURE = new CentralAtomMolecule(
            O, List.of(_H, _H, LP, LP)).register();

    public static final Chemical H2O_l = new Chemical(
            WATER_STRUCTURE, LIQUID_POLAR, 0x66FFFFFF,
            -285830, 69.95f, 75.3f, 0.0180f
    ).register();

    public static final Chemical H2O_g = new Chemical(
            WATER_STRUCTURE, GAS, lucent,
            -241820, 188.83f, 33.6f, 22.414f
    ).register();

    public static final Chemical H2O_s = new Chemical(
            WATER_STRUCTURE, SOLID, 0x88D9FFFF,
            -292720, 44.7f, 38.0f, 0.0196f
    ).register();
}
