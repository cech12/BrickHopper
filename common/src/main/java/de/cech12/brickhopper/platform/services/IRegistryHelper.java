package de.cech12.brickhopper.platform.services;

import de.cech12.brickhopper.blockentity.BrickHopperBlockEntity;
import de.cech12.brickhopper.inventory.BrickHopperContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

/**
 * Common registry helper service interface.
 */
public interface IRegistryHelper {

    BlockEntityTicker<BrickHopperBlockEntity> getBlockTicker();

    BlockEntityType<BrickHopperBlockEntity> getBlockEntityType();

    BrickHopperBlockEntity getNewBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state);

    MenuType<BrickHopperContainer> getMenuType();

    void onEntityCollision(@Nonnull BrickHopperBlockEntity blockEntity, @Nonnull Entity entity);

}
