package com.windwoif.equilibrium.content.recipe.chemical.structure.group;

import com.windwoif.equilibrium.content.recipe.chemical.structure.BondType;
import com.windwoif.equilibrium.content.recipe.chemical.structure.atom.Atom;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CentralGroup extends Group {
    private final BondType bond;
    private final Atom centralAtom;
    private final List<Group> surroundingGroups;

    public CentralGroup(BondType bondType, Atom centralAtom, List<Group> surroundingGroups) {
        this(bondType, centralAtom, surroundingGroups, 0, 0, 0, 0);
    }

    public CentralGroup(BondType bondType, Atom centralAtom, List<Group> surroundingGroups,
                        int enthalpy, float entropy, float molarHeatCapacity, float molarVolume) {
        super(calcMass(centralAtom, surroundingGroups),
                enthalpy, entropy, molarHeatCapacity, molarVolume);
        this.bond = bondType;
        this.centralAtom = centralAtom;
        this.surroundingGroups = sortGroups(surroundingGroups);
    }

    private static float calcMass(Atom central, List<Group> groups) {
        return (float) (central.molarMass() + groups.stream()
                .mapToDouble(Group::molarMass)
                .sum());
    }

    private List<Group> sortGroups(List<Group> groups) {
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
                        .filter(Objects::nonNull)
                        .flatMap(group -> group.getAtoms().entrySet().stream())
        ).collect(Collectors.collectingAndThen(
                Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Integer::sum),
                Collections::unmodifiableMap
        ));
    }
}
