package com.windwoif.equilibrium.content.recipe.manager;

import com.windwoif.equilibrium.content.recipe.chemical.Chemical;

import java.util.*;
import java.util.stream.Stream;

public class RecipeDictionary<T> {
    private final Map<Chemical, List<T>> index = new HashMap<>();

    public void register(Chemical chemical, T recipe) {
        index.computeIfAbsent(chemical, k -> new ArrayList<>()).add(recipe);
    }

    public Stream<T> getRecipes(Collection<Chemical> chemicals) {
        return chemicals.stream().flatMap(chem -> index.getOrDefault(chem, Collections.emptyList()).stream()).distinct();
    }

    public void sealIndices() {
        index.replaceAll((k, set) -> List.copyOf(set));
    }
}
