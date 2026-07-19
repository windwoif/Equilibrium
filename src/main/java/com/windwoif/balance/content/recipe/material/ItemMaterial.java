package com.windwoif.balance.content.recipe.material;

import com.windwoif.balance.content.reactors.reactorCore.Phase;
import com.windwoif.balance.content.recipe.chemical.Chemical;
import com.windwoif.balance.content.recipe.manager.RecipeDictionary;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import static com.windwoif.balance.content.recipe.manager.RecipeDictionary.convert;

public class ItemMaterial extends Material {
    private static final Map<String, Integer> FORMS = Map.of(
            "nuggets", 1,
            "ingots", 9,
            "storage_blocks", 81
    );

    private static final RecipeDictionary<ItemMaterial> INDEX = new RecipeDictionary<>();
    private static final Map<String, ItemMaterial> MATERIALS = new HashMap<>();

    public ItemMaterial(String name, Predicate<Phase> requirements, Map<Chemical, Integer> content) {
        super(name, requirements, content);
        MATERIALS.put(name, this);
    }

    public ItemMaterial(String name, Predicate<Phase> requirements, Map<RegistryObject<Chemical>, Integer> content, RegistryObject<Chemical> featuredChemical) {
        this(name, requirements, convert(content));
        INDEX.register(featuredChemical.get(), this);
    }

    public ItemMaterial(String name, Map<RegistryObject<Chemical>, Integer> content) {
        this(name, a -> false, convert(content));
    }

    public static Optional<Map<Chemical, Long>> resolveItem(ItemStack stack) {
        for (TagKey<Item> tag : stack.getTags().toList()) {
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
}