package de.cech12.brickhopper.blockentity;

import de.cech12.brickhopper.block.ForgeBrickHopperItemHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class ForgeBrickHopperBlockEntity extends BrickHopperBlockEntity {

    private ItemStackHandler inventory = new ItemStackHandler(3);

    public ForgeBrickHopperBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag nbt, @NotNull HolderLookup.Provider provider) {
        super.loadAdditional(nbt, provider);
        inventory = new ItemStackHandler(3);
        if (!this.tryLoadLootTable(nbt)) {
            this.inventory.deserializeNBT(provider, nbt);
        }
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag compound, @NotNull HolderLookup.Provider provider) {
        super.saveAdditional(compound, provider);
        if (!this.trySaveLootTable(compound)) {
            compound.merge(this.inventory.serializeNBT(provider));
        }
    }

    /**
     * Returns the number of slots in the inventory.
     */
    @Override
    public int getContainerSize() {
        return this.inventory.getSlots();
    }

    @Override
    @NotNull
    protected NonNullList<ItemStack> getItems() {
        NonNullList<ItemStack> list = NonNullList.withSize(3, ItemStack.EMPTY);
        for (int i = 0; i < 3; i++) {
            list.set(i, inventory.getStackInSlot(i));
        }
        return list;
    }

    @Override
    protected void setItems(@NotNull NonNullList<ItemStack> itemsIn) {
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
    @NotNull
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
    @NotNull
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
    public void setItem(int index, @NotNull ItemStack stack) {
        this.unpackLootTable(null);
        this.inventory.setStackInSlot(index, stack);
        this.setChanged();
    }

    @Override
    @NotNull
    protected IItemHandler createUnSidedHandler() {
        return new ForgeBrickHopperItemHandler(this);
    }

    @Override
    protected ItemStack putStackInInventoryAllSlots(BlockEntity source, Object destination, Object destInventoryObj, ItemStack stack) {
        IItemHandler destInventory = (IItemHandler) destInventoryObj;
        for (int slot = 0; slot < destInventory.getSlots() && !stack.isEmpty(); slot++) {
            stack = insertStack(source, destination, destInventory, stack, slot);
        }
        return stack;
    }

    private ItemStack insertStack(BlockEntity source, Object destination, IItemHandler destInventory, ItemStack stack, int slot) {
        ItemStack result = stack;
        if (!destInventory.insertItem(slot, stack, true).equals(stack)) {
            boolean inventoryWasEmpty = isEmpty(destInventory);
            result = destInventory.insertItem(slot, stack, false);
            if (result.getCount() < stack.getCount()) {
                updateCooldown(inventoryWasEmpty, source, destination);
            }
        }
        return result;
    }

    @Override
    protected Optional<Pair<Object, Object>> getItemHandler(Level level, double x, double y, double z, final Direction side) {
        BlockPos blockpos = BlockPos.containing(x, y, z);
        BlockState state = level.getBlockState(blockpos);
        if (state.hasBlockEntity()) {
            BlockEntity blockEntity = level.getBlockEntity(blockpos);
            if (blockEntity != null) {
                var handlerOptional = blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, side);
                if (handlerOptional.isPresent()) {
                    return handlerOptional.map(capability -> ImmutablePair.of(capability, blockEntity));
                }
                //support vanilla inventory block entities without IItemHandler
                if (blockEntity instanceof WorldlyContainer container) {
                    return Optional.of(ImmutablePair.of(new SidedInvWrapper(container, side), state));
                }
                if (blockEntity instanceof Container container) {
                    return Optional.of(ImmutablePair.of(new InvWrapper(container), state));
                }
            }
        }
        //support vanilla inventory blocks without IItemHandler
        Block block = state.getBlock();
        if (block instanceof WorldlyContainerHolder) {
            return Optional.of(ImmutablePair.of(new SidedInvWrapper(((WorldlyContainerHolder)block).getContainer(state, level, blockpos), side), state));
        }
        //get entities with item handlers
        List<Entity> list = getAllAliveEntitiesAt(level, x, y, z,
                entity -> entity instanceof Container || !(entity instanceof LivingEntity) && entity.getCapability(ForgeCapabilities.ITEM_HANDLER, side).isPresent());
        if (!list.isEmpty()) {
            Entity entity = list.get(level.random.nextInt(list.size()));
            LazyOptional<IItemHandler> cap = entity.getCapability(ForgeCapabilities.ITEM_HANDLER, side);
            if (cap.isPresent()) {
                return cap.map(capability -> ImmutablePair.of(capability, entity));
            }
            if (entity instanceof WorldlyContainer container) {
                return Optional.of(ImmutablePair.of(new SidedInvWrapper(container, side), entity));
            }
            if (entity instanceof Container containerEntity) {
                return Optional.of(ImmutablePair.of(new InvWrapper((containerEntity)), entity));
            }
        }
        return Optional.empty();
    }

    @Override
    protected boolean isNotFull(Object itemHandlerObj) {
        IItemHandler itemHandler = (IItemHandler) itemHandlerObj;
        for (int slot = 0; slot < itemHandler.getSlots(); slot++) {
            ItemStack stackInSlot = itemHandler.getStackInSlot(slot);
            if (stackInSlot.isEmpty() || stackInSlot.getCount() < itemHandler.getSlotLimit(slot)) {
                return true;
            }
        }
        return false;
    }

    private boolean isEmpty(IItemHandler itemHandler) {
        for (int slot = 0; slot < itemHandler.getSlots(); slot++) {
            ItemStack stackInSlot = itemHandler.getStackInSlot(slot);
            if (stackInSlot.getCount() > 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected boolean pullItemsFromItemHandler(Object itemHandlerObj) {
        IItemHandler handler = (IItemHandler) itemHandlerObj;
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
        return false;
    }

    @Override
    protected Object getOwnItemHandler() {
        return this.inventory;
    }

}
