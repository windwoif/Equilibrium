package com.windwoif.equilibrium.content.recipe.chemical.structure.group;

import com.windwoif.equilibrium.content.recipe.chemical.structure.IStructure;

public abstract class Group implements IStructure {
    private final float molarMass;
    private final int enthalpy;              // 最终值(实验或构造时预测),实例内保存
    private final float entropy;
    private final float molarHeatCapacity;
    private final float molarVolume;

    protected Group(float molarMass, int enthalpy, float entropy, float molarHeatCapacity, float molarVolume) {
        this.molarMass = molarMass;
        this.enthalpy = enthalpy;
        this.entropy = entropy;
        this.molarHeatCapacity = molarHeatCapacity;
        this.molarVolume = molarVolume;
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
}
