package com.windwoif.balance.content.reactors.reactorEntity;

import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.api.schematic.requirement.SpecialEntityItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.windwoif.balance.AllEntityTypes;
import com.windwoif.balance.content.reactors.connection.ReactorConnection;
import com.windwoif.balance.content.reactors.connection.ReactorConnectionManager;
import com.windwoif.balance.content.reactors.reactorCore.Phase;
import com.windwoif.balance.content.reactors.reactorCore.Reactor;
import com.windwoif.balance.content.recipe.chemical.Chemical;
import com.windwoif.balance.content.recipe.material.FluidMaterial;
import com.windwoif.balance.content.recipe.material.ItemMaterial;
import com.windwoif.balance.dimension.Atmosphere;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.network.NetworkHooks;

import java.util.*;

import static com.windwoif.balance.dimension.DimensionAtmosphere.getAtmosphere;
import static net.minecraft.world.item.Items.CLOCK;
import static net.minecraft.world.item.Items.FLINT_AND_STEEL;

public class ReactorEntity extends Entity implements IEntityAdditionalSpawnData, SpecialEntityItemRequirement {
    private static final EntityDataAccessor<CompoundTag> DATA_PHASES =
            SynchedEntityData.defineId(ReactorEntity.class, EntityDataSerializers.COMPOUND_TAG);
    private static final EntityDataAccessor<Boolean> BURNING = SynchedEntityData.defineId(
            ReactorEntity.class, EntityDataSerializers.BOOLEAN);

    private Reactor reactor;
    private List<PhaseData> clientPhases = Collections.emptyList();
    final Atmosphere atmosphere;
    private long lastConnectionRefresh = 0;
    private static final long CONNECTION_REFRESH_INTERVAL = 20;
    private float burnStrength = 0f;

    public ReactorEntity(EntityType<?> type, Level world) {
        super(type, world);
        atmosphere = getAtmosphere(world.dimension());
        if (!level().isClientSide) {
            updateClientPhases();
        }
    }

    public ReactorEntity(Level world, AABB boundingBox) {
        this(AllEntityTypes.REACTOR_ENTITY.get(), world);
        setBoundingBox(boundingBox);
        resetPositionToBB();
        this.reactor = new Reactor(getHeight(), getArea(), atmosphere, 10000);
        if (!level().isClientSide) {
            updateClientPhases();
        }
    }

