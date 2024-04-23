package de.cech12.brickhopper.platform;

import de.cech12.brickhopper.Constants;
import de.cech12.brickhopper.block.BrickHopperBlock;
import de.cech12.brickhopper.blockentity.BrickHopperBlockEntity;
import de.cech12.brickhopper.inventory.BrickHopperContainer;
import de.cech12.brickhopper.platform.services.IRegistryHelper;
import de.cech12.brickhopper.blockentity.NeoForgeBrickHopperBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nonnull;

public class NeoForgeRegistryHelper implements IRegistryHelper {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Constants.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Constants.MOD_ID);
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(BuiltInRegistries.MENU, Constants.MOD_ID);

    static {
        DeferredBlock<Block> block = BLOCKS.register("brick_hopper", () -> new BrickHopperBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.5F).sound(SoundType.WOOD).noOcclusion()));
        Constants.BRICK_HOPPER_BLOCK = block;
        Constants.BRICK_HOPPER_ITEM = fromBlock(block);
        Constants.BRICK_HOPPER_BLOCK_ENTITY_TYPE = BLOCK_ENTITY_TYPES.register("brick_hopper", () -> BlockEntityType.Builder.of(NeoForgeBrickHopperBlockEntity::new, Constants.BRICK_HOPPER_BLOCK.get()).build(null));
        Constants.BRICK_HOPPER_MENU_TYPE = MENU_TYPES.register("brickhopper", () -> IMenuTypeExtension.create((pWindowID, pInventory, pData) -> new BrickHopperContainer(pWindowID, pInventory)));
    }

    private static DeferredItem<Item> fromBlock(DeferredBlock<Block> block) {
        return ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
    }

    @Override
    public BlockEntityTicker<BrickHopperBlockEntity> getBlockTicker() {
        return NeoForgeBrickHopperBlockEntity::tick;
    }

    @Override
    public BrickHopperBlockEntity getNewBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new NeoForgeBrickHopperBlockEntity(pos, state);
    }

}
