package com.windwoif.equilibrium.content.recipe.chemical;

import com.windwoif.equilibrium.content.recipe.chemical.structure.ion.Ion;
import com.windwoif.equilibrium.content.recipe.chemical.structure.simpleMolecule.CentralAtomMolecule;

import java.util.List;

import static com.windwoif.equilibrium.content.recipe.chemical.Chemical.State.AQUEOUS;
import static com.windwoif.equilibrium.content.recipe.chemical.Chemical.State.MOLTEN_SALT;
import static com.windwoif.equilibrium.content.recipe.chemical.Chemical.lucent;
import static com.windwoif.equilibrium.content.recipe.chemical.Chemicals.*;
import static com.windwoif.equilibrium.content.recipe.chemical.structure.atom.Atoms.*;
import static com.windwoif.equilibrium.content.recipe.chemical.structure.group.LonePair.LP;

public class Ions {
    public static void init() {}

    // ========== Cations ==========
    public static final Ion H$ = new Ion(H, 1, "H⁺",
            0, 0, 20.8f, 0f).register();
    public static final Chemical H$_aq = new Chemical(H$, AQUEOUS).register();

    public static final Ion Li$ = new Ion(Li, 1, "Li⁺",
            -278500, 13.4f, 20.8f, 0.0130f).register();
    public static final Chemical Li$_aq = new Chemical(Li$, AQUEOUS).register();

    public static final Ion Na$ = new Ion(Na, 1, "Na⁺",
            -240100, 59.0f, 20.8f, 0.0237f).register();
    public static final Chemical Na$_aq = new Chemical(Na$, AQUEOUS).register();

    public static final Ion K$ = new Ion(K, 1, "K⁺",
            -252400, 102.5f, 20.8f, 0.0457f).register();
    public static final Chemical K$_aq = new Chemical(K$, AQUEOUS).register();

    public static final Ion Rb$ = new Ion(Rb, 1, "Rb⁺",
            -251200, 121.8f, 20.8f, 0.0559f).register();
    public static final Chemical Rb$_aq = new Chemical(Rb$, AQUEOUS).register();

    public static final Ion Cs$ = new Ion(Cs, 1, "Cs⁺",
            -258300, 133.0f, 20.8f, 0.0707f).register();
    public static final Chemical Cs$_aq = new Chemical(Cs$, AQUEOUS).register();

    public static final Ion Mg2$ = new Ion(Mg, 2, "Mg²⁺",
            -466900, -138.1f, 20.8f, 0.0140f).register();
    public static final Chemical Mg2$_aq = new Chemical(Mg2$, AQUEOUS).register();

    public static final Ion Ca2$ = new Ion(Ca, 2, "Ca²⁺",
            -543000, -53.1f, 20.8f, 0.0259f).register();
    public static final Chemical Ca2$_aq = new Chemical(Ca2$, AQUEOUS).register();

    public static final Ion Ba2$ = new Ion(Ba, 2, "Ba²⁺",
            -537700, 9.6f, 20.8f, 0.0382f).register();
    public static final Chemical Ba2$_aq = new Chemical(Ba2$, AQUEOUS).register();

    public static final Ion Al3$ = new Ion(Al, 3, "Al³⁺",
            -531100, -321.7f, 20.8f, 0.0100f).register();
    public static final Chemical Al3$_aq = new Chemical(Al3$, AQUEOUS).register();

    public static final Ion Fe2$ = new Ion(Fe, 2, "Fe²⁺",
            -89100, -137.7f, 20.8f, 0.0071f).register();
    public static final Chemical Fe2$_aq = new Chemical(Fe2$, AQUEOUS, 0x8877B077).register();

    public static final Ion Fe3$ = new Ion(Fe, 3, "Fe³⁺",
            -48500, -315.9f, 20.8f, 0.0071f).register();
    public static final Chemical Fe3$_aq = new Chemical(Fe3$, AQUEOUS, 0x88C28A3A).register();

    public static final Ion Cu2$ = new Ion(Cu, 2, "Cu²⁺",
            64800, -99.6f, 20.8f, 0.0071f).register();
    public static final Chemical Cu2$_aq = new Chemical(Cu2$, AQUEOUS, 0x882979C1).register();

    public static final Ion Zn2$ = new Ion(Zn, 2, "Zn²⁺",
            -153900, -112.1f, 20.8f, 0.0092f).register();
    public static final Chemical Zn2$_aq = new Chemical(Zn2$, AQUEOUS).register();

    public static final Ion Ag$ = new Ion(Ag, 1, "Ag⁺",
            105600, 72.7f, 20.8f, 0.0103f).register();
    public static final Chemical Ag$_aq = new Chemical(Ag$, AQUEOUS).register();

    public static final Ion Pb2$ = new Ion(Pb, 2, "Pb²⁺",
            -1700, 10.5f, 20.8f, 0.0183f).register();
    public static final Chemical Pb2$_aq = new Chemical(Pb2$, AQUEOUS).register();

