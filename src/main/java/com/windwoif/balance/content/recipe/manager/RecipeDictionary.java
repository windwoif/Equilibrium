package com.windwoif.balance.content.recipe.manager;

import com.windwoif.balance.content.recipe.chemical.Chemical;
import net.minecraftforge.registries.RegistryObject;

import java.util.*;
import java.util.stream.Stream;

public class RecipeDictionary<T> {
    private final Map<Chemical, Set<T>> index = new HashMap<>();

    public void register(Chemical chemical, T recipe) {
        index.computeIfAbsent(chemical, k -> new HashSet<>()).add(recipe);
    }

    public Stream<T> getRecipes(Collection<Chemical> chemicals) {
        return chemicals.stream().flatMap(chem -> index.getOrDefault(chem, Collections.emptySet()).stream()).distinct();
    }

    public static Map<Chemical, Integer> convert(Map<RegistryObject<Chemical>, Integer> map) {
        Map<Chemical, Integer> result = new HashMap<>();
        for (Map.Entry<RegistryObject<Chemical>, Integer> entry : map.entrySet()) {
            result.put(entry.getKey().get(), entry.getValue());
        }
        return Map.copyOf(result);
    }
}
