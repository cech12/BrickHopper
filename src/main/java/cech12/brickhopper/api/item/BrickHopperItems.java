package cech12.brickhopper.api.item;

import cech12.brickhopper.BrickHopperMod;
import cech12.brickhopper.api.block.BrickHopperBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BrickHopperItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BrickHopperMod.MOD_ID);

    public static final RegistryObject<Item> WOODEN_HOPPER = fromBlock(BrickHopperBlocks.BRICK_HOPPER, CreativeModeTab.TAB_REDSTONE);

    private static RegistryObject<Item> fromBlock(RegistryObject<Block> block, CreativeModeTab tab) {
        return ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }

}
