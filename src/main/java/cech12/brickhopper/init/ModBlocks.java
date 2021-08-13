package cech12.brickhopper.init;

import cech12.brickhopper.BrickHopperMod;
import cech12.brickhopper.api.block.BrickHopperBlocks;
import cech12.brickhopper.block.BrickHopperBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.block.AbstractBlock;

@Mod.EventBusSubscriber(modid= BrickHopperMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModBlocks {

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        BrickHopperBlocks.BRICK_HOPPER = registerBlock("brick_hopper", ItemGroup.TAB_REDSTONE, new BrickHopperBlock(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_RED).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops().strength(2.0F, 6.0F).noOcclusion()));
    }

    public static Block registerBlock(String name, ItemGroup itemGroup, Block block) {
        Item.Properties itemProperties = new Item.Properties().tab(itemGroup);
        BlockItem itemBlock = new BlockItem(block, itemProperties);
        block.setRegistryName(name);
        itemBlock.setRegistryName(name);
        ForgeRegistries.BLOCKS.register(block);
        ForgeRegistries.ITEMS.register(itemBlock);
        return block;
    }

}