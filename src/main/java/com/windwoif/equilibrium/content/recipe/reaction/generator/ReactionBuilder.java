package com.windwoif.equilibrium.content.recipe.reaction.generator;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.windwoif.equilibrium.content.recipe.chemical.Chemical;
import com.windwoif.equilibrium.content.recipe.reaction.Reaction;
import com.windwoif.equilibrium.content.recipe.reaction.generator.Salt.Anion;
import com.windwoif.equilibrium.content.recipe.reaction.generator.Salt.Cation;
import com.windwoif.equilibrium.content.recipe.reaction.generator.Salt.SaltBuilder;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public abstract class ReactionBuilder {
    public static final Cache<UnorderedPair<Feature>, List<Reaction>> cache = CacheBuilder.newBuilder()
            .weakValues()
            .maximumSize(5000)
            .expireAfterAccess(60, TimeUnit.SECONDS)
            .build();

    public record UnorderedPair<T>(T first, T second) {
        @Override
        public int hashCode() {
            return Objects.hashCode(first) + Objects.hashCode(second);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof UnorderedPair<?> that)) return false;
            return (Objects.equals(this.first, that.first) && Objects.equals(this.second, that.second))
                    || (Objects.equals(this.first, that.second) && Objects.equals(this.second, that.first));
        }
    }

    protected void cache(UnorderedPair<Feature> key, List<Reaction> reactions) {
        cache.put(key, reactions);
    }

    private final static Map<String, List<ReactionBuilderFactory>> reactionBuilders = new HashMap<>();

    public record ReactionBuilderFactory(String firstType, String secondType,
                                         BiFunction<Feature, Feature, ReactionBuilder> builder) {
        boolean unordered() {
            return firstType.equals(secondType);
        }
    }

    protected static void registerBuilder(ReactionBuilderFactory factory) {
        reactionBuilders.computeIfAbsent(factory.firstType, k -> new ArrayList<>()).add(factory);
    }

    static {
        registerBuilder(new ReactionBuilderFactory(Cation.TYPE, Anion.TYPE, (a, b) -> new SaltBuilder((Cation) a, (Anion) b)));
    }

    public abstract List<Reaction> build();

    public static Stream<Reaction> buildFrom(Collection<Chemical> content) {
        Map<String, List<Feature>> groups = content.stream()
                .flatMap(c -> c.structure().getFeatures().stream())
                .distinct()
                .collect(Collectors.groupingBy(Feature::type));
        return groups.keySet().stream()
                .map(type ->
                        reactionBuilders.getOrDefault(type, List.of()))
                .flatMap(List<ReactionBuilderFactory>::stream)
                .filter(builder ->
                        groups.containsKey(builder.secondType))
                .flatMap(
                        b -> b.unordered() ? combine(groups.get(b.firstType), b.builder)
                                : combine(groups.get(b.firstType), groups.get(b.secondType), b.builder)
                )
                .map(ReactionBuilder::build)
                .flatMap(List<Reaction>::stream); //YOUR TAKING TOO LONG
    }

    public static <T, R> Stream<R> combine(
            Collection<T> c1,
            Collection<T> c2,
            BiFunction<? super T, ? super T, ? extends R> f) {
        return c1.stream()
                .flatMap(a -> c2.stream().map(b -> f.apply(a, b)));
    }

    public static <T, R> Stream<R> combine(
            List<T> list,
            BiFunction<? super T, ? super T, ? extends R> f) {
        return IntStream.range(0, list.size())
                .boxed()
                .flatMap(i -> IntStream.range(i + 1, list.size()) // from i + 1? from i?
                        .mapToObj(j -> f.apply(list.get(i), list.get(j))));
    }
}
