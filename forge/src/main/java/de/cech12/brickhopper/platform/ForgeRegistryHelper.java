package de.cech12.brickhopper.platform;

import de.cech12.brickhopper.Constants;
import de.cech12.brickhopper.block.BrickHopperBlock;
import de.cech12.brickhopper.blockentity.BrickHopperBlockEntity;
import de.cech12.brickhopper.blockentity.ForgeBrickHopperBlockEntity;
import de.cech12.brickhopper.inventory.BrickHopperContainer;
import de.cech12.brickhopper.platform.services.IRegistryHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class ForgeRegistryHelper implements IRegistryHelper {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Constants.MOD_ID);
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.Keys.MENU_TYPES, Constants.MOD_ID);

    static {
        RegistryObject<Block> block = BLOCKS.register("brick_hopper", () -> new BrickHopperBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.5F).sound(SoundType.WOOD).noOcclusion()));
        Constants.BRICK_HOPPER_BLOCK = block;
        Constants.BRICK_HOPPER_ITEM = fromBlock(block);
        Constants.BRICK_HOPPER_BLOCK_ENTITY_TYPE = BLOCK_ENTITY_TYPES.register("brick_hopper", () -> BlockEntityType.Builder.of(ForgeBrickHopperBlockEntity::new, Constants.BRICK_HOPPER_BLOCK.get()).build(null));
        Constants.BRICK_HOPPER_MENU_TYPE = MENU_TYPES.register("brickhopper", () -> IForgeMenuType.create((pWindowID, pInventory, pData) -> new BrickHopperContainer(pWindowID, pInventory)));
    }

    private static RegistryObject<Item> fromBlock(RegistryObject<Block> block) {
        return ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
    }

    @Override
    public BrickHopperBlockEntity getNewBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new ForgeBrickHopperBlockEntity(pos, state);
    }
}