    public static final Ion Mn2$ = new Ion(Mn, 2, "Mn²⁺",
            -220800, -73.6f, 20.8f, 0.0076f).register();
    public static final Chemical Mn2$_aq = new Chemical(Mn2$, AQUEOUS, 0x88FFE0E0).register();

    public static final Ion NH4$ = new Ion(new CentralAtomMolecule(N, List.of(_H, _H, _H, _H)).rename("NH₄"), 1, "NH₄⁺",
            -132500, 113.4f, 35f, 0.0230f).register();
    public static final Chemical NH4$_aq = new Chemical(NH4$, AQUEOUS).register();

    // ========== Anions ==========
    public static final Ion F__ = new Ion(F, -1, "F⁻",
            -332600, -13.8f, 20.8f, 0.0110f).register();
    public static final Chemical F__aq = new Chemical(F__, AQUEOUS).register();

    public static final Ion Cl__ = new Ion(Cl, -1, "Cl⁻",
            -167200, 56.5f, 20.8f, 0.0260f).register();
    public static final Chemical Cl__aq = new Chemical(Cl__, AQUEOUS).register();

    public static final Ion Br__ = new Ion(Br, -1, "Br⁻",
            -121600, 82.4f, 20.8f, 0.0300f).register();
    public static final Chemical Br__aq = new Chemical(Br__, AQUEOUS).register();

    public static final Ion I__ = new Ion(I, -1, "I⁻",
            -55200, 111.3f, 20.8f, 0.0400f).register();
    public static final Chemical I__aq = new Chemical(I__, AQUEOUS).register();

    public static final Ion S2__ = new Ion(S, -2, "S²⁻",
            33100, 86.2f, 20.8f, 0.0230f).register();
    public static final Chemical S2__aq = new Chemical(S2__, AQUEOUS).register();

    public static final Ion OH__ = new Ion(new CentralAtomMolecule(O, List.of(_H, LP, LP, LP)).rename("OH"), -1, "OH⁻",
            -230000, -10.75f, 29.9f, 0.001f).register();
    public static final Chemical OH__aq = new Chemical(OH__, AQUEOUS).register();
    public static final Chemical OH_l = new Chemical(OH__, MOLTEN_SALT, lucent, -220000, 92f, 60f, 0.001f).register();

    public static final Ion O2__ = new Ion(O, -2, "O²⁻",
            844000, 148f, 20.8f, 0.0110f).register();
    public static final Chemical O2_l = new Chemical(O2__, MOLTEN_SALT, lucent,
            273000, 65f, 20.8f, 0.0110f).register();

    public static final Ion NO3__ = new Ion(new CentralAtomMolecule(N, List.of($O, $O, $O)).rename("NO₃"), -1, "NO₃⁻",
            -205000, 146.4f, 100f, 0.0340f).register();
    public static final Chemical NO3__aq = new Chemical(NO3__, AQUEOUS).register();

    public static final Ion SO4_2__ = new Ion(new CentralAtomMolecule(S, List.of($O, $O, $O, $O)).rename("SO₄"), -2, "SO₄²⁻",
            -909400, 20.1f, 100f, 0.0450f).register();
    public static final Chemical SO4_2__aq = new Chemical(SO4_2__, AQUEOUS).register();
    public static final Chemical SO4_2__l = new Chemical(SO4_2__, MOLTEN_SALT, lucent,
            -909000, 19f, 100f, 0.0450f).register();

    public static final Ion HSO4__ = new Ion(new CentralAtomMolecule(S, List.of(__O, __O, __O, _OH)).rename("HSO₄"), -1, "HSO₄⁻",
            -887000, 131.8f, 100f, 0.0400f).register();
    public static final Chemical HSO4__aq = new Chemical(HSO4__, AQUEOUS).register();

    public static final Ion ClO4__ = new Ion(new CentralAtomMolecule(Cl, List.of($O, $O, $O, $O)).rename("ClO₄"), -1, "ClO₄⁻",
            -129300, 182.0f, 100f, 0.0440f).register();
    public static final Chemical ClO4__aq = new Chemical(ClO4__, AQUEOUS).register();

    public static final Ion MnO4__ = new Ion(new CentralAtomMolecule(Mn, List.of($O, $O, $O, $O)).rename("MnO₄"), -1, "MnO₄⁻",
            -541400, 191.2f, 100f, 0.0480f).register();
    public static final Chemical MnO4__aq = new Chemical(MnO4__, AQUEOUS, 0xFF9B1B8C).register();

    public static final Ion CrO4_2__ = new Ion(new CentralAtomMolecule(Cr, List.of($O, $O, $O, $O)).rename("CrO₄"), -2, "CrO₄²⁻",
            -881100, 50.2f, 100f, 0.0450f).register();
    public static final Chemical CrO4_2__aq = new Chemical(CrO4_2__, AQUEOUS, 0xFFFFCC00).register();
}