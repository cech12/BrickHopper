package cech12.brickhopper.init;

import cech12.brickhopper.BrickHopperMod;
import cech12.brickhopper.api.block.BrickHopperBlocks;
import cech12.brickhopper.api.tileentity.BrickHopperTileEntities;
import cech12.brickhopper.tileentity.BrickHopperTileEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid= BrickHopperMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModTileEntities {

    @SubscribeEvent
    public static void registerTileEntities(RegistryEvent.Register<BlockEntityType<?>> event) {
        BrickHopperTileEntities.BRICK_HOPPER = register(BrickHopperTileEntity::new, "brick_hopper", BrickHopperBlocks.BRICK_HOPPER, event);
    }

    private static <T extends BlockEntity> BlockEntityType<T> register(BlockEntityType.BlockEntitySupplier<T> supplier, String registryName, Block block, RegistryEvent.Register<BlockEntityType<?>> registryEvent) {
        BlockEntityType<T> tileEntityType = BlockEntityType.Builder.of(supplier, block).build(null);
        tileEntityType.setRegistryName(registryName);
        registryEvent.getRegistry().register(tileEntityType);
        return tileEntityType;
    }

}
