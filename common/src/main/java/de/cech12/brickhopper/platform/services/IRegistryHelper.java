package de.cech12.brickhopper.platform.services;

import de.cech12.brickhopper.blockentity.BrickHopperBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

/**
 * Common registry helper service interface.
 */
public interface IRegistryHelper {

    BlockEntityTicker<BrickHopperBlockEntity> getBlockTicker();

    BrickHopperBlockEntity getNewBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state);

}
