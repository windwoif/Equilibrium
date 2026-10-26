package com.windwoif.equilibrium.content.recipe.chemical.structure.simpleMolecule;

import com.windwoif.equilibrium.content.recipe.chemical.structure.BondType;
import com.windwoif.equilibrium.content.recipe.chemical.structure.Structure;
import com.windwoif.equilibrium.content.recipe.chemical.structure.atom.Atom;

import java.util.HashMap;
import java.util.Map;

public class DiatomicMolecule extends Structure<DiatomicMolecule> {
    public static final String TYPE = "diatomic";

    private static final String TAG_ATOM1 = "Atom1";
    private static final String TAG_ATOM2 = "Atom2";
    private static final String TAG_BOND = "Bond";

    private final Atom atom1;
    private final Atom atom2;
    private final BondType bondType;

    public DiatomicMolecule(Atom atom1, Atom atom2, BondType bondType,
                            int enthalpy, float entropy, float molarHeatCapacity, float molarVolume) {
        super(atom1.molarMass() + atom2.molarMass(), null, enthalpy, entropy, molarHeatCapacity, molarVolume);
        this.atom1 = atom1;
        this.atom2 = atom2;
        this.bondType = bondType;
    }

    @Override
    public Map<Atom, Integer> getAtoms() {
        Map<Atom, Integer> atoms = new HashMap<>();
        atoms.merge(atom1, 1, Integer::sum);
        atoms.merge(atom2, 1, Integer::sum);
        return atoms;
    }

    @Override
    protected String typeId() {
        return TYPE;
    }

    public Atom getAtom1() { return atom1; }
    public Atom getAtom2() { return atom2; }
    public BondType getBondType() { return bondType; }
}
