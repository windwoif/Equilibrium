package com.windwoif.equilibrium.content.recipe.chemical.structure.atom;

import static com.windwoif.equilibrium.content.recipe.chemical.structure.Packing.*;

public class Atoms {
    public static void init() {}

    // ==================== 1 ====================
    public static final Atom H  = new Atom("H",  31,   1.008f, 130.68f, 28.8f, 0x00FFFFFF, MOLECULAR).register();
    public static final Atom He = new Atom("He", 140,  4.003f, 126.15f, 20.8f, 0x00FFFFFF, MOLECULAR).register();

    // ==================== 2 ====================
    public static final Atom Li = new Atom("Li", 152,  6.941f,  29.1f,  24.8f, 0xFFC0C0C0, BCC).register();
    public static final Atom Be = new Atom("Be", 112,  9.012f,   9.5f,  16.4f, 0xFFC0C0C0, CCP_HCP).register();
    public static final Atom B  = new Atom("B",   84, 10.811f,   5.90f, 11.1f, 0xFFA0A0A0, MOLECULAR).register();
    public static final Atom C  = new Atom("C",   77, 12.011f,   5.74f,  8.5f, 0xFF303030, DIAMOND).register();
    public static final Atom N  = new Atom("N",   71, 14.007f, 191.61f, 29.1f, 0x00FFFFFF, MOLECULAR).register();
    public static final Atom O  = new Atom("O",   66, 15.999f, 205.15f, 29.4f, 0x00FFFFFF, MOLECULAR).register();
    public static final Atom F  = new Atom("F",   57, 18.998f, 202.79f, 31.3f, 0x00FFFFFF, MOLECULAR).register();
    public static final Atom Ne = new Atom("Ne", 154, 20.180f, 146.33f, 20.8f, 0x00FFFFFF, MOLECULAR).register();

    // ==================== 3 ====================
    public static final Atom Na = new Atom("Na", 186, 22.990f,  51.3f,  28.2f, 0xFFC0C0C0, BCC).register();
    public static final Atom Mg = new Atom("Mg", 160, 24.305f,  32.7f,  24.9f, 0xFFC0C0C0, CCP_HCP).register();
    public static final Atom Al = new Atom("Al", 143, 26.982f,  28.3f,  24.2f, 0xFFC0C0C0, CCP_HCP).register();
    public static final Atom Si = new Atom("Si", 111, 28.085f,  18.82f, 20.0f, 0xFFA0A0A0, DIAMOND).register();
    public static final Atom P  = new Atom("P",  111, 30.974f,  41.1f,  23.8f, 0xFFD0B000, MOLECULAR).register();
    public static final Atom S  = new Atom("S",  104, 32.065f,  32.1f,  22.8f, 0xFFFFFF00, MOLECULAR).register();
    public static final Atom Cl = new Atom("Cl",  99, 35.45f,  223.08f, 33.9f, 0xFF00FF00, MOLECULAR).register();
    public static final Atom Ar = new Atom("Ar", 188, 39.948f, 154.84f, 20.8f, 0x00FFFFFF, MOLECULAR).register();

    // ==================== 4 ====================
    public static final Atom K  = new Atom("K",  227, 39.098f,  64.7f,  29.6f, 0xFFC0C0C0, BCC).register();
    public static final Atom Ca = new Atom("Ca", 197, 40.078f,  41.4f,  25.9f, 0xFFC0C0C0, CCP_HCP).register();
    public static final Atom Sc = new Atom("Sc", 162, 44.956f,  34.6f,  25.5f, 0xFFC0C0C0, CCP_HCP).register();
    public static final Atom Ti = new Atom("Ti", 147, 47.867f,  30.7f,  25.1f, 0xFFC0C0C0, CCP_HCP).register();
    public static final Atom V  = new Atom("V",  134, 50.942f,  28.9f,  24.9f, 0xFFC0C0C0, BCC).register();
    public static final Atom Cr = new Atom("Cr", 128, 51.996f,  23.8f,  23.3f, 0xFFC0C0C0, BCC).register();
    public static final Atom Mn = new Atom("Mn", 127, 54.938f,  32.0f,  26.3f, 0xFFC0C0C0, BCC).register();
    public static final Atom Fe = new Atom("Fe", 126, 55.845f,  27.3f,  25.1f, 0xFFC0C0C0, BCC).register();
    public static final Atom Co = new Atom("Co", 125, 58.933f,  30.0f,  24.8f, 0xFFC0C0C0, CCP_HCP).register();
    public static final Atom Ni = new Atom("Ni", 124, 58.693f,  29.9f,  26.1f, 0xFFC0C0C0, CCP_HCP).register();
    public static final Atom Cu = new Atom("Cu", 128, 63.546f,  33.2f,  24.4f, 0xFFFF7800, CCP_HCP).register();
    public static final Atom Zn = new Atom("Zn", 134, 65.380f,  41.6f,  25.4f, 0xFFC0C0C0, CCP_HCP).register();
    public static final Atom Ga = new Atom("Ga", 122, 69.723f,  40.8f,  25.9f, 0xFFC0C0C0, MOLECULAR).register();
    public static final Atom Ge = new Atom("Ge", 122, 72.630f,  31.1f,  23.2f, 0xFFA0A0A0, DIAMOND).register();
    public static final Atom As = new Atom("As", 119, 74.922f,  35.1f,  24.6f, 0xFFA0A0A0, MOLECULAR).register();
    public static final Atom Se = new Atom("Se", 116, 78.971f,  42.4f,  25.4f, 0xFFA0A0A0, MOLECULAR).register();
    public static final Atom Br = new Atom("Br", 114, 79.904f, 152.23f,  36.0f, 0xFF8B0000, MOLECULAR).register();
    public static final Atom Kr = new Atom("Kr", 202, 83.798f, 164.08f,  20.8f, 0x00FFFFFF, MOLECULAR).register();