    public void resetPositionToBB() {
        AABB bb = getBoundingBox();
        setPosRaw(bb.getCenter().x, bb.minY, bb.getCenter().z);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

    @Override
    public void tick() {
        xRotO = getXRot();
        yRotO = getYRot();
        walkDistO = walkDist;
        xo = getX();
        yo = getY();
        zo = getZ();

        if (getBoundingBox().getXsize() == 0) {
            discard();
            return;
        }

        if (!level().isClientSide) {
            reactor.tick(0.05f);
            boolean burning = reactor.isBurning();
            this.entityData.set(BURNING, burning);
            updateClientPhases();

            long now = level().getGameTime();
            if (now - lastConnectionRefresh >= CONNECTION_REFRESH_INTERVAL) {
                updateConnections();
                refreshAtmosphereConnections();
                absorbItems();
                lastConnectionRefresh = now;
            }
        } else {
            updateBurnStrength();
        }
    }

    private void absorbItems() {
        AABB absorbBox = getBoundingBox();
        List<ItemEntity> items = level().getEntitiesOfClass(ItemEntity.class, absorbBox);
        for (ItemEntity item : items) {
            ItemStack stack = item.getItem();
            Optional<Map<Chemical, Long>> composition = ItemMaterial.resolveItem(stack);
            composition.ifPresent(chemicalLongMap -> {
                int count = stack.getCount();
                chemicalLongMap.forEach((chem, amountPerItem) -> {
                    long totalAmount = amountPerItem * count;
                    reactor.changeChemical(chem, totalAmount, atmosphere.temperature());
                });
            });
            item.discard();
        }
    }

    public void addChemical(Chemical chemical, long amount) {
        if (reactor != null) reactor.changeChemical(chemical, amount, atmosphere.temperature());
    }

    private int getHeight() {
        AABB bb = getBoundingBox();
        return (int) bb.getYsize();
    }

    private int getArea() {
        AABB bb = getBoundingBox();
        return (int) (bb.getXsize() * bb.getZsize());
    }

    public double getTotalVolume() {
        return reactor.getTotalVolume();
    }

    public Reactor getReactor() {
        return reactor;
    }

    public List<PhaseData> getPhaseData() {
        if (level().isClientSide) {
            CompoundTag wrapper = this.entityData.get(DATA_PHASES);
            if (wrapper.contains("Phases")) {
                ListTag list = wrapper.getList("Phases", Tag.TAG_COMPOUND);
                List<PhaseData> phases = new ArrayList<>(list.size());
                for (Tag tag : list) {
                    phases.add(PhaseData.fromNBT((CompoundTag) tag));
                }
                Collections.reverse(phases);
                clientPhases = phases;
            }
            return clientPhases;
        }
        return Collections.emptyList();
    }

    public boolean isBurning() {
        return this.entityData.get(BURNING);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (level().isClientSide) {
            return InteractionResult.SUCCESS;
        }

        ItemStack stack = player.getItemInHand(hand);
        if (stack.isEmpty()) {
            return InteractionResult.PASS;
        }

        if (stack.is(FLINT_AND_STEEL)) {
            reactor.light();
            return InteractionResult.SUCCESS;
        }

        if (stack.is(CLOCK)) {
            player.displayClientMessage(Component.literal(reactor.getPressure() + "-" + reactor.getTemperature()), true);
            return InteractionResult.SUCCESS;
        }

        Optional<Map<Chemical, Long>> composition = ItemMaterial.resolveItem(stack);
        if (composition.isPresent()) {
            composition.get().forEach((chem, amount) -> {
                if (reactor != null) reactor.changeChemical(chem, amount, atmosphere.temperature());
            });
            stack.shrink(1);
            player.displayClientMessage(Component.literal("Item decomposed"), true);
            return InteractionResult.SUCCESS;
        }

        IFluidHandlerItem fluidHandler = FluidUtil.getFluidHandler(stack).orElse(null);
        if (fluidHandler != null) {
            FluidStack fluidStack = fluidHandler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.EXECUTE);
            composition = FluidMaterial.resolveFluid(fluidStack);
            if (composition.isPresent()) {
                composition.get().forEach((chem, amount) -> {
                    if (reactor != null) reactor.changeChemical(chem, amount * fluidStack.getAmount(), atmosphere.temperature());
                });
                player.displayClientMessage(Component.literal("Item decomposed"), true);
                ItemStack newStack = fluidHandler.getContainer();
                if (!stack.isEmpty()) {
                    player.setItemInHand(hand, newStack);
                }
                return InteractionResult.SUCCESS;
            }
        }

        player.displayClientMessage(Component.literal("Cannot decompose this item"), true);
        return InteractionResult.FAIL;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_PHASES, new CompoundTag());
        this.entityData.define(BURNING, false);
    }

    private void updateClientPhases() {
        if (level().isClientSide || reactor == null) return;

        ListTag phasesList = new ListTag();
        for (Phase phase : reactor.getSortedPhases()) {
            phase.updateVolume();
            PhaseData data = new PhaseData(
                    phase.getState(),
                    phase.getVolume(),
                    phase.getRenderColor()
            );
            phasesList.add(data.toNBT());
        }
        CompoundTag wrapper = new CompoundTag();
        wrapper.put("Phases", phasesList);
        this.entityData.set(DATA_PHASES, wrapper);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        Vec3 pos = position();
        writeBoundingBox(compound, getBoundingBox().move(pos.scale(-1)));
        if (reactor != null) {
            compound.put("ReactorData", reactor.serializeNBT());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        Vec3 pos = position();
        setBoundingBox(readBoundingBox(compound).move(pos));
        this.reactor = new Reactor(getHeight(), getArea(), atmosphere, 10000);
        if (compound.contains("ReactorData", Tag.TAG_COMPOUND)) {
            reactor.deserializeNBT(compound.getCompound("ReactorData"));
        }
        if (!level().isClientSide) {
            updateClientPhases();
        }
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        CompoundTag compound = new CompoundTag();
        addAdditionalSaveData(compound);
        buffer.writeNbt(compound);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        readAdditionalSaveData(additionalData.readNbt());
    }

    public static void writeBoundingBox(CompoundTag compound, AABB bb) {
        compound.put("From", VecHelper.writeNBT(new Vec3(bb.minX, bb.minY, bb.minZ)));
        compound.put("To", VecHelper.writeNBT(new Vec3(bb.maxX, bb.maxY, bb.maxZ)));
    }

    public static AABB readBoundingBox(CompoundTag compound) {
        Vec3 from = VecHelper.readNBT(compound.getList("From", Tag.TAG_DOUBLE));
        Vec3 to = VecHelper.readNBT(compound.getList("To", Tag.TAG_DOUBLE));
        return new AABB(from, to);
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public void setPos(double x, double y, double z) {
        AABB bb = getBoundingBox();
        setPosRaw(x, y, z);
        Vec3 center = bb.getCenter();
        setBoundingBox(bb.move(-center.x, -bb.minY, -center.z).move(x, y, z));
    }

    @Override
    public void move(MoverType typeIn, Vec3 pos) {
        if (!level().isClientSide && isAlive() && pos.lengthSqr() > 0.0D) {
            discard();
        }
    }

    @Override
    public void push(double x, double y, double z) {
        if (!level().isClientSide && isAlive() && x * x + y * y + z * z > 0.0D) {
            discard();
        }
    }

    @Override
    protected float getEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return 0.0F;
    }

    public void playPlaceSound() {
        AllSoundEvents.SLIME_ADDED.playFrom(this, 0.5F, 0.75F);
    }

    @Override
    public void push(Entity entityIn) {
        super.push(entityIn);
    }

    @Override
    protected boolean repositionEntityAfterLoad() {
        return false;
    }

    @Override
    public float rotate(Rotation transformRotation) {
        AABB bb = getBoundingBox().move(position().scale(-1));
        if (transformRotation == Rotation.CLOCKWISE_90 || transformRotation == Rotation.COUNTERCLOCKWISE_90) {
            setBoundingBox(new AABB(bb.minZ, bb.minY, bb.minX, bb.maxZ, bb.maxY, bb.maxX).move(position()));
        }
        return super.rotate(transformRotation);
    }

    @Override
    public float mirror(Mirror transformMirror) {
        return super.mirror(transformMirror);
    }

    @Override
    public void thunderHit(ServerLevel world, LightningBolt lightningBolt) {}

    @Override
    public void refreshDimensions() {}

    public static EntityType.Builder<?> build(EntityType.Builder<?> builder) {
        @SuppressWarnings("unchecked")
        EntityType.Builder<ReactorEntity> entityBuilder = (EntityType.Builder<ReactorEntity>) builder;
        return entityBuilder;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public ItemRequirement getRequiredItems() {
        return ItemRequirement.INVALID;
    }

    @Override
    public boolean isIgnoringBlockTriggers() {
        return true;
    }

    public boolean contains(BlockPos pos) {
        return getBoundingBox().contains(Vec3.atCenterOf(pos));
    }

    @Override
    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    public void setPortalEntrancePos() {
        portalEntrancePos = blockPosition();
    }

    @Override
    public PortalInfo findDimensionEntryPoint(ServerLevel pDestination) {
        return super.findDimensionEntryPoint(pDestination);
    }

    private void updateConnections() {
        if (level().isClientSide) return;
        ReactorConnectionManager manager = ReactorConnectionManager.get(level());
        if (manager == null) return;
        Set<ReactorEntity> neighbors = findAdjacentReactors();
        Set<ReactorConnection> currentConnections = new HashSet<>(manager.getConnections(this));
        for (ReactorConnection conn : currentConnections) {
            ReactorEntity other = conn.getOther(this);
            if (!neighbors.contains(other) && !conn.isMechanical()) {
                manager.removeConnection(this, other);
            }
        }
        for (ReactorEntity neighbor : neighbors) {
            if (!manager.hasConnection(this, neighbor)) {
                manager.addConnection(this, neighbor);
            }
        }
    }

    public int getSurface() {
        AABB aabb = getBoundingBox();
        int x = (int) aabb.getXsize();
        int y = (int) aabb.getYsize();
        int z = (int) aabb.getZsize();
        return 4*(x + y + z) + 2*(x*y + x*z + y*z);
    }

    private void refreshAtmosphereConnections() {
        ReactorConnectionManager manager = ReactorConnectionManager.get(level());
        if (manager == null) return;

        Set<ReactorConnection> existing = new HashSet<>(manager.getConnections(this));
        for (ReactorConnection conn : existing) {
            if (conn.isAtmosphereConnection()) {
                manager.removeConnection(conn);
            }
        }

        AABB bb = getBoundingBox();
        int minX = (int) Math.floor(bb.minX);
        int maxX = (int) Math.ceil(bb.maxX) - 1;
        int minY = (int) Math.floor(bb.minY);
        int maxY = (int) Math.ceil(bb.maxY) - 1;
        int minZ = (int) Math.floor(bb.minZ);
        int maxZ = (int) Math.ceil(bb.maxZ) - 1;

        java.util.function.BiPredicate<BlockPos, ReactorEntity> isOccupiedByOtherReactor = (pos, self) -> {
            AABB blockBox = new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
            return level().getEntitiesOfClass(ReactorEntity.class, blockBox)
                    .stream().anyMatch(e -> e != self);
        };

        java.util.function.BiPredicate<BlockPos, ReactorEntity> isExposed = (pos, self) -> {
            if (!level().getBlockState(pos).isAir()) return false;
            return !isOccupiedByOtherReactor.test(pos, self);
        };

        for (int y = minY; y <= maxY; y++) {
            int exposedCount = 0;
            for (int x = minX; x <= maxX; x++) {
                BlockPos northPos = new BlockPos(x, y, minZ - 1);
                if (isExposed.test(northPos, this)) exposedCount++;
                BlockPos southPos = new BlockPos(x, y, maxZ + 1);
                if (isExposed.test(southPos, this)) exposedCount++;
            }
            for (int z = minZ; z <= maxZ; z++) {
                BlockPos westPos = new BlockPos(minX - 1, y, z);
                if (isExposed.test(westPos, this)) exposedCount++;
                BlockPos eastPos = new BlockPos(maxX + 1, y, z);
                if (isExposed.test(eastPos, this)) exposedCount++;
            }
            if (exposedCount > 0) {
                manager.createAtmosphereConnection(this, ReactorConnection.ConnectionType.VERTICAL,
                        exposedCount, 1, y, false);
            }
        }

        int bottomExposed = 0;
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                BlockPos belowPos = new BlockPos(x, minY - 1, z);
                if (isExposed.test(belowPos, this)) bottomExposed++;
            }
        }
        if (bottomExposed > 0) {
            manager.createAtmosphereConnection(this, ReactorConnection.ConnectionType.HORIZONTAL,
                    bottomExposed, 1, minY, true);
        }

        int topExposed = 0;
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                BlockPos abovePos = new BlockPos(x, maxY + 1, z);
                if (isExposed.test(abovePos, this)) topExposed++;
            }
        }
        if (topExposed > 0) {
            manager.createAtmosphereConnection(this, ReactorConnection.ConnectionType.HORIZONTAL,
                    topExposed, 1, maxY, false);
        }
    }

    private Set<ReactorEntity> findAdjacentReactors() {
        AABB box = getBoundingBox().inflate(1);
        List<ReactorEntity> list = level().getEntitiesOfClass(ReactorEntity.class, box, e -> e != this);
        return new HashSet<>(list);
    }

    public float getBurnStrength() {
        return burnStrength;
    }

    private void updateBurnStrength() {
        float target = isBurning() ? 0.8f : 0f;
        burnStrength += (target - burnStrength) * 0.01f;
        if (Math.abs(burnStrength - target) < 0.01f) {
            burnStrength = target;
        }
    }

    @Override
    public void onRemovedFromWorld() {
        if (!level().isClientSide) {
            ReactorConnectionManager manager = ReactorConnectionManager.get(level());
            if (manager != null) {
                manager.removeAllConnections(this);
            }
        }
        super.onRemovedFromWorld();
    }
}