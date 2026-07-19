package com.windwoif.balance.content.recipe.reaction;

import com.windwoif.balance.content.recipe.chemical.Chemical;
import com.windwoif.balance.content.recipe.manager.RecipeDictionary;
import net.minecraftforge.registries.RegistryObject;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Reaction {
    private final Map<Chemical, Integer> reactants;
    private final Map<Chemical, Integer> products;
    private final double activationEnergy;
    private final double preExponential;
    private final Map<Chemical, Integer> totalReaction;
    public final State state;

    private static final double R = 8.314;

    private static final RecipeDictionary<Reaction> INDEX = new RecipeDictionary<>();

    public Reaction(Map<Chemical, Integer> reactants, Chemical featuredReactant,
                    Map<Chemical, Integer> products, Chemical featuredProduct,
                    double activationEnergy, double preExponential, State state) {
        this.reactants = Map.copyOf(reactants);
        this.products = Map.copyOf(products);
        this.activationEnergy = activationEnergy;
        this.preExponential = preExponential;
        this.state = state;
        totalReaction = Stream
                .concat(products.keySet().stream(), reactants.keySet().stream())
                .distinct()
                .collect(Collectors.toMap(
                        key -> key,
                        key -> products.getOrDefault(key, 0) - reactants.getOrDefault(key, 0)
                ));
        INDEX.register(featuredReactant, this);
        INDEX.register(featuredProduct, this);
    }

    public Reaction(Map<RegistryObject<Chemical>, Integer> reactants, RegistryObject<Chemical> featuredReactant,
                    Map<RegistryObject<Chemical>, Integer> products, RegistryObject<Chemical> featuredProduct,
                    double activationEnergy, double preExponential, State state) {
        this(RecipeDictionary.convert(reactants), featuredReactant.get(), RecipeDictionary.convert(products), featuredProduct.get(), activationEnergy, preExponential, state);
    }

    public enum State {
        LIQUID_POLAR, LIQUID_NONPOLAR, GAS, SOLID, SURFACE
    }
    public double getEq(float T) {
        double deltaG = totalReaction.entrySet().stream()
                .map(a -> a.getKey().getGf(T) * a.getValue())
                .reduce(0.0, Double::sum);
        return Math.exp(-deltaG / (8.314 * T));
    }

    public double calculateRateConstant(float temperature){
        return preExponential * Math.exp(-activationEnergy / (R * temperature));
    }

    public Map<Chemical, Integer> getReactants() { return reactants; }
    public Map<Chemical, Integer> getProducts() { return products; }

    public Map<Chemical, Integer> getTotalReaction() {
        return totalReaction;
    }

    public static Stream<Reaction> getReactions(Collection<Chemical> chemicals) {
        return INDEX.getRecipes(chemicals);
    }
}