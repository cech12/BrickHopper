package de.cech12.brickhopper.block;

import de.cech12.brickhopper.Constants;
import de.cech12.brickhopper.blockentity.BrickHopperBlockEntity;
import de.cech12.brickhopper.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BrickHopperBlock extends HopperBlock {

    public BrickHopperBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return Services.REGISTRY.getNewBlockEntity(pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> entityType) {
        return createTickerHelper(entityType, Constants.BRICK_HOPPER_BLOCK_ENTITY_TYPE.get(), (lev, pos, sta, entity) -> BrickHopperBlockEntity.tick(lev, entity));
    }

    @Override
    @NotNull
    public InteractionResult useWithoutItem(@NotNull BlockState state, Level worldIn, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hit) {
        if (worldIn.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity blockEntity = worldIn.getBlockEntity(pos);
            if (blockEntity instanceof BrickHopperBlockEntity) {
                player.openMenu((BrickHopperBlockEntity)blockEntity);
                player.awardStat(Stats.INSPECT_HOPPER);
            }
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public void entityInside(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Entity entity, @NotNull InsideBlockEffectApplier insideBlockEffectApplier) {
        if (Services.CONFIG.isPullItemsFromWorldEnabled() && entity instanceof ItemEntity itemEntity) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof BrickHopperBlockEntity brickHopperBlockEntity
                    && !itemEntity.getItem().isEmpty() && itemEntity.getBoundingBox().move((-pos.getX()), (-pos.getY()), (-pos.getZ())).intersects(brickHopperBlockEntity.getSuckAabb())
            ) {
                brickHopperBlockEntity.onItemEntityIsCaptured(itemEntity);
            }
        }
    }

}