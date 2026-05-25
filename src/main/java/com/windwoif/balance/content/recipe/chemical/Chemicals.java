package com.windwoif.balance.content.recipe.chemical;

import net.minecraftforge.registries.RegistryObject;

import static com.windwoif.balance.Balance.CHEMICALS;
import static com.windwoif.balance.content.recipe.chemical.Chemical.State.*;

public class Chemicals {
    public static void init() {}

    final static int lucent = 0x00FFFFFF;

    public static final RegistryObject<Chemical> NULL_CHEMICAL = CHEMICALS.register("null_chemical",
            () -> new Chemical("null_chemical", GAS, 0, 0, 0, 0, 0, lucent));

    public static final RegistryObject<Chemical> HYDROGEN_ION = CHEMICALS.register("hydrogen_ion",
            () -> new Chemical("H⁺", AQUEOUS, 0, 0f, 0.001f, 0f, 1.0f, lucent));

    public static final RegistryObject<Chemical> HYDROXIDE = CHEMICALS.register("hydroxide",
            () -> new Chemical("OH⁻", AQUEOUS, -230000, -10.8f, 0.001f, 0f, 17.0f, lucent));

    public static final RegistryObject<Chemical> SODIUM_ION = CHEMICALS.register("sodium_ion",
            () -> new Chemical("Na⁺", AQUEOUS, -240100, 58.5f, 0.001f, 0f, 23.0f, lucent));

    public static final RegistryObject<Chemical> CHLORIDE = CHEMICALS.register("chloride",
            () -> new Chemical("Cl⁻", AQUEOUS, -167200, 56.5f, 0.001f, 0f, 35.5f, lucent));

    public static final RegistryObject<Chemical> WATER = CHEMICALS.register("water",
            () -> new Chemical("H₂O", LIQUID_POLAR, -285800, 70.0f, 0.018f, 75.3f, 18.0f, 0x40FFFFFF));

    public static final RegistryObject<Chemical> WATER_GAS = CHEMICALS.register("water_gas",
            () -> new Chemical("H₂O(g)", GAS, -241800, 188.8f, 22.4f, 33.6f, 18.0f, lucent));

    public static final RegistryObject<Chemical> METHANE = CHEMICALS.register("methane",
            () -> new Chemical("CH₄", GAS, -74600, 186.3f, 22.4f, 35.7f, 16.0f, lucent));

    public static final RegistryObject<Chemical> OXYGEN = CHEMICALS.register("oxygen",
            () -> new Chemical("O₂", GAS, 0, 205.0f, 22.4f, 29.4f, 32.0f, lucent));

    public static final RegistryObject<Chemical> NITROGEN = CHEMICALS.register("nitrogen",
            () -> new Chemical("N₂", GAS, 0, 191.6f, 22.4f, 29.1f, 28.0f, lucent));

    public static final RegistryObject<Chemical> CARBON_DIOXIDE = CHEMICALS.register("carbon_dioxide",
            () -> new Chemical("CO₂", GAS, -393500, 213.8f, 22.4f, 37.1f, 44.0f, lucent));

    public static final RegistryObject<Chemical> ETHANOL = CHEMICALS.register("ethanol",
            () -> new Chemical("C₂H₅OH", LIQUID_NONPOLAR, -277600, 160.7f, 0.058f, 112.0f, 46.0f, 0x40FFFFFF));
    // ---------- 金属（固体） ----------
    public static final RegistryObject<Chemical> IRON = CHEMICALS.register("iron",
            () -> new Chemical("Fe(s)", Chemical.State.SOLID, 0, 27.3f, 0.0071f, 25f, 55.85f, 0xFFD3D3D3));

    public static final RegistryObject<Chemical> GOLD = CHEMICALS.register("gold",
            () -> new Chemical("Au(s)", Chemical.State.SOLID, 0, 47.4f, 0.0102f, 25f, 197.0f, 0xFFFFD700));

    public static final RegistryObject<Chemical> COPPER = CHEMICALS.register("copper",
            () -> new Chemical("Cu(s)", Chemical.State.SOLID, 0, 33.2f, 0.0071f, 25f, 63.55f, 0xFFDA6D12));

    // ---------- 金属（熔融态）修正摩尔体积 ----------
    public static final RegistryObject<Chemical> MOLTEN_IRON = CHEMICALS.register("molten_iron",
            () -> new Chemical("Fe(l)", Chemical.State.MOLTEN_METAL, 13800, 34.9f, 0.0080f, 25f, 55.85f, 0xFFFFAD00));

    public static final RegistryObject<Chemical> MOLTEN_GOLD = CHEMICALS.register("molten_gold",
            () -> new Chemical("Au(l)", Chemical.State.MOLTEN_METAL, 12600, 56.8f, 0.0113f, 25f, 197.0f, 0xFFFFD700));

    public static final RegistryObject<Chemical> MOLTEN_COPPER = CHEMICALS.register("molten_copper",
            () -> new Chemical("Cu(l)", Chemical.State.MOLTEN_METAL, 13100, 42.8f, 0.0082f, 25f, 63.55f, 0xFFFF5C00));

    public static final RegistryObject<Chemical> SODIUM_CHLORIDE = CHEMICALS.register("sodium_chloride",
            () -> new Chemical("NaCl", SOLID, -411200, 72.1f, 0.027f, 50.5f, 58.5f, lucent));

    public static final RegistryObject<Chemical> CARBON = CHEMICALS.register("carbon",
            () -> new Chemical("C(s)", Chemical.State.SOLID, 0, 5.7f, 0.0092f, 20.0f, 12.01f, 0xFF333333));
}