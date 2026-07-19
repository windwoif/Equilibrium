package com.windwoif.balance.content.recipe.reaction;

import net.minecraftforge.registries.RegistryObject;

import java.util.Map;

import static com.windwoif.balance.Balance.REACTIONS;
import static com.windwoif.balance.content.recipe.chemical.Chemicals.*;
import static com.windwoif.balance.content.recipe.reaction.Reaction.State.*;

public class Reactions {

    public static void init() {}

    public static final RegistryObject<Reaction> NEUTRALIZATION = REACTIONS.register("neutralization",
            () -> new Reaction(
                    Map.of(H$_aq, 1, OH__aq, 1), H$_aq,
                    Map.of(H2O_l, 1), H2O_l,
                    10000.0, 1.0e8, LIQUID_POLAR
            ));

    public static final RegistryObject<Reaction> METHANE_COMBUSTION = REACTIONS.register("methane_combustion",
            () -> new Reaction(
                    Map.of(CH4_g, 1, O2_g, 2), CH4_g,
                    Map.of(CO2_g, 1, H2O_g, 2), CO2_g,
                    50000.0, 1.0e8, GAS
            ));

    public static final RegistryObject<Reaction> ETHANOL_COMBUSTION = REACTIONS.register("ethanol_combustion",
            () -> new Reaction(
                    Map.of(C2H5OH_l, 1, O2_g, 3), C2H5OH_l,
                    Map.of(CO2_g, 2, H2O_g, 3), CO2_g,
                    55000.0, 1.0e8, SURFACE
            ));

    public static final RegistryObject<Reaction> NACL_DISSOLUTION = REACTIONS.register("nacl_dissolution",
            () -> new Reaction(
                    Map.of(NaCl_s, 1), NaCl_s,
                    Map.of(Na$_aq, 1, Cl__aq, 1), Na$_aq,
                    20000.0, 1.0e3, SURFACE
            ));

    public static final RegistryObject<Reaction> WATER_EVAPORATION = REACTIONS.register("water_evaporation",
            () -> new Reaction(
                    Map.of(H2O_l, 1), H2O_l,
                    Map.of(H2O_g, 1), H2O_g,
                    40000.0, 1e3, SURFACE
            ));

    public static final RegistryObject<Reaction> IRON_MELTING = REACTIONS.register("iron_melting",
            () -> new Reaction(
                    Map.of(Fe_s, 1), Fe_s,
                    Map.of(Fe_l, 1), Fe_l,
                    13800.0, 1e2, SURFACE
            ));

    public static final RegistryObject<Reaction> GOLD_MELTING = REACTIONS.register("gold_melting",
            () -> new Reaction(
                    Map.of(Au_s, 1), Au_s,
                    Map.of(Au_l, 1), Au_l,
                    12600.0, 1e2, SURFACE
            ));

    public static final RegistryObject<Reaction> COPPER_MELTING = REACTIONS.register("copper_melting",
            () -> new Reaction(
                    Map.of(Cu_s, 1), Cu_s,
                    Map.of(Cu_l, 1), Cu_l,
                    13100.0, 1e2, SURFACE
            ));

    public static final RegistryObject<Reaction> CARBON_COMBUSTION = REACTIONS.register("carbon_combustion",
            () -> new Reaction(
                    Map.of(C_s, 1, O2_g, 1), C_s,
                    Map.of(CO2_g, 1), CO2_g,
                    50000.0, 1e4, SURFACE
            ));
}