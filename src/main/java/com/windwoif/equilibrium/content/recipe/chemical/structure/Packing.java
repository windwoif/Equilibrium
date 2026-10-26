package com.windwoif.equilibrium.content.recipe.chemical.structure;

public enum Packing {
    CCP_HCP(0.7405f),
    BCC(0.6802f),
    SIMPLE_CUBIC(0.5236f),
    DIAMOND(0.34f),
    MOLECULAR(0.7f),
    LIQUID(0.65f);

    private final float fraction;

    Packing(float fraction) {
        this.fraction = fraction;
    }

    public float fraction() {
        return fraction;
    }
}
