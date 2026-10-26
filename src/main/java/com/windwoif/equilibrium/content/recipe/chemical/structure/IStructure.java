package com.windwoif.equilibrium.content.recipe.chemical.structure;

import com.windwoif.equilibrium.content.recipe.chemical.structure.atom.Atom;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static com.windwoif.equilibrium.content.recipe.chemical.structure.Structure.toSubscript;

public interface IStructure {

    Map<Atom, Integer> getAtoms();

    float molarMass();

    default int enthalpy() {
        return 0;
    }


    default float entropy() {
        return 0;
    }


    default float molarHeatCapacity() {
        return 0;
    }


    default float molarVolume() {
        return predictMolarVolume();
    }


    default float predictMolarVolume() {
        float sphereVolume = (float) getAtoms().entrySet().stream()
                .mapToDouble(e -> e.getKey().getSphereVolume() * e.getValue())
                .sum();
        return sphereVolume / packingFraction();
    }


    default float packingFraction() {
        return Packing.MOLECULAR.fraction();
    }


    default String generateFormula() {
        Map<Atom, Integer> atomMap = getAtoms();
        List<Map.Entry<Atom, Integer>> entries = new ArrayList<>(atomMap.entrySet());
        entries.sort(Comparator.comparing(e -> e.getKey().getSymbol()));
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Atom, Integer> entry : entries) {
            String symbol = entry.getKey().getSymbol();
            int count = entry.getValue();
            sb.append(symbol);
            if (count > 1) sb.append(toSubscript(count));
        }
        return sb.toString();
    }

//    RenderData getRenderData();
}
