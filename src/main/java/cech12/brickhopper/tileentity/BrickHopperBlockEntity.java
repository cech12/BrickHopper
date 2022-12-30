package cech12.brickhopper.tileentity;

import cech12.brickhopper.api.blockentity.BrickHopperBlockEntities;
import cech12.brickhopper.block.BrickHopperItemHandler;
import cech12.brickhopper.config.ServerConfig;
import cech12.brickhopper.inventory.BrickHopperContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.Hopper;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.core.NonNullList;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BrickHopperBlockEntity extends RandomizableContainerBlockEntity implements Hopper {

    private ItemStackHandler inventory = new ItemStackHandler(3);
    private int transferCooldown = -1;
    private long tickedGameTime;

    public BrickHopperBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        super(BrickHopperBlockEntities.BRICK_HOPPER.get(), pos, state);
    }

    @Override
    public void load(@Nonnull CompoundTag nbt) {
        super.load(nbt);
        inventory = new ItemStackHandler(3);
        if (!this.tryLoadLootTable(nbt)) {
            this.inventory.deserializeNBT(nbt);
        }
        this.transferCooldown = nbt.getInt("TransferCooldown");
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag compound) {
        super.saveAdditional(compound);
        if (!this.trySaveLootTable(compound)) {
            compound.merge(this.inventory.serializeNBT());
        }
        compound.putInt("TransferCooldown", this.transferCooldown);
    }

    /**
     * Returns the number of slots in the inventory.
     */
    @Override
    public int getContainerSize() {
        return this.inventory.getSlots();
    }

    @Override
    @Nonnull
    protected NonNullList<ItemStack> getItems() {
        NonNullList<ItemStack> list = NonNullList.withSize(3, ItemStack.EMPTY);
        for (int i = 0; i < 3; i++) {
            list.set(i, inventory.getStackInSlot(i));
        }
        return list;
    }

    @Override
    protected void setItems(@Nonnull NonNullList<ItemStack> itemsIn) {
        if (itemsIn.size() == 3) {
            for (int i = 0; i < 3; i++) {
                this.inventory.setStackInSlot(i, itemsIn.get(i));
            }
        }
        //this.setChanged(); //don't set it as changed to be compatible with Canary
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    @Override
    @Nonnull
    public ItemStack removeItem(int index, int count) {
        this.unpackLootTable(null);
        ItemStack stack = this.inventory.extractItem(index, count, false);
        this.setChanged();
        return stack;
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    @Override
    @Nonnull
    public ItemStack removeItemNoUpdate(int index) {
        this.unpackLootTable(null);
        ItemStack stack = this.inventory.getStackInSlot(index);
        this.inventory.setStackInSlot(index, ItemStack.EMPTY);
        this.setChanged();
        return stack;
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    @Override
    public void setItem(int index, @Nonnull ItemStack stack) {
        this.unpackLootTable(null);
        this.inventory.setStackInSlot(index, stack);
        this.setChanged();
    }

    @Override
    @Nonnull
    protected Component getDefaultName() {
        return Component.translatable("block.brickhopper.brick_hopper");
    }

    public static void tick(Level level, BlockPos pos, BlockState state, BrickHopperBlockEntity entity) {
        if (level != null && !level.isClientSide) {
            entity.transferCooldown--;
            entity.tickedGameTime = level.getGameTime();
            if (!entity.isOnTransferCooldown()) {
                entity.setTransferCooldown(0);
                entity.updateHopper(entity::pullItems);
            }
        }
    }

    private void updateHopper(Supplier<Boolean> p_200109_1_) {
        if (this.level != null && !this.level.isClientSide) {
            if (!this.isOnTransferCooldown() && this.getBlockState().getValue(HopperBlock.ENABLED)) {
                boolean flag = false;
                if (!this.isEmpty()) {
                    flag = this.transferItemsOut();
                }
                if (isNotFull(this.inventory)) {
                    flag |= p_200109_1_.get();
                }
                if (flag) {
                    this.setTransferCooldown(ServerConfig.BRICK_HOPPER_COOLDOWN.get());
                    this.setChanged();
                }
            }
        }
    }

    private static ItemStack putStackInInventoryAllSlots(BlockEntity source, Object destination, IItemHandler destInventory, ItemStack stack) {
        for (int slot = 0; slot < destInventory.getSlots() && !stack.isEmpty(); slot++) {
            stack = insertStack(source, destination, destInventory, stack, slot);
        }
        return stack;
    }

    private static ItemStack insertStack(BlockEntity source, Object destination, IItemHandler destInventory, ItemStack stack, int slot) {
        ItemStack itemstack = destInventory.getStackInSlot(slot);
        if (destInventory.insertItem(slot, stack, true) != stack) {
            boolean insertedItem = false;
            boolean inventoryWasEmpty = isEmpty(destInventory);
            if (itemstack.isEmpty()) {
                destInventory.insertItem(slot, stack, false);
                stack = ItemStack.EMPTY;
                insertedItem = true;
            } else if (ItemHandlerHelper.canItemStacksStack(itemstack, stack)) {
                int originalSize = stack.getCount();
                stack = destInventory.insertItem(slot, stack, false);
                insertedItem = originalSize < stack.getCount();
            }
            if (insertedItem) {
                if (inventoryWasEmpty && destination instanceof BrickHopperBlockEntity) {
                    BrickHopperBlockEntity destinationHopper = (BrickHopperBlockEntity)destination;
                    if (!destinationHopper.mayTransfer()) {
                        int k = 0;
                        if (source instanceof BrickHopperBlockEntity) {
                            if (destinationHopper.getLastUpdateTime() >= ((BrickHopperBlockEntity) source).getLastUpdateTime()) {
                                k = 1;
                            }
                        }
                        destinationHopper.setTransferCooldown(ServerConfig.BRICK_HOPPER_COOLDOWN.get() - k);
                    }
                }
            }
        }
        return stack;
    }

    private static Optional<Pair<IItemHandler, Object>> getItemHandler(BrickHopperBlockEntity hopper, Direction hopperFacing) {
        double x = hopper.getLevelX() + (double) hopperFacing.getStepX();
        double y = hopper.getLevelY() + (double) hopperFacing.getStepY();
        double z = hopper.getLevelZ() + (double) hopperFacing.getStepZ();
        return getItemHandler(hopper.getLevel(), x, y, z, hopperFacing.getOpposite());
    }

    public static Optional<Pair<IItemHandler, Object>> getItemHandler(Level level, double x, double y, double z, final Direction side) {
        int i = Mth.floor(x);
        int j = Mth.floor(y);
        int k = Mth.floor(z);
        BlockPos blockpos = new BlockPos(i, j, k);
        BlockState state = level.getBlockState(blockpos);
        if (state.hasBlockEntity()) {
            BlockEntity blockEntity = level.getBlockEntity(blockpos);
            if (blockEntity != null) {
                return blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, side)
                        .map(capability -> ImmutablePair.of(capability, blockEntity));
            }
        }
        //support vanilla inventory blocks without IItemHandler
        Block block = state.getBlock();
        if (block instanceof WorldlyContainerHolder) {
            return Optional.of(ImmutablePair.of(new SidedInvWrapper(((WorldlyContainerHolder)block).getContainer(state, level, blockpos), side), state));
        }
        //get entities with item handlers
        List<Entity> list = level.getEntities((Entity)null,
                new AABB(x - 0.5D, y - 0.5D, z - 0.5D, x + 0.5D, y + 0.5D, z + 0.5D),
                (entity) -> !(entity instanceof LivingEntity) && entity.isAlive() && entity.getCapability(ForgeCapabilities.ITEM_HANDLER, side).isPresent());
        if (!list.isEmpty()) {
            Entity entity = list.get(level.random.nextInt(list.size()));
            return entity.getCapability(ForgeCapabilities.ITEM_HANDLER, side)
                    .map(capability -> ImmutablePair.of(capability, entity));
        }
        return Optional.empty();
    }

    private static boolean isNotFull(IItemHandler itemHandler) {
        for (int slot = 0; slot < itemHandler.getSlots(); slot++) {
            ItemStack stackInSlot = itemHandler.getStackInSlot(slot);
            if (stackInSlot.isEmpty() || stackInSlot.getCount() < itemHandler.getSlotLimit(slot)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isEmpty(IItemHandler itemHandler) {
        for (int slot = 0; slot < itemHandler.getSlots(); slot++) {
            ItemStack stackInSlot = itemHandler.getStackInSlot(slot);
            if (stackInSlot.getCount() > 0) {
                return false;
            }
        }
        return true;
    }

    private boolean transferItemsOut() {
        Direction hopperFacing = this.getBlockState().getValue(HopperBlock.FACING);
        return getItemHandler(this, hopperFacing)
                .map(destinationResult -> {
                    IItemHandler itemHandler = destinationResult.getKey();
                    Object destination = destinationResult.getValue();
                    if (isNotFull(itemHandler)) {
                        for (int i = 0; i < this.getContainerSize(); ++i) {
                            if (!this.getItem(i).isEmpty()) {
                                ItemStack originalSlotContents = this.getItem(i).copy();
                                ItemStack insertStack = this.removeItem(i, 1);
                                ItemStack remainder = putStackInInventoryAllSlots(this, destination, itemHandler, insertStack);
                                if (remainder.isEmpty()) {
                                    return true;
                                }
                                this.setItem(i, originalSlotContents);
                            }
                        }

                    }
                    return false;
                })
                .orElse(false);
    }

    /**
     * Pull dropped EntityItems from the world above the hopper and items
     * from any inventory attached to this hopper into the hopper's inventory.
     *
     * @return whether any items were successfully added to the hopper
     */
    public boolean pullItems() {
        return getItemHandler(this, Direction.UP)
                .map(itemHandlerResult -> {
                    //get item from item handler
                    if (ServerConfig.BRICK_HOPPER_PULL_ITEMS_FROM_INVENTORIES_ENABLED.get()) {
                        IItemHandler handler = itemHandlerResult.getKey();
                        for (int i = 0; i < handler.getSlots(); i++) {
                            ItemStack extractItem = handler.extractItem(i, 1, true);
                            if (!extractItem.isEmpty()) {
                                for (int j = 0; j < this.getContainerSize(); j++) {
                                    ItemStack destStack = this.getItem(j);
                                    if (this.canPlaceItem(j, extractItem) && (destStack.isEmpty() || destStack.getCount() < destStack.getMaxStackSize()
                                            && destStack.getCount() < this.getMaxStackSize() && ItemHandlerHelper.canItemStacksStack(extractItem, destStack))) {
                                        extractItem = handler.extractItem(i, 1, false);
                                        if (destStack.isEmpty()) {
                                            this.setItem(j, extractItem);
                                        } else {
                                            destStack.grow(1);
                                            this.setItem(j, destStack);
                                        }
                                        this.setChanged();
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                    return false;
                }).orElseGet(() -> {
                    //capture item
                    if (ServerConfig.BRICK_HOPPER_PULL_ITEMS_FROM_WORLD_ENABLED.get()) {
                        for (ItemEntity itementity : getCaptureItems(this)) {
                            if (captureItem(this, itementity)) {
                                return true;
                            }
                        }
                    }
                    return false;
                });
    }

    public boolean captureItem(Container p_200114_0_, ItemEntity p_200114_1_) {
        boolean flag = false;
        ItemStack itemstack = p_200114_1_.getItem().copy();
        ItemStack itemstack1 = putStackInInventoryAllSlots(null, p_200114_0_, this.inventory, itemstack);
        if (itemstack1.isEmpty()) {
            flag = true;
            p_200114_1_.remove(Entity.RemovalReason.DISCARDED);
        } else {
            p_200114_1_.setItem(itemstack1);
        }
        return flag;
    }

    public static List<ItemEntity> getCaptureItems(BrickHopperBlockEntity p_200115_0_) {
        return p_200115_0_.getSuckShape().toAabbs().stream().flatMap((p_200110_1_) -> {
            return p_200115_0_.getLevel().getEntitiesOfClass(ItemEntity.class, p_200110_1_.move(p_200115_0_.getLevelX() - 0.5D, p_200115_0_.getLevelY() - 0.5D, p_200115_0_.getLevelZ() - 0.5D), EntitySelector.ENTITY_STILL_ALIVE).stream();
        }).collect(Collectors.toList());
    }

    /**
     * Gets the world X position for this hopper entity.
     */
    @Override
    public double getLevelX() {
        return (double)this.worldPosition.getX() + 0.5D;
    }

    /**
     * Gets the world Y position for this hopper entity.
     */
    @Override
    public double getLevelY() {
        return (double)this.worldPosition.getY() + 0.5D;
    }

    /**
     * Gets the world Z position for this hopper entity.
     */
    @Override
    public double getLevelZ() {
        return (double)this.worldPosition.getZ() + 0.5D;
    }

    public void setTransferCooldown(int ticks) {
        this.transferCooldown = ticks;
    }

    private boolean isOnTransferCooldown() {
        return this.transferCooldown > 0;
    }

    public boolean mayTransfer() {
        return this.transferCooldown > ServerConfig.BRICK_HOPPER_COOLDOWN.get();
    }

    public void onEntityCollision(Entity p_200113_1_) {
        if (ServerConfig.BRICK_HOPPER_PULL_ITEMS_FROM_WORLD_ENABLED.get()) {
            if (p_200113_1_ instanceof ItemEntity) {
                BlockPos blockpos = this.getBlockPos();
                if (Shapes.joinIsNotEmpty(Shapes.create(p_200113_1_.getBoundingBox().move(-blockpos.getX(), -blockpos.getY(), -blockpos.getZ())), this.getSuckShape(), BooleanOp.AND)) {
                    this.updateHopper(() -> captureItem(this, (ItemEntity)p_200113_1_));
                }
            }
        }
    }

    @Override
    @Nonnull
    protected AbstractContainerMenu createMenu(int id, @Nonnull Inventory player) {
        return new BrickHopperContainer(id, player, this);
    }

    @Override
    @Nonnull
    protected net.minecraftforge.items.IItemHandler createUnSidedHandler() {
        return new BrickHopperItemHandler(this);
    }

    public long getLastUpdateTime() {
        return this.tickedGameTime;
    }
}
