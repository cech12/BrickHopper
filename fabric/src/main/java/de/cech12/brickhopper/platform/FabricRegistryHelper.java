package de.cech12.brickhopper.platform;

import de.cech12.brickhopper.blockentity.BrickHopperBlockEntity;
import de.cech12.brickhopper.blockentity.FabricBrickHopperBlockEntity;
import de.cech12.brickhopper.platform.services.IRegistryHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class FabricRegistryHelper implements IRegistryHelper {

    @Override
    public BrickHopperBlockEntity getNewBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new FabricBrickHopperBlockEntity(pos, state);
    }

}
