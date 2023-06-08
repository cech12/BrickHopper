package cech12.brickhopper.api.block;

import cech12.brickhopper.BrickHopperMod;
import cech12.brickhopper.block.BrickHopperBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BrickHopperBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, BrickHopperMod.MOD_ID);

    public static final RegistryObject<Block> BRICK_HOPPER = BLOCKS.register("brick_hopper", () -> new BrickHopperBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).requiresCorrectToolForDrops().strength(2.0F, 6.0F).noOcclusion()));

}
