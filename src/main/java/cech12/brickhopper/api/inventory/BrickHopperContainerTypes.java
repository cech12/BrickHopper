package cech12.brickhopper.api.inventory;

import cech12.brickhopper.BrickHopperMod;
import cech12.brickhopper.inventory.BrickHopperContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraftforge.common.extensions.IForgeMenuType;

public class BrickHopperContainerTypes {

    public final static ResourceLocation BRICK_HOPPER_ID = new ResourceLocation(BrickHopperMod.MOD_ID, "brickhopper");

    public static MenuType<? extends AbstractContainerMenu> BRICK_HOPPER = IForgeMenuType.create((pWindowID, pInventory, pData) -> {
        BlockPos pos = pData.readBlockPos();
        return new BrickHopperContainer(pWindowID, pInventory, pos);
    }).setRegistryName(BRICK_HOPPER_ID);

}
