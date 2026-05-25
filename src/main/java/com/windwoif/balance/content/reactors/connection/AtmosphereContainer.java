package com.windwoif.balance.content.reactors.connection;

import com.mojang.logging.LogUtils;
import com.windwoif.balance.content.reactors.reactorCore.Container;
import com.windwoif.balance.content.reactors.reactorCore.Phase;
import com.windwoif.balance.content.recipe.chemical.Chemical;
import com.windwoif.balance.dimension.Atmosphere;
import net.minecraft.nbt.ListTag;
import org.slf4j.Logger;

import java.util.List;

public class AtmosphereContainer extends Container {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final Atmosphere atmosphere;
    private final Phase gasPhase = getPhase(Chemical.State.GAS);

    public AtmosphereContainer(Atmosphere atmosphere) {
        super(1, 1, atmosphere, 1000);
        this.atmosphere = atmosphere;
        atmosphere.atmosphere().forEach((key, value) -> gasPhase.getContents().put(key, (long) value));
    }

    @Override
    public void tick() {
        gasPhase.emptyRenew();
    }

    @Override
    protected void changeChemical(Chemical chemical, long amount) {}

    @Override
    public void changeChemical(Chemical chemical, long amount, float temperature) {}

    @Override
    public void changeHeat(float heat) {}

    @Override
    public long tryChangeChemical(Chemical chemical, long amount, float temperature) {
        return -amount;
    }

    @Override
    public void LoadChemicals(ListTag chemicalsList) {}

    @Override
    public ListTag SaveChemicals() {
        return new ListTag();
    }

    @Override
    public PressureInfo getPressureInfo(double y) {
        double pressure = atmosphere.pressure();
        return new PressureInfo(pressure, gasPhase);
    }

    @Override
    public float getTemperature() {
        return atmosphere.temperature();
    }

    @Override
    public double getTotalVolume() {
        return 1e12;
    }

    @Override
    public List<Phase> getSortedPhases() {
        return List.of(gasPhase);
    }

    @Override
    public List<PhaseInterface> getPhaseInterfaces(int yStart, int yEnd) {
        return List.of();
    }
}