package cech12.brickhopper.block;

import cech12.brickhopper.config.ServerConfig;
import cech12.brickhopper.tileentity.BrickHopperTileEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.HopperBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BrickHopperBlock extends HopperBlock {

    public BrickHopperBlock(AbstractBlock.Properties properties) {
        super(properties);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable IBlockReader worldIn, @Nonnull List<ITextComponent> tooltip, @Nonnull ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (!ServerConfig.BRICK_HOPPER_PULL_ITEMS_FROM_WORLD_ENABLED.get()) {
            tooltip.add(new TranslationTextComponent("block.brickhopper.brick_hopper.desc.cannotAbsorbItemsFromWorld").withStyle(TextFormatting.RED));
        }
        if (!ServerConfig.BRICK_HOPPER_PULL_ITEMS_FROM_INVENTORIES_ENABLED.get()) {
            tooltip.add(new TranslationTextComponent("block.brickhopper.brick_hopper.desc.cannotAbsorbItemsFromInventories").withStyle(TextFormatting.RED));
        }
    }

    @Override
    public boolean isToolEffective(BlockState state, ToolType tool) {
        return tool == ToolType.PICKAXE;
    }

    @Override
    public TileEntity newBlockEntity(@Nonnull IBlockReader worldIn) {
        return new BrickHopperTileEntity();
    }

    /**
     * Called by ItemBlocks after a block is set in the world, to allow post-place logic
     */
    @Override
    public void setPlacedBy(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull LivingEntity placer, ItemStack stack) {
        if (stack.hasCustomHoverName()) {
            TileEntity tileentity = worldIn.getBlockEntity(pos);
            if (tileentity instanceof BrickHopperTileEntity) {
                ((BrickHopperTileEntity)tileentity).setCustomName(stack.getHoverName());
            }
        }
    }

    @Override
    @Nonnull
    public ActionResultType use(@Nonnull BlockState state, World worldIn, @Nonnull BlockPos pos, @Nonnull PlayerEntity player,
                                             @Nonnull Hand handIn, @Nonnull BlockRayTraceResult hit) {
        if (worldIn.isClientSide) {
            return ActionResultType.SUCCESS;
        } else {
            TileEntity tileentity = worldIn.getBlockEntity(pos);
            if (tileentity instanceof BrickHopperTileEntity) {
                NetworkHooks.openGui((ServerPlayerEntity) player, (BrickHopperTileEntity) tileentity, pos);
                player.awardStat(Stats.INSPECT_HOPPER);
            }
            return ActionResultType.CONSUME;
        }
    }

    @Override
    public void onRemove(BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            TileEntity tileentity = worldIn.getBlockEntity(pos);
            if (tileentity instanceof BrickHopperTileEntity) {
                InventoryHelper.dropContents(worldIn, pos, (BrickHopperTileEntity)tileentity);
                worldIn.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, worldIn, pos, newState, isMoving);
        }
    }

    @Override
    public void entityInside(@Nonnull BlockState state, World worldIn, @Nonnull BlockPos pos, @Nonnull Entity entityIn) {
        TileEntity tileentity = worldIn.getBlockEntity(pos);
        if (tileentity instanceof BrickHopperTileEntity) {
            ((BrickHopperTileEntity)tileentity).onEntityCollision(entityIn);
        }
    }

}