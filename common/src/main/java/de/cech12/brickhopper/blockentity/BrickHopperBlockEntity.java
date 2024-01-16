package de.cech12.brickhopper.blockentity;

import de.cech12.brickhopper.inventory.BrickHopperContainer;
import de.cech12.brickhopper.platform.Services;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.Hopper;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

import javax.annotation.Nonnull;

public abstract class BrickHopperBlockEntity extends RandomizableContainerBlockEntity implements Hopper {

    protected int transferCooldown = -1;
    protected long tickedGameTime;

    public BrickHopperBlockEntity(BlockPos pos, BlockState state) {
        super(Services.REGISTRY.getBlockEntityType(), pos, state);
    }

    @Override
    public void load(@Nonnull CompoundTag nbt) {
        super.load(nbt);
        this.transferCooldown = nbt.getInt("TransferCooldown");
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("TransferCooldown", this.transferCooldown);
    }

    @Override
    @Nonnull
    protected Component getDefaultName() {
        return Component.translatable("block.brickhopper.brick_hopper");
    }

    /**
     * Gets the world X position for this hopper entity.
     */
    @Override
    public double getLevelX() {
        return (double)this.worldPosition.getX() + 0.5D;
    }

    /**
     * Gets the world Y position for this hopper entity.
     */
    @Override
    public double getLevelY() {
        return (double)this.worldPosition.getY() + 0.5D;
    }

    /**
     * Gets the world Z position for this hopper entity.
     */
    @Override
    public double getLevelZ() {
        return (double)this.worldPosition.getZ() + 0.5D;
    }

    @Override
    @Nonnull
    protected AbstractContainerMenu createMenu(int id, @Nonnull Inventory player) {
        return new BrickHopperContainer(id, player, this);
    }

    public void setTransferCooldown(int ticks) {
        this.transferCooldown = ticks;
    }

    protected boolean isOnTransferCooldown() {
        return this.transferCooldown > 0;
    }

    public boolean mayTransfer() {
        return this.transferCooldown > Services.CONFIG.getCooldown();
    }

    protected long getLastUpdateTime() {
        return this.tickedGameTime;
    }
}
