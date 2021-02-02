package cech12.brickhopper.block;

import cech12.brickhopper.config.ServerConfig;
import cech12.brickhopper.tileentity.BrickHopperTileEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;

public class BrickHopperItemHandler extends InvWrapper
{
    private final BrickHopperTileEntity hopper;

    public BrickHopperItemHandler(BrickHopperTileEntity hopper)
    {
        super(hopper);
        this.hopper = hopper;
    }

    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
    {
        if (simulate) {
            return super.insertItem(slot, stack, true);
        } else {
            boolean wasEmpty = getInv().isEmpty();
            int originalStackSize = stack.getCount();
            stack = super.insertItem(slot, stack, false);
            if (wasEmpty && originalStackSize > stack.getCount())
            {
                if (!hopper.mayTransfer())
                {
                    hopper.setTransferCooldown(ServerConfig.BRICK_HOPPER_COOLDOWN.get());
                }
            }
            return stack;
        }
    }
}
