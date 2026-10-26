package com.windwoif.equilibrium.content.recipe.chemical.structure.group;

import com.windwoif.equilibrium.content.recipe.chemical.structure.BondType;
import com.windwoif.equilibrium.content.recipe.chemical.structure.atom.Atom;

import java.util.Map;

public class SingleAtomGroup extends Group {
    public static final String TYPE = "single_atom";

    private final BondType bond;
    private final Atom atom;

    public SingleAtomGroup(BondType bondType, Atom atom) {
        this(bondType, atom, 0, 0, 0, 0);
    }

    public SingleAtomGroup(BondType bondType, Atom atom,
                           int enthalpy, float entropy, float molarHeatCapacity, float molarVolume) {
        super(atom.molarMass(), enthalpy, entropy, molarHeatCapacity, molarVolume);
        this.bond = bondType;
        this.atom = atom;
    }

    @Override
    public Map<Atom, Integer> getAtoms() {
        return Map.of(atom, 1);
    }

    public BondType Bond() {
        return bond;
    }

    public Atom Atom() {
        return atom;
    }
}
