package cech12.brickhopper.block;

import cech12.brickhopper.api.blockentity.BrickHopperBlockEntities;
import cech12.brickhopper.config.ServerConfig;
import cech12.brickhopper.tileentity.BrickHopperBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BrickHopperBlock extends HopperBlock {

    public BrickHopperBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable BlockGetter worldIn, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (!ServerConfig.BRICK_HOPPER_PULL_ITEMS_FROM_WORLD_ENABLED.get()) {
            tooltip.add(Component.translatable("block.brickhopper.brick_hopper.desc.cannotAbsorbItemsFromWorld").withStyle(ChatFormatting.RED));
        }
        if (!ServerConfig.BRICK_HOPPER_PULL_ITEMS_FROM_INVENTORIES_ENABLED.get()) {
            tooltip.add(Component.translatable("block.brickhopper.brick_hopper.desc.cannotAbsorbItemsFromInventories").withStyle(ChatFormatting.RED));
        }
    }

    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new BrickHopperBlockEntity(pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@Nonnull Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> entityType) {
        return createTickerHelper(entityType, BrickHopperBlockEntities.BRICK_HOPPER.get(), BrickHopperBlockEntity::tick);
    }

    /**
     * Called by ItemBlocks after a block is set in the world, to allow post-place logic
     */
    @Override
    public void setPlacedBy(@Nonnull Level worldIn, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull LivingEntity placer, ItemStack stack) {
        if (stack.hasCustomHoverName()) {
            BlockEntity blockEntity = worldIn.getBlockEntity(pos);
            if (blockEntity instanceof BrickHopperBlockEntity) {
                ((BrickHopperBlockEntity)blockEntity).setCustomName(stack.getHoverName());
            }
        }
    }

    @Override
    @Nonnull
    public InteractionResult use(@Nonnull BlockState state, Level worldIn, @Nonnull BlockPos pos, @Nonnull Player player,
                                             @Nonnull InteractionHand handIn, @Nonnull BlockHitResult hit) {
        if (worldIn.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity blockEntity = worldIn.getBlockEntity(pos);
            if (blockEntity instanceof BrickHopperBlockEntity) {
                NetworkHooks.openScreen((ServerPlayer) player, (BrickHopperBlockEntity) blockEntity, pos);
                player.awardStat(Stats.INSPECT_HOPPER);
            }
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public void onRemove(BlockState state, @Nonnull Level worldIn, @Nonnull BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = worldIn.getBlockEntity(pos);
            if (blockEntity instanceof BrickHopperBlockEntity) {
                Containers.dropContents(worldIn, pos, (BrickHopperBlockEntity)blockEntity);
                worldIn.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, worldIn, pos, newState, isMoving);
        }
    }

    @Override
    public void entityInside(@Nonnull BlockState state, Level worldIn, @Nonnull BlockPos pos, @Nonnull Entity entityIn) {
        BlockEntity blockEntity = worldIn.getBlockEntity(pos);
        if (blockEntity instanceof BrickHopperBlockEntity) {
            ((BrickHopperBlockEntity)blockEntity).onEntityCollision(entityIn);
        }
    }

}