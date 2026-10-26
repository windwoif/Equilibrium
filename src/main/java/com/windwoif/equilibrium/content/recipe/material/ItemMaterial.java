package com.windwoif.equilibrium.content.recipe.material;

import com.windwoif.equilibrium.content.reactors.reactorCore.Phase;
import com.windwoif.equilibrium.content.recipe.chemical.Chemical;
import com.windwoif.equilibrium.content.recipe.manager.RecipeDictionary;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public class ItemMaterial extends Material {
    private static final Map<String, Integer> FORMS = Map.of(
            "nuggets", 1,
            "ingots", 9,
            "storage_blocks", 81
    );

    private static final RecipeDictionary<ItemMaterial> INDEX = new RecipeDictionary<>();
    private static final Map<String, ItemMaterial> MATERIALS = new HashMap<>();

    private Item standardItem;

    private ItemMaterial(String name, Predicate<Phase> requirements, Map<Chemical, Integer> content) {
        super(name, requirements, content);
        MATERIALS.put(name, this);
    }

    public ItemMaterial(String name, Item item, Predicate<Phase> requirements, Map<Chemical, Integer> content, Chemical featuredChemical) {
        this(name, requirements, content);
        INDEX.register(featuredChemical, this);
        standardItem = item;
    }

    public ItemMaterial(String name, Map<Chemical, Integer> content) {
        this(name, a -> false, content);
    }

    public static Optional<Map<Chemical, Long>> resolveItem(Item item) {
        for (TagKey<Item> tag : item.builtInRegistryHolder().tags().toList()) {
            String path = tag.location().getPath(); // "ingots/copper"
            String[] parts = path.split("/");
            String form;
            String materialName;
            if (parts.length == 2) {
                form = parts[0]; // "ingots"
                materialName = parts[1]; // "copper"
            } else {
                continue;
            }
            Material material = MATERIALS.get(materialName);
            if (material == null) continue;

            int factor = FORMS.get(form);
            Map<Chemical, Long> result = new HashMap<>();
            material.content.forEach((chem, amount) -> result.put(chem, ((long) amount * factor)));
            return Optional.of(result);
        }
        return Optional.empty();
    }

    public static Optional<ItemMaterial> get(Phase phase) {
        return INDEX.getRecipes(phase.getContents().keySet())
                .filter(m -> m.matches(phase))
                .findAny();
    }

    public Item getItem() {
        return standardItem;
    }
}
