package com.windwoif.balance.content.recipe.reaction;

import com.windwoif.balance.content.recipe.chemical.Chemicals;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;

import static com.windwoif.balance.Balance.REACTIONS;
import static com.windwoif.balance.content.recipe.chemical.Chemicals.*;
import static com.windwoif.balance.content.recipe.reaction.Reaction.State.*;

public class Reactions {

    public static void init() {}

    public static final RegistryObject<Reaction> NULL_REACTION = REACTIONS.register("none_reaction",
            () -> new Reaction(
                    Map.of(Chemicals.NULL_CHEMICAL.get(), 1),
                    Map.of(Chemicals.NULL_CHEMICAL.get(), 1),
                    1,
                    1,
                    GAS
            )
    );
    // ---------- 中和反应（离子方程式）----------
    public static final RegistryObject<Reaction> NEUTRALIZATION = REACTIONS.register("neutralization",
            () -> new Reaction(
                    Map.of(                               // 反应物
                            HYDROGEN_ION.get(), 1,
                            HYDROXIDE.get(), 1
                    ),
                    Map.of(                               // 产物
                            WATER.get(), 1
                    ),
                    10000.0,      // 活化能 10 kJ/mol
                    1.0e6,        // 指前因子
                    LIQUID_POLAR        // 液相反应
            ));

    // ---------- 甲烷燃烧 ----------
    public static final RegistryObject<Reaction> METHANE_COMBUSTION = REACTIONS.register("methane_combustion",
            () -> new Reaction(
                    Map.of(
                            METHANE.get(), 1,
                            OXYGEN.get(), 2
                    ),
                    Map.of(
                            CARBON_DIOXIDE.get(), 1,
                            WATER_GAS.get(), 2      // 改为气态水
                    ),
                    50000.0,      // 50 kJ/mol（活化能）
                    1.0e8,        // 指前因子
                    GAS           // 反应状态为气相
            ));

    // ---------- 乙醇燃烧 ----------
    public static final RegistryObject<Reaction> ETHANOL_COMBUSTION = REACTIONS.register("ethanol_combustion",
            () -> new Reaction(
                    Map.of(
                            ETHANOL.get(), 1,
                            OXYGEN.get(), 3
                    ),
                    Map.of(
                            CARBON_DIOXIDE.get(), 2,
                            WATER_GAS.get(), 3
                    ),
                    55000.0,
                    1.0e8,
                    SURFACE
            ));

    // ---------- 氯化钠溶解（可逆过程，仅演示）----------
    public static final RegistryObject<Reaction> NACL_DISSOLUTION = REACTIONS.register("nacl_dissolution",
            () -> new Reaction(
                    Map.of(
                            SODIUM_CHLORIDE.get(), 1
                    ),
                    Map.of(
                            SODIUM_ION.get(), 1,
                            CHLORIDE.get(), 1
                    ),
                    20000.0,
                    1.0e3,
                    SURFACE
            ));

    public static final RegistryObject<Reaction> WATER_EVAPORATION = REACTIONS.register("water_evaporation",
            () -> new Reaction(
                    Map.of(Chemicals.WATER.get(), 1),
                    Map.of(Chemicals.WATER_GAS.get(), 1),
                    40000.0,
                    1e3,
                    SURFACE
            ));

    // 铁熔融 (Fe(s) ⇌ Fe(l))
    public static final RegistryObject<Reaction> IRON_MELTING = REACTIONS.register("iron_melting",
            () -> new Reaction(
                    Map.of(Chemicals.IRON.get(), 1),
                    Map.of(Chemicals.MOLTEN_IRON.get(), 1),
                    13800.0,
                    1e2,
                    SURFACE
            ));

    // 金熔融 (Au(s) ⇌ Au(l))
    public static final RegistryObject<Reaction> GOLD_MELTING = REACTIONS.register("gold_melting",
            () -> new Reaction(
                    Map.of(Chemicals.GOLD.get(), 1),
                    Map.of(Chemicals.MOLTEN_GOLD.get(), 1),
                    12600.0,
                    1e2,
                    SURFACE
            ));

    // 铜熔融 (Cu(s) ⇌ Cu(l))
    public static final RegistryObject<Reaction> COPPER_MELTING = REACTIONS.register("copper_melting",
            () -> new Reaction(
                    Map.of(Chemicals.COPPER.get(), 1),
                    Map.of(Chemicals.MOLTEN_COPPER.get(), 1),
                    13100.0,
                    1e2,
                    SURFACE
            ));

    public static final RegistryObject<Reaction> CARBON_COMBUSTION = REACTIONS.register("carbon_combustion",
            () -> new Reaction(
                    Map.of(
                            Chemicals.CARBON.get(), 1,
                            Chemicals.OXYGEN.get(), 1
                    ),
                    Map.of(
                            Chemicals.CARBON_DIOXIDE.get(), 1
                    ),
                    50000.0,
                    1e4,
                    SURFACE
            ));
}


