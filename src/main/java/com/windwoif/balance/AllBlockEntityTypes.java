package com.windwoif.balance;

import com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedCogRenderer;
import com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedCogVisual;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.windwoif.balance.content.pump.PumpBlockEntity;
import com.windwoif.balance.content.valve.ValveBlockEntity;

import static com.windwoif.balance.Balance.REGISTRATE;

public class AllBlockEntityTypes {
    public static final BlockEntityEntry<PumpBlockEntity> PUMP_BLOCK_ENTITY = REGISTRATE
            .blockEntity("pump", PumpBlockEntity::new)
            .visual(() -> EncasedCogVisual::small, false)
            .validBlocks(AllBlocks.PUMP)
            .renderer(() -> EncasedCogRenderer::small)
            .register();
    public static final BlockEntityEntry<ValveBlockEntity> VALVE_BLOCK_ENTITY = REGISTRATE
            .blockEntity("valve", ValveBlockEntity::new)
            .validBlocks(AllBlocks.VALVE_BLOCK)
            .register();
    public static void init() {}

}
