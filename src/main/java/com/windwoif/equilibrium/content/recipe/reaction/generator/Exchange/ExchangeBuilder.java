package com.windwoif.equilibrium.content.recipe.reaction.generator.Exchange;

import com.windwoif.equilibrium.content.recipe.chemical.Chemical;
import com.windwoif.equilibrium.content.recipe.chemical.structure.atom.Atom;
import com.windwoif.equilibrium.content.recipe.reaction.Reaction;
import com.windwoif.equilibrium.content.recipe.reaction.generator.ReactionBuilder;
import com.windwoif.equilibrium.content.recipe.reaction.generator.ReactionMath;

import java.util.List;
import java.util.Map;

public class ExchangeBuilder extends ReactionBuilder {
    /** J/mol */
    private static final double ACTIVATION_ENERGY = 50000.0;
    private static final double PRE_EXPONENTIAL = 1.0e6;

    private final HalfReaction first;
    private final HalfReaction second;

    public ExchangeBuilder(HalfReaction first, HalfReaction second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Registers the builder for one exchanged atom.
     */
    public static void register(Atom atom) {
        String type = HalfReaction.PREFIX + atom.getSymbol();
        registerBuilder(new ReactionBuilderFactory(type, type,
                (first, second) -> new ExchangeBuilder((HalfReaction) first, (HalfReaction) second)));
    }

    @Override
    public List<Reaction> build() {
        int firstAtoms = first.released();
        int secondAtoms = second.released();
        if (firstAtoms <= 0 || secondAtoms <= 0) return List.of();

        int total = ReactionMath.lcm(firstAtoms, secondAtoms);
        int firstFactor = total / firstAtoms;
        int secondFactor = total / secondAtoms;

        Map<Chemical, Integer> reactants = ReactionMath.merge(
                first.consumes(), firstFactor, second.produces(), secondFactor);
        Map<Chemical, Integer> products = ReactionMath.merge(
                first.produces(), firstFactor, second.consumes(), secondFactor);

        List<Reaction> reactions = List.of(new Reaction(reactants, products,
                ACTIVATION_ENERGY, PRE_EXPONENTIAL, Reaction.State.SURFACE));
        cache(new UnorderedPair<>(first, second), reactions);
        return reactions;
    }
}
