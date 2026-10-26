package com.windwoif.equilibrium.content.recipe.reaction.generator.Salt;

import com.windwoif.equilibrium.content.recipe.chemical.structure.StructureRegistry;
import com.windwoif.equilibrium.content.recipe.chemical.structure.ion.Ion;
import com.windwoif.equilibrium.content.recipe.chemical.structure.ionicCompund.IonicCompound;
import com.windwoif.equilibrium.content.recipe.reaction.Reaction;
import com.windwoif.equilibrium.content.recipe.reaction.generator.ReactionBuilder;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SaltBuilder extends ReactionBuilder {
    Cation cation;
    Anion anion;
    Map<Ion, Integer> composition;
    IonicCompound ionicCompound;

    public SaltBuilder(Cation cation, Anion anion) {
        this.cation = cation;
        this.anion = anion;
        composition = IonicCompound.buildComposition(cation.getIon(), anion.getIon());
        ionicCompound = (IonicCompound) StructureRegistry.get(IonicCompound.getName(composition))
                .orElseGet(() -> new IonicCompound(composition).cache());
    }

    @Override
    public List<Reaction> build() {
        Optional<List<Reaction>> reactions = ionicCompound.getReactions();
        cache(new UnorderedPair<>(cation, anion), reactions.orElse(List.of()));
        return reactions.orElse(List.of());
    }
}
