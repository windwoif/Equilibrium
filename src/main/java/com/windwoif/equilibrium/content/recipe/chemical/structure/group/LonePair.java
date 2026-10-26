package com.windwoif.equilibrium.content.recipe.chemical.structure.group;

import com.windwoif.equilibrium.content.recipe.chemical.structure.atom.Atom;

import java.util.Map;

public class LonePair extends Group {
    private LonePair() {
        super(0, 0, 0, 0, 0);
    }

    @Override
    public Map<Atom, Integer> getAtoms() {
        return Map.of();
    }

    public final static LonePair LP = new LonePair();
}
