package cech12.brickhopper.api.inventory;

import cech12.brickhopper.BrickHopperMod;
import cech12.brickhopper.inventory.BrickHopperContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;

public class BrickHopperContainerTypes {

    public final static ResourceLocation BRICK_HOPPER_ID = new ResourceLocation(BrickHopperMod.MOD_ID, "brickhopper");

    public static ContainerType<? extends Container> BRICK_HOPPER = IForgeContainerType.create((pWindowID, pInventory, pData) -> {
        BlockPos pos = pData.readBlockPos();
        return new BrickHopperContainer(pWindowID, pInventory, pos);
    }).setRegistryName(BRICK_HOPPER_ID);

}
