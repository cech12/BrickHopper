package cech12.brickhopper.inventory;

import cech12.brickhopper.api.inventory.BrickHopperMenuTypes;
import cech12.brickhopper.tileentity.BrickHopperBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;

import javax.annotation.Nonnull;

public class BrickHopperContainer extends AbstractContainerMenu {
    private final BrickHopperBlockEntity hopper;

    public BrickHopperContainer(int id, Inventory playerInventory, BrickHopperBlockEntity inventory) {
        super(BrickHopperMenuTypes.BRICK_HOPPER.get(), id);
        this.hopper = inventory;
        checkContainerSize(inventory, 3);
        inventory.startOpen(playerInventory.player);
        //hopper slots
        for(int j = 0; j < 3; ++j) {
            this.addSlot(new Slot(inventory, j, 62 + j * 18, 20));
        }
        //inventory
        for(int l = 0; l < 3; ++l) {
            for(int k = 0; k < 9; ++k) {
                this.addSlot(new Slot(playerInventory, k + l * 9 + 9, 8 + k * 18, l * 18 + 51));
            }
        }

        for(int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(playerInventory, i1, 8 + i1 * 18, 109));
        }
    }

    public BrickHopperContainer(int id, Inventory playerInventoryIn, BlockPos pos) {
        this(id, playerInventoryIn, (BrickHopperBlockEntity) playerInventoryIn.player.level().getBlockEntity(pos));
    }

    /**
     * Determines whether supplied player can use this container
     */
    @Override
    public boolean stillValid(@Nonnull Player playerIn) {
        return this.hopper.stillValid(playerIn);
    }

    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
     * inventory and the other inventory(s).
     */
    @Override
    @Nonnull
    public ItemStack quickMoveStack(@Nonnull Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < this.hopper.getContainerSize()) {
                if (!this.moveItemStackTo(itemstack1, this.hopper.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, this.hopper.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    /**
     * Called when the container is closed.
     */
    @Override
    public void removed(@Nonnull Player playerIn) {
        super.removed(playerIn);
        this.hopper.stopOpen(playerIn);
    }
}
