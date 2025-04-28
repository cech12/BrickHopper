package de.cech12.brickhopper.item;

import de.cech12.brickhopper.platform.Services;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class BrickHopperItem extends BlockItem {

    public BrickHopperItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull Item.TooltipContext context, @NotNull TooltipDisplay display, @NotNull Consumer<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, context, display, tooltip, flagIn);
        if (!Services.CONFIG.isPullItemsFromWorldEnabled()) {
            tooltip.accept(Component.translatable("block.brickhopper.brick_hopper.desc.cannotAbsorbItemsFromWorld").withStyle(ChatFormatting.RED));
        }
        if (!Services.CONFIG.isPullItemsFromInventoriesEnabled()) {
            tooltip.accept(Component.translatable("block.brickhopper.brick_hopper.desc.cannotAbsorbItemsFromInventories").withStyle(ChatFormatting.RED));
        }
    }

}
