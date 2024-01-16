package de.cech12.brickhopper.block;

import de.cech12.brickhopper.blockentity.BrickHopperBlockEntity;
import de.cech12.brickhopper.platform.Services;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;

public class ForgeBrickHopperItemHandler extends InvWrapper
{
    private final BrickHopperBlockEntity hopper;

    public ForgeBrickHopperItemHandler(BrickHopperBlockEntity hopper)
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
                    hopper.setTransferCooldown(Services.CONFIG.getCooldown());
                }
            }
            return stack;
        }
    }
}
