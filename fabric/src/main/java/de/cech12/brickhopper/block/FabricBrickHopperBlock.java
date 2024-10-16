package de.cech12.brickhopper.block;

import de.cech12.brickhopper.FabricBrickHopperMod;
import de.cech12.brickhopper.blockentity.BrickHopperBlockEntity;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class FabricBrickHopperBlock extends BrickHopperBlock {

    public FabricBrickHopperBlock(Properties properties) {
        super(properties);
    }

    @Override
    @NotNull
    public InteractionResult useWithoutItem(@NotNull BlockState state, Level worldIn, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hit) {
        if (worldIn.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity blockEntity = worldIn.getBlockEntity(pos);
            if (blockEntity instanceof BrickHopperBlockEntity container) {
                player.openMenu(new ExtendedScreenHandlerFactory<>() {
                    @Override
                    public Object getScreenOpeningData(ServerPlayer player) {
                        return new FabricBrickHopperMod.HopperData(false);
                    }

                    @NotNull
                    @Override
                    public Component getDisplayName(){
                        return container.getDisplayName();
                    }

                    @Override
                    public AbstractContainerMenu createMenu(int windowId, @NotNull Inventory inventory, @NotNull Player player) {
                        return container.createMenu(windowId, inventory, player);
                    }
                });
                player.awardStat(Stats.INSPECT_HOPPER);
            }
            return InteractionResult.CONSUME;
        }
    }
}
