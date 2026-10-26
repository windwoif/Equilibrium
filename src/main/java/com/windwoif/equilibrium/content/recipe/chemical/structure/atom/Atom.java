package com.windwoif.equilibrium.content.recipe.chemical.structure.atom;

import com.windwoif.equilibrium.content.recipe.chemical.structure.Packing;
import com.windwoif.equilibrium.content.recipe.chemical.structure.Structure;
import net.minecraft.nbt.CompoundTag;

import java.util.Map;

public class Atom extends Structure<Atom> {
    public static final String TYPE = "atom";

    private static final String TAG_SYMBOL = "Symbol";

    private static final float RADIUS_TO_VOLUME_SCALE = (float) (4.0 / 3.0 * Math.PI * 1e-36 * 6.022e23 * 1000);

    private final String symbol;
    private final float radius;   // pm
    private final int CpkColor;
    private final Packing packing;

    public Atom(String symbol, float radius, float molarMass,
                float entropy, float molarHeatCapacity,
                int CPKColorRGB, Packing packing) {
        this(symbol, radius, molarMass, 0, entropy, molarHeatCapacity, CPKColorRGB, packing);
    }

    public Atom(String symbol, float radius, float molarMass,
                int enthalpy, float entropy, float molarHeatCapacity,
                int CPKColorRGB, Packing packing) {
        // 体积在构造时预测(球体积 / 堆积效率),存入实例
        super(molarMass, symbol, enthalpy, entropy, molarHeatCapacity,
                sphereVolume(radius) / packing.fraction());
        this.symbol = symbol;
        this.radius = radius;
        this.CpkColor = CPKColorRGB;
        this.packing = packing;
    }

    /** 单个原子的球体积,L/mol */
    private static float sphereVolume(float radiusPm) {
        return radiusPm * radiusPm * radiusPm * RADIUS_TO_VOLUME_SCALE;
    }

    @Override
    public Map<Atom, Integer> getAtoms() {
        return Map.of(this, 1);
    }

    @Override
    public float packingFraction() {
        return packing.fraction();
    }

    public float getSphereVolume() {
        return sphereVolume(radius);
    }

    @Override
    protected String typeId() {
        return TYPE;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString(TAG_TYPE, typeId());
        tag.putString(TAG_SYMBOL, symbol);
        return tag;
    }

    public String getSymbol() { return symbol; }
    public float getRadius() { return radius; }
    public int getCpkColor() { return CpkColor; }
    public Packing getPacking() { return packing; }
}
