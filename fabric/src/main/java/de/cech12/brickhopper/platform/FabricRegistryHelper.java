package de.cech12.brickhopper.platform;

import de.cech12.brickhopper.blockentity.FabricBrickHopperBlockEntity;
import de.cech12.brickhopper.blockentity.BrickHopperBlockEntity;
import de.cech12.brickhopper.platform.services.IRegistryHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class FabricRegistryHelper implements IRegistryHelper {

    @Override
    public BlockEntityTicker<BrickHopperBlockEntity> getBlockTicker() {
        return FabricBrickHopperBlockEntity::tick;
    }

    @Override
    public BrickHopperBlockEntity getNewBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new FabricBrickHopperBlockEntity(pos, state);
    }

}
