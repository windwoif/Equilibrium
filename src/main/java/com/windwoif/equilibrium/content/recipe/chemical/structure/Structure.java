package com.windwoif.equilibrium.content.recipe.chemical.structure;

import com.windwoif.equilibrium.content.recipe.chemical.structure.ionicCompund.IonicCompound;
import com.windwoif.equilibrium.content.recipe.reaction.Reaction;
import com.windwoif.equilibrium.content.recipe.reaction.generator.Feature;
import net.minecraft.nbt.CompoundTag;

import java.util.*;
import java.util.function.Function;

public abstract class Structure<T extends Structure<T>> implements IStructure {
    private final float molarMass;
    private String name;
    private final int enthalpy;
    private final float entropy;
    private final float molarHeatCapacity;
    private final float molarVolume;
    private final List<Feature> features = new ArrayList<>();

    /**
     * @param enthalpy           J/mol
     * @param entropy            J/(mol·K)
     * @param molarVolume        L/mol
     * @param molarHeatCapacity  J/(mol·K)
     * @param molarMass          g/mol
     */
    protected Structure(float molarMass, String name,
                        int enthalpy, float entropy, float molarHeatCapacity, float molarVolume) {

        this.molarMass = molarMass;
        this.name = name;
        this.enthalpy = enthalpy;
        this.entropy = entropy;
        this.molarHeatCapacity = molarHeatCapacity;
        this.molarVolume = molarVolume;
    }

    public String getName() {
        if (name == null) name = generateFormula();
        return name;
    }

    @Override
    public float molarMass() {
        return molarMass;
    }

    @Override
    public int enthalpy() {
        return enthalpy;
    }

    @Override
    public float entropy() {
        return entropy;
    }

    @Override
    public float molarHeatCapacity() {
        return molarHeatCapacity;
    }

    @Override
    public float molarVolume() {
        return molarVolume;
    }

    @SuppressWarnings("unchecked")
    public T register() {
        StructureRegistry.register(getName(), this);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T cache() {
        StructureRegistry.registerDynamic(getName(), this);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T rename(String name) {
        this.name = name;
        return (T) this;
    }

    /**
     * Attaches features used by the reaction generator.
     */
    @SuppressWarnings("unchecked")
    public T feature(Feature... features) {
        this.features.addAll(List.of(features));
        return (T) this;
    }

    public static final String TAG_TYPE = "Type";
    public static final String TAG_NAME = "Name";

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString(TAG_TYPE, typeId());
        tag.putString(TAG_NAME, getName());
        writeAdditional(tag);
        return tag;
    }

    protected abstract String typeId();

    protected void writeAdditional(CompoundTag tag) {};

    public static Structure<?> fromNBT(CompoundTag tag) {
        String name = tag.getString(TAG_NAME);
        return StructureRegistry.get(name).orElseGet(() -> {
            Function<CompoundTag, Structure<?>> function = PREDICTABLE_TYPES.get(tag.getString(TAG_TYPE));
            if (function != null) {
                return StructureRegistry.registerDynamic(name, function.apply(tag));
            }
            throw new IllegalArgumentException("Unregistered structure: " + name);
        });
    }

    private static final Map<String, Function<CompoundTag, Structure<?>>> PREDICTABLE_TYPES = new HashMap<>();

    protected static void registerPredictable(String type, Function<CompoundTag, Structure<?>> factory) {
        PREDICTABLE_TYPES.put(type, factory);
    }

    public static void init() {
        registerPredictable(IonicCompound.TYPE, IonicCompound::fromNBT);
    }

    public Optional<List<Reaction>> getReactions() {
        return Optional.empty();
    }

    public List<Feature> getFeatures() {
        return List.copyOf(features);
    }

    private static final char[] SUBSCRIPTS = {'₀', '₁', '₂', '₃', '₄', '₅', '₆', '₇', '₈', '₉'};

    public static String toSubscript(int n) {
        StringBuilder sb = new StringBuilder();
        for (char c : String.valueOf(n).toCharArray()) {
            sb.append(SUBSCRIPTS[c - '0']);
        }
        return sb.toString();
    }
}