    // ==================== 5 ====================
    public static final Atom Rb = new Atom("Rb", 248, 85.468f,  76.8f,  31.1f, 0xFFC0C0C0, BCC).register();
    public static final Atom Sr = new Atom("Sr", 215, 87.62f,   55.0f,  26.4f, 0xFFC0C0C0, CCP_HCP).register();
    public static final Atom Y  = new Atom("Y",  180, 88.906f,  44.4f,  26.5f, 0xFFC0C0C0, CCP_HCP).register();
    public static final Atom Zr = new Atom("Zr", 160, 91.224f,  39.0f,  25.4f, 0xFFC0C0C0, CCP_HCP).register();
    public static final Atom Nb = new Atom("Nb", 146, 92.906f,  36.4f,  24.6f, 0xFFC0C0C0, BCC).register();
    public static final Atom Mo = new Atom("Mo", 139, 95.950f,  28.7f,  24.1f, 0xFFC0C0C0, BCC).register();
    public static final Atom Tc = new Atom("Tc", 135, 98.0f,    33.0f,  24.3f, 0xFFC0C0C0, CCP_HCP).register();
    public static final Atom Ru = new Atom("Ru", 134, 101.070f, 28.5f,  24.1f, 0xFFC0C0C0, CCP_HCP).register();
    public static final Atom Rh = new Atom("Rh", 134, 102.906f, 31.5f,  25.0f, 0xFFC0C0C0, CCP_HCP).register();
    public static final Atom Pd = new Atom("Pd", 137, 106.420f, 37.8f,  25.9f, 0xFFC0C0C0, CCP_HCP).register();
    public static final Atom Ag = new Atom("Ag", 144, 107.868f, 42.6f,  25.4f, 0xFFC0C0C0, CCP_HCP).register();
    public static final Atom Cd = new Atom("Cd", 151, 112.414f, 51.8f,  26.0f, 0xFFC0C0C0, CCP_HCP).register();
    public static final Atom In = new Atom("In", 167, 114.818f, 57.8f,  26.7f, 0xFFC0C0C0, MOLECULAR).register();
    public static final Atom Sn = new Atom("Sn", 140, 118.710f, 51.5f,  27.0f, 0xFFC0C0C0, BCC).register();
    public static final Atom Sb = new Atom("Sb", 141, 121.760f, 45.7f,  25.2f, 0xFFC0C0C0, MOLECULAR).register();
    public static final Atom Te = new Atom("Te", 135, 127.600f, 49.7f,  25.7f, 0xFFA0A0A0, MOLECULAR).register();
    public static final Atom I  = new Atom("I",  133, 126.904f, 116.14f, 36.9f, 0xFF800080, MOLECULAR).register();
    public static final Atom Xe = new Atom("Xe", 216, 131.293f, 169.68f, 20.8f, 0x00FFFFFF, MOLECULAR).register();

