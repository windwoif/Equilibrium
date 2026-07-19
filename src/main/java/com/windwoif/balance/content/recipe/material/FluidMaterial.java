package com.windwoif.balance.content.recipe.material;

import com.windwoif.balance.content.reactors.reactorCore.Phase;
import com.windwoif.balance.content.recipe.chemical.Chemical;
import com.windwoif.balance.content.recipe.manager.RecipeDictionary;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import static com.windwoif.balance.content.recipe.manager.RecipeDictionary.convert;

public class FluidMaterial extends Material {
    private static final RecipeDictionary<FluidMaterial> INDEX = new RecipeDictionary<>();
    private static final Map<Fluid, FluidMaterial> MATERIALS = new HashMap<>();

    public FluidMaterial(String name, Fluid fluid, Predicate<Phase> requirements, Map<Chemical, Integer> content) {
        super(name, requirements, content);
        MATERIALS.put(fluid, this);
    }

    public FluidMaterial(String name, Fluid fluid, Predicate<Phase> requirements, Map<RegistryObject<Chemical>, Integer> content, RegistryObject<Chemical> featuredChemical) {
        this(name, fluid, requirements, convert(content));
        INDEX.register(featuredChemical.get(), this);
    }

    public static Optional<Map<Chemical, Long>> resolveFluid(FluidStack stack) {
        Fluid fluid = stack.getFluid();
        Material material = MATERIALS.get(fluid);
        if (material == null) return Optional.empty();
        Map<Chemical, Long> result = new HashMap<>();
        material.content.forEach((chem, amount) -> result.put(chem, (long) amount));
        return Optional.of(result);
    }

    public static Optional<FluidMaterial> get(Phase phase) {
        return INDEX.getRecipes(phase.getContents().keySet())
                .filter(m -> m.matches(phase))
                .findAny();
    }
}