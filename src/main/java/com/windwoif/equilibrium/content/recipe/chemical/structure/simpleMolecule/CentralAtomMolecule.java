package com.windwoif.equilibrium.content.recipe.chemical.structure.simpleMolecule;

import com.windwoif.equilibrium.content.recipe.chemical.structure.Structure;
import com.windwoif.equilibrium.content.recipe.chemical.structure.atom.Atom;
import com.windwoif.equilibrium.content.recipe.chemical.structure.group.Group;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.windwoif.equilibrium.content.recipe.chemical.structure.atom.Atoms.C;


public class CentralAtomMolecule extends Structure<CentralAtomMolecule> {
    public static final String TYPE = "central";

    private final Atom centralAtom;
    private final List<Group> surroundingGroups;

    public CentralAtomMolecule(Atom centralAtom, List<Group> surroundingGroups) {
        this(centralAtom, surroundingGroups, null, 0, 0, 0, 0);
    }

    /**
     * VSEPR species
     * @param centralAtom can't be C
     * @param surroundingGroups contains null for lone pairs
     */
    public CentralAtomMolecule(Atom centralAtom, List<Group> surroundingGroups, String name,
                               int enthalpy, float entropy, float molarHeatCapacity, float molarVolume) {
        super(calcMass(centralAtom, surroundingGroups),
                name, enthalpy, entropy, molarHeatCapacity, molarVolume);
        if (centralAtom == C) throw new IllegalArgumentException("Central Atom can't be CARBON");
        this.centralAtom = centralAtom;
        this.surroundingGroups = sortGroups(surroundingGroups);
    }

    private static float calcMass(Atom central, List<Group> groups) {
        return (float) (central.molarMass() + groups.stream()
                .filter(Objects::nonNull)
                .mapToDouble(Group::molarMass)
                .sum());
    }

    private List<Group> sortGroups(List<Group> groups) {
        // Sort groups to ensure consistent serialization.
        // Maybe it doesn't matter now.
        List<Group> sorted = new ArrayList<>(groups);
        sorted.sort(Comparator.comparingDouble(Group::molarMass)
                .thenComparing(g -> g.getClass().getSimpleName())
        );
        return Collections.unmodifiableList(sorted);
    }

    @Override
    public Map<Atom, Integer> getAtoms() {
        return Stream.concat(
                Stream.of(Map.entry(centralAtom, 1)),
                surroundingGroups.stream()
                        .flatMap(group -> group.getAtoms().entrySet().stream())
        ).collect(Collectors.collectingAndThen(
                Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Integer::sum),
                Collections::unmodifiableMap
        ));
    }

    @Override
    protected String typeId() {
        return TYPE;
    }

    public Atom getCentralAtom() { return centralAtom; }
    public List<Group> getSurroundingGroups() { return surroundingGroups; }
}
