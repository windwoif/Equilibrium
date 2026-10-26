package com.windwoif.equilibrium.content.recipe.chemical;

import com.windwoif.equilibrium.content.recipe.chemical.structure.Structure;
import net.minecraft.nbt.CompoundTag;

import static com.windwoif.equilibrium.content.recipe.chemical.ChemicalRegistry.registerDynamic;

/**
 * @param enthalpy           J/mol
 * @param entropy            J/(mol·K)
 * @param molarHeatCapacity  J/(mol·K)
 * @param molarVolume        L/mol
 */
public record Chemical(Structure<?> structure, State state, int ARGB,
                       int enthalpy, float entropy, float molarHeatCapacity, float molarVolume) {
    public enum State {
        SOLID(0xFFCCCCCC, "(s)"),
        LIQUID_POLAR(0x88FFFFFF, "(l)"),
        LIQUID_NONPOLAR(0x88FFFFFF, "(l)"),
        GAS(lucent, "(g)"),
        AQUEOUS(lucent, "(aq)"),
        ORGANIC(lucent, "(org)"),
        MOLTEN_SALT(0xFFFFE100, "(l)"),
        MOLTEN_METAL(0xFFFFB900, "(l)");

        public final int autoColour;
        public final String suffix;

        State(int autoColour, String suffix) {
            this.autoColour = autoColour;
            this.suffix = suffix;
        }
    }
    public final static int lucent = 0x00FFFFFF;

    private static final String TAG_STRUCTURE = "Structure";
    private static final String TAG_STATE = "State";

    public Chemical(Structure<?> structure, State state) {
        this(structure, state, state.autoColour);
    }

    public Chemical(Structure<?> structure, State state, int ARGB) {
        this(structure, state, ARGB,
                structure.enthalpy(), structure.entropy(),
                structure.molarHeatCapacity(), structure.molarVolume());
    }

    public String name() {
        return structure.getName() + state.suffix;
    }

    public double getGf(float temperature) {
        return enthalpy - temperature * entropy;
    }

    public float getEffectiveVolume(long amount) {
        return (float) ((amount / 1000.0) * molarVolume);
    }

    public float getHeatCapacity(long amount) {
        return (float) ((amount / 1000.0) * molarHeatCapacity);
    }

    public float getMass(long amount) { //kg;质量是结构属性,与物态无关
        return (float) ((amount / 1000.0 / 1000.0) * structure.molarMass());
    }

    public Chemical register() {
        ChemicalRegistry.register(this);
        return this;
    }

    public Chemical cache() {
        registerDynamic(this);
        return this;
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.put(TAG_STRUCTURE, structure.serializeNBT());
        tag.putString(TAG_STATE, state.name());
        return tag;
    }

    public static Chemical fromNBT(CompoundTag tag) {
        Structure<?> structure = Structure.fromNBT(tag.getCompound(TAG_STRUCTURE));
        State state = State.valueOf(tag.getString(TAG_STATE));
        String name = structure.getName() + state.suffix;
        return ChemicalRegistry.get(name).orElseGet(() -> new Chemical(structure, state));
    }

    @Override
    public String toString() {
        return name();
    }
}