package com.windwoif.equilibrium.content.recipe.reaction;

import com.windwoif.equilibrium.content.recipe.chemical.Chemical;
import com.windwoif.equilibrium.content.recipe.manager.RecipeDictionary;

import java.util.Collection;
import java.util.Map;
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

    /**
     * @param activationEnergy  J/mol
     */
    public Reaction(Map<Chemical, Integer> reactants, Map<Chemical, Integer> products,
                    double activationEnergy, double preExponential, State state) {
        this.reactants = Map.copyOf(reactants);
        this.products = Map.copyOf(products);
        this.activationEnergy = activationEnergy;
        this.preExponential = preExponential;
        this.state = state;
        this.totalReaction = Stream
                .concat(products.keySet().stream(), reactants.keySet().stream())
                .distinct()
                .collect(Collectors.toMap(
                        key -> key,
                        key -> products.getOrDefault(key, 0) - reactants.getOrDefault(key, 0)
                ));
    }

    /**
     * @param activationEnergy  J/mol
     */
    public Reaction(Map<Chemical, Integer> reactants, Chemical featuredReactant,
                    Map<Chemical, Integer> products, Chemical featuredProduct,
                    double activationEnergy, double preExponential, State state) {
        this(reactants, products, activationEnergy, preExponential, state);
        INDEX.register(featuredReactant, this);
        INDEX.register(featuredProduct, this);
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

    private static String formatSide(Map<Chemical, Integer> side) {
        return side.entrySet().stream()
                .map(e -> {
                    int count = e.getValue();
                    String name = e.getKey().name();
                    return count == 1 ? name : count + name;
                })
                .collect(Collectors.joining(" + "));
    }

    @Override
    public String toString() {
        return formatSide(reactants) + " == " + formatSide(products);
    }
}