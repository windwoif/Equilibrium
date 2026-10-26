package com.windwoif.equilibrium.content.recipe.chemical;

import com.windwoif.equilibrium.content.recipe.chemical.structure.group.CentralGroup;
import com.windwoif.equilibrium.content.recipe.chemical.structure.group.SingleAtomGroup;

import java.util.List;

import static com.windwoif.equilibrium.content.recipe.chemical.structure.BondType.*;
import static com.windwoif.equilibrium.content.recipe.chemical.structure.BondType.SINGLE;
import static com.windwoif.equilibrium.content.recipe.chemical.structure.atom.Atoms.H;
import static com.windwoif.equilibrium.content.recipe.chemical.structure.atom.Atoms.O;
import static com.windwoif.equilibrium.content.recipe.chemical.structure.group.LonePair.LP;

public class Chemicals {
    public static void init() {
        Elements.init();
        Ions.init();
        Salts.init();
        Oxides.init();
    }
    static SingleAtomGroup _H = new SingleAtomGroup(SINGLE, H);
    static SingleAtomGroup __O = new SingleAtomGroup(DOUBLE, O);
    static SingleAtomGroup $O = new SingleAtomGroup(DELOCALIZED, O);
    static CentralGroup _OH = new CentralGroup(SINGLE, O, List.of(_H, LP, LP));
}
