package com.windwoif.equilibrium.content.recipe.chemical.structure.ion;

import com.windwoif.equilibrium.content.recipe.chemical.structure.Structure;
import com.windwoif.equilibrium.content.recipe.chemical.structure.atom.Atom;
import com.windwoif.equilibrium.content.recipe.reaction.generator.Salt.Anion;
import com.windwoif.equilibrium.content.recipe.reaction.generator.Salt.Cation;

import java.util.Map;

public class Ion extends Structure<Ion> {
    public static final String TYPE = "ion";

    private static final String TAG_PARTICLE = "Particle";
    private static final String TAG_CHARGE = "Charge";

    private final short charge;
    private final Structure<?> particle;

    public Ion(Structure<?> particle, int charge, String name,
               int enthalpy, float entropy, float molarHeatCapacity,
               float molarVolume) {
        super(particle.molarMass(), name, enthalpy, entropy, molarHeatCapacity, molarVolume);
        this.charge = (short) charge;
        this.particle = particle;
        feature(charge > 0 ? new Cation(this) : new Anion(this));
    }

    @Override
    public Map<Atom, Integer> getAtoms() {
        return particle.getAtoms();
    }

    @Override
    protected String typeId() {
        return TYPE;
    }

    public short getCharge() { return charge; }
    public Structure<?> getParticle() { return particle; }
}
