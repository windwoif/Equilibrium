package com.windwoif.equilibrium.content.reactors.reactorCore;

import com.windwoif.equilibrium.content.reactors.connection.ReactorConnection;
import com.windwoif.equilibrium.content.recipe.chemical.Chemical;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.windwoif.equilibrium.content.recipe.chemical.Chemical.State.*;

public class Phase {
    public Phase(Chemical.State state) {
        this.state = state;
    }
    private final Chemical.State state;
    private final Map<Chemical, Long> contents = new HashMap<>();
    private final Map<ReactorConnection, Double> connectionDemands = new HashMap<>();
    private double demandRatio = UNUPDATED;
    private double volume; //L
    private double density; //kg/L
    private double mass;
    private double pressure;
    private static final double UNUPDATED = -1;
    HashMap<Chemical, Long> RecentContents;

    public void clear(){
        contents.clear();
    }
    public boolean isEmpty(){
        return contents.isEmpty();
    }
    public Map<Chemical, Long> getContents() {
        return contents;
    }
    public void updateVolume() {
        if (state != GAS) this.volume = contents.entrySet().stream()
                .mapToDouble(a -> a.getKey().getEffectiveVolume(a.getValue())).sum();
    }
    public void setVolume(double volume) {
        this.volume = volume;
    }
    public double getVolume() {
        return volume;
    }
    public void setPressure(double pressure) {
        this.pressure = pressure;
    }
    public double getPressure() {
        return pressure;
    }
    public void updateDensity() {
        density = Math.max(mass / volume, 0);
    }
    public double getDensity() {
        return density;
    }
    public void updateMass() {
        mass = contents.entrySet().stream().mapToDouble(a -> a.getKey().getMass(a.getValue())).sum();
    }
    public double getMass() {
        return mass;
    }
    public double getAmountOfSubstance() {
        return contents.values().stream().mapToDouble(a -> a).sum() / 1000.0;
    }

    public double getMassFraction(Chemical chemical) {
        return chemical.getMass(contents.getOrDefault(chemical, 0L)) / mass;
    }

    public int getRenderColor() {
        if (contents.isEmpty()) return 0x00000000;

        double totalMoles = getAmountOfSubstance() * 1000;
        if (totalMoles <= 0) return 0x00000000;

        Chemical.State state = this.state;
        if (state == SOLID || state == MOLTEN_SALT || state == MOLTEN_METAL) {
            double totalR = 0, totalG = 0, totalB = 0;
            double aProd = 1.0;
            for (Map.Entry<Chemical, Long> entry : contents.entrySet()) {
                Chemical chem = entry.getKey();
                double moles = entry.getValue();
                double fraction = moles / totalMoles;

                int argb = chem.ARGB();
                float aBase = ((argb >> 24) & 0xFF) / 255.0f;
                int r = (argb >> 16) & 0xFF;
                int g = (argb >> 8) & 0xFF;
                int b = argb & 0xFF;

                aProd *= Math.pow(1 - aBase, fraction);
                totalR += r * fraction;
                totalG += g * fraction;
                totalB += b * fraction;

            }
            int r = (int) Math.round(totalR);
            int g = (int) Math.round(totalG);
            int b = (int) Math.round(totalB);
            int a = (int) Math.round(255 - aProd * 255);
            return (a << 24) | (r << 16) | (g << 8) | b;
        } else {
            Map<Chemical, Double> concentrations = getConcentrations();
            double aProd = 1.0, rProd = 1.0, gProd = 1.0, bProd = 1.0;

            for (Map.Entry<Chemical, Double> entry : concentrations.entrySet()) {
                if (entry.getValue() == 0) continue;
                Chemical chem = entry.getKey();
                double concentration = entry.getValue() /1000;
                int argb = chem.ARGB();
                float aBase = ((argb >> 24) & 0xFF) / 255.0f;
                float rBase = ((argb >> 16) & 0xFF) / 255.0f;
                float gBase = ((argb >> 8) & 0xFF) / 255.0f;
                float bBase = (argb & 0xFF) / 255.0f;

                aProd *= Math.pow(1 - aBase, state == LIQUID_POLAR || state == LIQUID_NONPOLAR? 1 : concentration);
                rProd *= Math.pow(rBase, concentration);
                gProd *= Math.pow(gBase, concentration);
                bProd *= Math.pow(bBase, concentration);

            }

            int r = (int) (rProd * 255);
            int g = (int) (gProd * 255);
            int b = (int) (bProd * 255);
            int a = (int) (255 - aProd * 255);
            return (a << 24) | (r << 16) | (g << 8) | b;
        }
    }
    private Map<Chemical, Double> getConcentrations() {
        return contents.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,
                a -> a.getValue() / volume * 1000));
    }

    public void demand(ReactorConnection connection, double amount) {
        if (!(amount >= 0)) return;
        connectionDemands.merge(connection, Math.min(amount,1e5), Double::sum);
    }
    public Map<Chemical, Long> getFlowAmount(ReactorConnection connection) {
        double demandVolume = connectionDemands.getOrDefault(connection, 0.0);
        if (demandVolume <= 0) return Map.of();

        if (demandRatio == UNUPDATED) {
            updateDemandRatio();
        }

        double allocatedVolume = demandRatio * demandVolume;
        if (allocatedVolume <= 0) return Map.of();

        double phaseVolume = getVolume();
        double volumeRatio = Math.min(1.0, allocatedVolume / phaseVolume);

        Map<Chemical, Long> result = new HashMap<>();
        for (Map.Entry<Chemical, Long> entry : contents.entrySet()) {
            long transfer = (long) Math.ceil(entry.getValue() * volumeRatio);
            if (transfer > 0) result.put(entry.getKey(), transfer);
        }
        return result;
    }
    private void updateDemandRatio() {
        demandRatio = Math.min(1, volume / connectionDemands.values().stream().mapToDouble(v -> v).sum());
    }
    public void renew() {
        connectionDemands.clear();
        demandRatio = UNUPDATED;
        RecentContents = new HashMap<>(contents);
    }
    public void emptyRenew() {
        connectionDemands.clear();
        demandRatio = 1;
    }

    public Chemical.State getState() {
        return state;
    }

    @Override
    public String toString() {
        return String.format("%s", state);
    }
}
