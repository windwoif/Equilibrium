package com.windwoif.balance.content.pump;

import com.simibubi.create.content.kinetics.simpleRelays.SimpleKineticBlockEntity;
import com.windwoif.balance.content.reactors.connection.ReactorConnection;
import com.windwoif.balance.content.reactors.connection.ReactorConnectionManager;
import com.windwoif.balance.content.reactors.reactorEntity.ReactorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class PumpBlockEntity extends SimpleKineticBlockEntity {
    private ReactorConnection connection;

    public PumpBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, PumpBlockEntity be) {
        if (level.isClientSide) return;
        be.tick();
    }

    @Override
    public void tick() {
        super.tick();
        if (level == null || level.isClientSide) return;
        if (connection == null) {
            tryEstablishConnection();
        } else if (!isConnectionValid()) {
            removeConnection();
            tryEstablishConnection();
        } else {
            updateDrivingPressure();
        }
        if (connection != null) {
            connection.setActive(true);
        }
    }

    private void updateDrivingPressure() {
        connection.setDrivingPressure(getSpeed() * 4096);
    }

    private boolean isConnectionValid() {
        if (connection == null) return false;
        ReactorEntity a = connection.getLowerEntity();
        ReactorEntity b = connection.getUpperEntity();
        return a != null && a.isAlive() && b != null && b.isAlive();
    }

    private void tryEstablishConnection() {
        if (level == null || level.isClientSide) return;
        Direction facing = getBlockState().getValue(PumpBlock.FACING);
        BlockPos frontPos = worldPosition.relative(facing);
        BlockPos backPos = worldPosition.relative(facing.getOpposite());

        ReactorEntity front = findReactorAt(frontPos);
        ReactorEntity back = findReactorAt(backPos);
        if (front == null || back == null) return;

        ReactorConnectionManager manager = ReactorConnectionManager.get(level);
        if (manager == null) return;

        ReactorConnection.ConnectionType type;
        double globalMinY = worldPosition.getY();
        if (facing == Direction.UP || facing == Direction.DOWN) {
            type = ReactorConnection.ConnectionType.HORIZONTAL;
        } else {
            type = ReactorConnection.ConnectionType.VERTICAL;
        }

        this.connection = manager.designateConnection(front, back, type, 1, 1, globalMinY);
        if (this.connection != null) {
            this.connection.setActive(true);
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

    private ReactorEntity findReactorAt(BlockPos pos) {
        if (level == null) return null;
        AABB box = new AABB(pos.getX(), pos.getY(), pos.getZ(),
                pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
        return level.getEntitiesOfClass(ReactorEntity.class, box)
                .stream().findFirst().orElse(null);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        removeConnection();
    }

    @Override
    public float calculateStressApplied() {
        this.lastStressApplied = 2;
        return 2;
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return new AABB(worldPosition);
    }

    @Override
    protected boolean isNoisy() {
        return true;
    }
}