    // ==================== 6 ====================
    public static final Atom Cs = new Atom("Cs", 265, 132.905f, 85.2f,  32.2f, 0xFFC0C0C0, BCC).register();
    public static final Atom Ba = new Atom("Ba", 222, 137.327f, 62.5f,  28.1f, 0xFFC0C0C0, BCC).register();
    public static final Atom La = new Atom("La", 187, 138.905f, 56.9f,  27.1f, 0xFFC0C0C0, CCP_HCP).register();
    public static final Atom Ce = new Atom("Ce", 182, 140.116f, 56.0f,  26.9f, 0xFFC0C0C0, CCP_HCP).register();
    public static final Atom Pr = new Atom("Pr", 182, 140.908f, 73.2f,  27.2f, 0xFFC0C0C0, CCP_HCP).register();
    public static final Atom Nd = new Atom("Nd", 182, 144.242f, 71.1f,  27.5f, 0xFFC0C0C0, CCP_HCP).register();
    public static final Atom Pm = new Atom("Pm", 183, 145.0f,   70.0f,  27.3f, 0xFFC0C0C0, CCP_HCP).register();
    public static final Atom Sm = new Atom("Sm", 180, 150.36f,  69.6f,  27.0f, 0xFFC0C0C0, CCP_HCP).register();
    public static final Atom Eu = new Atom("Eu", 180, 151.964f, 77.8f,  27.7f, 0xFFC0C0C0, BCC).register();
    public static final Atom Gd = new Atom("Gd", 180, 157.25f,  68.1f,  27.4f, 0xFFC0C0C0, CCP_HCP).register();
    public static final Atom Tb = new Atom("Tb", 177, 158.925f, 73.2f,  27.4f, 0xFFC0C0C0, CCP_HCP).register();
    public static final Atom Dy = new Atom("Dy", 178, 162.500f, 75.6f,  27.7f, 0xFFC0C0C0, CCP_HCP).register();
    public static final Atom Ho = new Atom("Ho", 176, 164.930f, 75.3f,  27.1f, 0xFFC0C0C0, CCP_HCP).register();
    public static final Atom Er = new Atom("Er", 176, 167.259f, 73.2f,  28.1f, 0xFFC0C0C0, CCP_HCP).register();
    public static final Atom Tm = new Atom("Tm", 175, 168.934f, 74.0f,  27.0f, 0xFFC0C0C0, CCP_HCP).register();
    public static final Atom Yb = new Atom("Yb", 170, 173.045f, 59.9f,  26.7f, 0xFFC0C0C0, CCP_HCP).register();
    public static final Atom Lu = new Atom("Lu", 174, 174.967f, 51.0f,  26.9f, 0xFFC0C0C0, CCP_HCP).register();
    public static final Atom Hf = new Atom("Hf", 159, 178.49f,  43.6f,  25.7f, 0xFFC0C0C0, CCP_HCP).register();
    public static final Atom Ta = new Atom("Ta", 146, 180.948f, 41.5f,  25.4f, 0xFFC0C0C0, BCC).register();
    public static final Atom W  = new Atom("W",  139, 183.840f, 32.6f,  24.3f, 0xFFC0C0C0, BCC).register();
    public static final Atom Re = new Atom("Re", 137, 186.207f, 36.9f,  25.5f, 0xFFC0C0C0, CCP_HCP).register();
    public static final Atom Os = new Atom("Os", 135, 190.23f,  32.6f,  24.7f, 0xFFC0C0C0, CCP_HCP).register();
    public static final Atom Ir = new Atom("Ir", 136, 192.217f, 35.5f,  25.1f, 0xFFC0C0C0, CCP_HCP).register();
    public static final Atom Pt = new Atom("Pt", 139, 195.084f, 41.6f,  25.9f, 0xFFC0C0C0, CCP_HCP).register();
    public static final Atom Au = new Atom("Au", 144, 196.967f, 47.4f,  25.4f, 0xFFFFD700, CCP_HCP).register();
    public static final Atom Hg = new Atom("Hg", 151, 200.592f, 75.9f,  27.9f, 0xFFC0C0C0, LIQUID).register();
    public static final Atom Tl = new Atom("Tl", 170, 204.38f,  64.2f,  26.3f, 0xFFC0C0C0, CCP_HCP).register();
    public static final Atom Pb = new Atom("Pb", 175, 207.200f, 64.8f,  26.4f, 0xFFC0C0C0, CCP_HCP).register();
    public static final Atom Bi = new Atom("Bi", 160, 208.980f, 56.7f,  25.5f, 0xFFC0C0C0, MOLECULAR).register();

    // ==================== 7 ====================
    public static final Atom Th = new Atom("Th", 180, 232.038f, 51.8f,  26.2f, 0xFFC0C0C0, CCP_HCP).register();
    public static final Atom U  = new Atom("U",  138, 238.029f, 50.2f,  27.7f, 0xFFC0C0C0, CCP_HCP).register();
}