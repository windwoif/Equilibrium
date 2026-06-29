package com.windwoif.balance.content.recipe.material;

import com.windwoif.balance.Balance;
import com.windwoif.balance.content.reactors.reactorCore.Phase;
import com.windwoif.balance.content.recipe.chemical.Chemical;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import static com.windwoif.balance.Balance.MODID;
import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public record Material(
        String name, //tag name
        Predicate<Phase> requirements,
        Map<Chemical, Long> content) {
    public final static Map<String, Integer> FORMS = Map.of(
            "nuggets", 1,
            "ingots", 9,
            "storage_blocks", 81
    );


    public static Optional<Map<Chemical, Long>> resolve(ItemStack stack) {
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
            IForgeRegistry<Material> materialRegistry = RegistryManager.ACTIVE.getRegistry(Balance.MATERIAL_REGISTRY_KEY);
            Material material = materialRegistry.getValue(fromNamespaceAndPath(MODID, materialName));
            if (material == null) continue;

            int factor = FORMS.get(form);
            Map<Chemical, Long> result = new HashMap<>();
            material.content().forEach((chem, amount) -> result.put(chem, amount * factor));
            return Optional.of(result);
        }
        return Optional.empty();
    }
}
