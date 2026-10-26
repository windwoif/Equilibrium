package com.windwoif.equilibrium.content.recipe.chemical.structure.render;

import com.windwoif.equilibrium.content.recipe.chemical.structure.BondType;
import com.windwoif.equilibrium.content.recipe.chemical.structure.atom.Atom;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class RenderData {// TODO
    public final List<AtomPlacement> atoms = new ArrayList<>();
    public final List<BondPlacement> bonds = new ArrayList<>();

    public RenderData addAtom(Atom atom, Vec3 pos) {
        atoms.add(new AtomPlacement(atom, pos));
        return this;
    }

    public RenderData addBond(Vec3 start, Vec3 end, BondType type) {
        bonds.add(new BondPlacement(start, end, type));
        return this;
    }

    public record AtomPlacement(Atom atom, Vec3 position) {}
    public record BondPlacement(Vec3 start, Vec3 end, BondType type) {}
}