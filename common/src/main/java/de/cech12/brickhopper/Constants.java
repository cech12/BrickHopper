package de.cech12.brickhopper;

import de.cech12.brickhopper.blockentity.BrickHopperBlockEntity;
import de.cech12.brickhopper.inventory.BrickHopperContainer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

/**
 * Class that contains all common constants.
 */
public class Constants {

    /** mod id */
    public static final String MOD_ID = "brickhopper";
    /** mod name*/
    public static final String MOD_NAME = "Brick Hopper";
    /** Logger instance */
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    public static Supplier<Block> BRICK_HOPPER_BLOCK;
    public static Supplier<Item> BRICK_HOPPER_ITEM;
    public static Supplier<BlockEntityType<? extends BrickHopperBlockEntity>> BRICK_HOPPER_BLOCK_ENTITY_TYPE;
    public static Supplier<MenuType<BrickHopperContainer>> BRICK_HOPPER_MENU_TYPE;

    private Constants() {}

    public static ResourceLocation id(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }

}