package com.windwoif.equilibrium.content.valve;

import com.windwoif.equilibrium.content.reactors.connection.ReactorConnection;
import com.windwoif.equilibrium.content.reactors.connection.ReactorConnectionManager;
import com.windwoif.equilibrium.content.reactors.reactorEntity.ReactorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class ValveBlockEntity extends BlockEntity {
    private ReactorConnection connection;
    private Direction facing;
    private boolean powered;

    public ValveBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        this.facing = state.getValue(ValveBlock.FACING);
        this.powered = state.getValue(ValveBlock.POWERED);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ValveBlockEntity valve) {
        if (level.isClientSide) return;

        valve.facing = state.getValue(ValveBlock.FACING);
        valve.powered = state.getValue(ValveBlock.POWERED);

        if (valve.connection != null && !valve.isConnectionValid()) {
            valve.removeConnection();
        }

        if (valve.connection == null && valve.powered) {
            valve.tryEstablishConnection();
        }

        if (valve.connection != null) {
            valve.connection.setActive(valve.powered);
        }
    }

    private boolean isConnectionValid() {
        if (connection == null) return false;
        ReactorEntity a = connection.getLowerEntity();
        ReactorEntity b = connection.getUpperEntity();
        return a != null && a.isAlive() && b != null && b.isAlive();
    }

    private void tryEstablishConnection() {
        if (level == null || level.isClientSide) return;
        Direction facing = getBlockState().getValue(ValveBlock.FACING);
        BlockPos frontPos = worldPosition.relative(facing);
        BlockPos backPos = worldPosition.relative(facing.getOpposite());

        ReactorEntity front = findReactorAt(frontPos);
        ReactorEntity back = findReactorAt(backPos);
        if (front == null || back == null) return;

        ReactorConnectionManager manager = ReactorConnectionManager.get(level);
        if (manager == null) return;

        double globalMinY = worldPosition.getY();
        ReactorConnection.ConnectionType type;
        if (facing == Direction.UP || facing == Direction.DOWN) {
            type = ReactorConnection.ConnectionType.HORIZONTAL;
        } else {
            type = ReactorConnection.ConnectionType.VERTICAL;
        }


        this.connection = manager.designateConnection(front, back, type, 1, 1, globalMinY);
        if (this.connection != null) {
            this.connection.setActive(powered);
        }
    }

    void removeConnection() {
        if (connection != null && level instanceof ServerLevel serverLevel) {
            ReactorConnectionManager manager = ReactorConnectionManager.get(serverLevel);
            if (manager != null) {
                manager.removeConnection(connection);
            }
        }
        connection = null;
    }

    public void onRedstoneChanged(boolean powered) {
        if (connection != null) {
            connection.setActive(powered);
        }
        this.powered = powered;
    }

    private ReactorEntity findReactorAt(BlockPos pos) {
        if (level == null) return null;
        AABB box = new AABB(pos.getX(), pos.getY(), pos.getZ(),
                pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
        return level.getEntitiesOfClass(ReactorEntity.class, box)
                .stream().findFirst().orElse(null);
    }

    @Override
    public void setRemoved() {
        if (!level.isClientSide) {
            removeConnection();
        }
        super.setRemoved();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!level.isClientSide) {
            boolean currentPowered = level.hasNeighborSignal(worldPosition);
            if (currentPowered != powered) {
                powered = currentPowered;
                BlockState state = level.getBlockState(worldPosition);
                if (state.getValue(ValveBlock.POWERED) != powered) {
                    level.setBlock(worldPosition, state.setValue(ValveBlock.POWERED, powered), 3);
                }
            }
            if (connection == null && powered) {
                tryEstablishConnection();
            }
        }
    }
}