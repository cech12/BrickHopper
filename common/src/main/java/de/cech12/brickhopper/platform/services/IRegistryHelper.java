package de.cech12.brickhopper.platform.services;

import de.cech12.brickhopper.blockentity.BrickHopperBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

/**
 * Common registry helper service interface.
 */
public interface IRegistryHelper {

    BrickHopperBlockEntity getNewBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state);

}
