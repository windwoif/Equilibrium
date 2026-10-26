package com.windwoif.equilibrium.content.recipe.chemical.structure.ionicCompund;

import com.windwoif.equilibrium.content.recipe.chemical.Chemical;
import com.windwoif.equilibrium.content.recipe.chemical.ChemicalRegistry;
import com.windwoif.equilibrium.content.recipe.chemical.structure.Structure;
import com.windwoif.equilibrium.content.recipe.chemical.structure.atom.Atom;
import com.windwoif.equilibrium.content.recipe.chemical.structure.ion.Ion;
import com.windwoif.equilibrium.content.recipe.reaction.Reaction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.*;

import static com.windwoif.equilibrium.content.recipe.chemical.Chemical.State.*;
import static com.windwoif.equilibrium.content.recipe.reaction.generator.ReactionMath.gcd;

public class IonicCompound extends Structure<IonicCompound> {
    public static final String TYPE = "ionic";

    private static final String TAG_IONS = "Ions";
    private static final String TAG_ION = "Ion";
    private static final String TAG_COUNT = "Count";

    private final Map<Ion, Integer> composition;
    private List<Reaction> savedReactions;

    public IonicCompound(Ion cation, Ion anion,
                         Integer enthalpy, Float entropy, Float molarHeatCapacity, Float molarVolume) {
        this(buildComposition(cation, anion), enthalpy, entropy, molarHeatCapacity, molarVolume);
    }

    public IonicCompound(Map<Ion, Integer> composition) {
        this(composition, null, null, null, null);
    }

    public IonicCompound(Map<Ion, Integer> composition,
                         Integer enthalpy, Float entropy, Float molarHeatCapacity, Float molarVolume) {
        super(calcMass(composition), getName(composition),
                enthalpy != null ? enthalpy : resolveEnthalpy(composition),
                entropy != null ? entropy : resolveEntropy(composition),
                molarHeatCapacity != null ? molarHeatCapacity : predictHeatCapacity(composition),
                molarVolume != null ? molarVolume : predictVolume(composition));
        this.composition = Map.copyOf(composition);
    }

    private static float calcMass(Map<Ion, Integer> composition) {
        return (float) composition.entrySet().stream()
                .mapToDouble(e -> e.getKey().molarMass() * e.getValue())
                .sum();
    }

    private static float predictVolume(Map<Ion, Integer> composition) {
        return (float) composition.entrySet().stream()
                .mapToDouble(e -> e.getKey().molarVolume() * e.getValue())
                .sum();
    }

    private static float predictHeatCapacity(Map<Ion, Integer> composition) {
        int atomCount = composition.entrySet().stream()
                .mapToInt(e -> e.getKey().getParticle().getAtoms().values().stream()
                        .mapToInt(Integer::intValue).sum() * e.getValue())
                .sum();
        return 25f * atomCount;
    }

    private static SaltThermo.Data lookup(Map<Ion, Integer> composition) {
        Ion cation = composition.keySet().stream()
                .filter(i -> i.getCharge() > 0).findFirst().orElse(null);
        Ion anion = composition.keySet().stream()
                .filter(i -> i.getCharge() < 0).findFirst().orElse(null);
        if (cation == null || anion == null) return null;
        return SaltThermo.get(cation.getName(), anion.getName());
    }

    private static int resolveEnthalpy(Map<Ion, Integer> composition) {
        SaltThermo.Data data = lookup(composition);
        if (data != null) return data.enthalpy();
        int ionSum = composition.entrySet().stream()
                .mapToInt(e -> e.getKey().enthalpy() * e.getValue()).sum();
        return ionSum - SaltThermo.DEFAULT_DISSOLUTION_ENTHALPY;
    }

    private static float resolveEntropy(Map<Ion, Integer> composition) {
        SaltThermo.Data data = lookup(composition);
        if (data != null) return data.entropy();
        float ionSum = (float) composition.entrySet().stream()
                .mapToDouble(e -> e.getKey().entropy() * e.getValue()).sum();
        return ionSum - SaltThermo.DEFAULT_DISSOLUTION_ENTROPY;
    }

