package com.windwoif.balance.content.pump;

import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import static com.windwoif.balance.AllBlockEntityTypes.PUMP_BLOCK_ENTITY;

public class PumpBlock extends DirectionalKineticBlock implements IBE<PumpBlockEntity>, ICogWheel {

    public PumpBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING).getAxis();
    }

    @Override
    public boolean isLargeCog() {
        return false;
    }

    @Override
    public boolean isSmallCog() {
        return true;
    }

    @Override
    public Class<PumpBlockEntity> getBlockEntityClass() {
        return PumpBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends PumpBlockEntity> getBlockEntityType() {
        return PUMP_BLOCK_ENTITY.get();
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        if (context.getLevel().getBlockEntity(context.getClickedPos()) instanceof PumpBlockEntity pump) {
            pump.removeConnection();
        }
        return super.onWrenched(state, context);
    }
}