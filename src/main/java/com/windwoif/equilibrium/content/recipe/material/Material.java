package com.windwoif.equilibrium.content.recipe.material;

import com.windwoif.equilibrium.content.reactors.reactorCore.Phase;
import com.windwoif.equilibrium.content.recipe.chemical.Chemical;
import java.util.Map;
import java.util.function.Predicate;

public abstract class Material {
    protected final String name;

    protected final Predicate<Phase> requirements;
    protected final Map<Chemical, Integer> content;

    public Material(String name, Predicate<Phase> requirements, Map<Chemical, Integer> content) {
        this.name = name;
        this.requirements = requirements;
        this.content = content;
    }

    public String getName() { return name; }
    public Predicate<Phase> getRequirements() { return requirements; }
    public Map<Chemical, Integer> getContent() { return content; }

    public boolean matches(Phase phase) {
        return requirements.test(phase);
    }
}