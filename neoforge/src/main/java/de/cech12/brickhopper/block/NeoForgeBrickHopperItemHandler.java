package de.cech12.brickhopper.block;

import de.cech12.brickhopper.platform.Services;
import de.cech12.brickhopper.blockentity.NeoForgeBrickHopperBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;

public class NeoForgeBrickHopperItemHandler extends InvWrapper
{
    private final NeoForgeBrickHopperBlockEntity hopper;

    public NeoForgeBrickHopperItemHandler(NeoForgeBrickHopperBlockEntity hopper) {
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
