package com.windwoif.balance.content.recipe.material;

import net.minecraft.world.level.material.Fluids;

import java.util.Map;

import static com.windwoif.balance.content.recipe.chemical.Chemicals.*;

public class Materials {
    public static void init() {
    }

    private static final int NUGGET_BASE = 500000;

    public static final Material IRON = new ItemMaterial(
            "iron",
            Map.of(Fe_s, NUGGET_BASE)
    );

    public static final Material GOLD = new ItemMaterial(
            "gold",
            Map.of(Au_s, NUGGET_BASE)
    );

    public static final Material COPPER = new ItemMaterial(
            "copper",
            Map.of(Cu_s, NUGGET_BASE)
    );

    public static final Material COAL = new ItemMaterial(
            "coal",
            p -> p.getMassFraction(C_s.get()) > 0.95,
            Map.of(C_s, NUGGET_BASE),
            C_s
    );

    public static final Material WATER = new FluidMaterial(
            "water",
            Fluids.WATER,
            p -> p.getMassFraction(H2O_l.get()) + p.getMassFraction(OH__aq.get()) > 0.999 && p.getContents().size() <= 4,
            Map.of(H2O_l, 55555),
            H2O_l
    );

    public static final Material TEST = new ItemMaterial(
            "redstone",
            Map.of(O2_g, 1000, N2_g, 4000)
    );

    public static final Material TEST2 = new ItemMaterial(
            "lapis",
            Map.of(H2O_l, 685300)
    );

    public static final Material TEST3 = new ItemMaterial(
            "quartz",
            Map.of(NaCl_s, NUGGET_BASE)
    );
}