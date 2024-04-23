package de.cech12.brickhopper;

import de.cech12.brickhopper.block.FabricBrickHopperBlock;
import de.cech12.brickhopper.blockentity.FabricBrickHopperBlockEntity;
import de.cech12.brickhopper.blockentity.BrickHopperBlockEntity;
import de.cech12.brickhopper.inventory.BrickHopperContainer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class FabricBrickHopperMod implements ModInitializer {

    private static final Block BRICK_HOPPER_BLOCK = Registry.register(BuiltInRegistries.BLOCK, location("brick_hopper"), new FabricBrickHopperBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.5F).sound(SoundType.WOOD).noOcclusion()));;
    private static final Item BRICK_HOPPER_ITEM = Registry.register(BuiltInRegistries.ITEM, location("brick_hopper"), new BlockItem(BRICK_HOPPER_BLOCK, new Item.Properties()));
    private static final BlockEntityType<? extends BrickHopperBlockEntity> BRICK_HOPPER_BLOCK_ENTITY_TYPE = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, location("brick_hopper"), BlockEntityType.Builder.of(FabricBrickHopperBlockEntity::new, BRICK_HOPPER_BLOCK).build(null));
    private static final MenuType<BrickHopperContainer> BRICK_HOPPER_MENU_TYPE = Registry.register(BuiltInRegistries.MENU, location("brickhopper"), new ExtendedScreenHandlerType<>((pWindowID, pInventory, pData) -> new BrickHopperContainer(pWindowID, pInventory)));

    static {
        Constants.BRICK_HOPPER_BLOCK = () -> BRICK_HOPPER_BLOCK;
        Constants.BRICK_HOPPER_ITEM = () -> BRICK_HOPPER_ITEM;
        Constants.BRICK_HOPPER_BLOCK_ENTITY_TYPE = () -> BRICK_HOPPER_BLOCK_ENTITY_TYPE;
        Constants.BRICK_HOPPER_MENU_TYPE = () -> BRICK_HOPPER_MENU_TYPE;
    }

    private static ResourceLocation location(String name) {
        return new ResourceLocation(Constants.MOD_ID, name);
    }

    @Override
    public void onInitialize() {
        CommonLoader.init();
        //Register item in the creative tab.
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.REDSTONE_BLOCKS).register(content -> {
            content.accept(Constants.BRICK_HOPPER_ITEM.get());
        });
    }

}
