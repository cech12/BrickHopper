package cech12.brickhopper.inventory;

import cech12.brickhopper.api.inventory.BrickHopperContainerTypes;
import cech12.brickhopper.tileentity.BrickHopperTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

public class BrickHopperContainer extends Container {
    private final BrickHopperTileEntity hopper;

    public BrickHopperContainer(int id, PlayerInventory playerInventory, BrickHopperTileEntity inventory) {
        super(BrickHopperContainerTypes.BRICK_HOPPER, id);
        this.hopper = inventory;
        assertInventorySize(inventory, 3);
        inventory.openInventory(playerInventory.player);
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

    public BrickHopperContainer(int id, PlayerInventory playerInventoryIn, BlockPos pos) {
        this(id, playerInventoryIn, (BrickHopperTileEntity) playerInventoryIn.player.world.getTileEntity(pos));
    }

    /**
     * Determines whether supplied player can use this container
     */
    @Override
    public boolean canInteractWith(@Nonnull PlayerEntity playerIn) {
        return this.hopper.isUsableByPlayer(playerIn);
    }

    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
     * inventory and the other inventory(s).
     */
    @Override
    @Nonnull
    public ItemStack transferStackInSlot(@Nonnull PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index < this.hopper.getSizeInventory()) {
                if (!this.mergeItemStack(itemstack1, this.hopper.getSizeInventory(), this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, this.hopper.getSizeInventory(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    /**
     * Called when the container is closed.
     */
    @Override
    public void onContainerClosed(@Nonnull PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        this.hopper.closeInventory(playerIn);
    }
}