    @Override
    public Map<Atom, Integer> getAtoms() {
        Map<Atom, Integer> atoms = new HashMap<>();
        composition.forEach((ion, count) ->
                ion.getAtoms().forEach((atom, n) -> atoms.merge(atom, n * count, Integer::sum)));
        return atoms;
    }

    public static String getName(Map<Ion, Integer> composition) {
        StringBuilder sb = new StringBuilder();
        composition.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(
                        Comparator.comparingInt(Ion::getCharge).reversed()))
                .forEach(e -> {
                    sb.append(e.getKey().getParticle().getName());
                    int count = e.getValue();
                    if (count > 1) sb.append(count);
                });
        return sb.toString();
    }

    public static Map<Ion, Integer> buildComposition(Ion cation, Ion anion) {
        if (cation.getCharge() <= 0) throw new IllegalArgumentException("Cation must have positive charge: " + cation.getName());
        if (anion.getCharge() >= 0) throw new IllegalArgumentException("Anion must have negative charge: " + anion.getName());
        int[] ratio = neutralRatio(cation.getCharge(), anion.getCharge());
        return Map.of(cation, ratio[0], anion, ratio[1]);
    }

    public static int[] neutralRatio(short cationCharge, short anionCharge) {
        int a = Math.abs(cationCharge);
        int b = Math.abs(anionCharge);
        int gcd = gcd(a, b);
        return new int[]{b / gcd, a / gcd};
    }

    @Override
    protected String typeId() {
        return TYPE;
    }

    @Override
    protected void writeAdditional(CompoundTag tag) {
        ListTag ionsTag = new ListTag();
        for (Map.Entry<Ion, Integer> e : composition.entrySet()) {
            CompoundTag ionTag = new CompoundTag();
            ionTag.put(TAG_ION, e.getKey().serializeNBT());
            ionTag.putInt(TAG_COUNT, e.getValue());
            ionsTag.add(ionTag);
        }
        tag.put(TAG_IONS, ionsTag);
    }

    public static IonicCompound fromNBT(CompoundTag tag) {
        ListTag ionsTag = tag.getList(TAG_IONS, Tag.TAG_COMPOUND);
        Map<Ion, Integer> composition = new HashMap<>();
        for (int i = 0; i < ionsTag.size(); i++) {
            CompoundTag ionTag = ionsTag.getCompound(i);
            Ion ion = (Ion) Ion.fromNBT(ionTag.getCompound(TAG_ION));
            composition.put(ion, ionTag.getInt(TAG_COUNT));
        }
        return new IonicCompound(composition);
    }

    private Optional<Reaction> dissolveTo(Chemical.State state, Chemical salt) {
        Map<Chemical, Integer> reactants = new HashMap<>();
        for (Map.Entry<Ion, Integer> entry : composition.entrySet()) {
            Optional<Chemical> chemical = ChemicalRegistry.get(entry.getKey().getName() + state.suffix);
            if (chemical.isEmpty()) return Optional.empty();
            reactants.put(chemical.get(), entry.getValue());
        }
        return Optional.of(new Reaction(Map.copyOf(reactants), Map.of(salt, 1),
                40000.0, 1e6, Reaction.State.SURFACE));
    }

    public Optional<List<Reaction>> getReactions() {
        if (savedReactions == null) {
            Chemical salt = ChemicalRegistry.get(this.getName() + SOLID.suffix)
                    .orElseGet(() -> new Chemical(this, SOLID).cache());
            List<Reaction> reactions = new ArrayList<>();
            dissolveTo(AQUEOUS, salt).ifPresent(reactions::add);
            dissolveTo(MOLTEN_SALT, salt).ifPresent(reactions::add);
            savedReactions = List.copyOf(reactions);
        }
        return Optional.of(savedReactions);
    }
}