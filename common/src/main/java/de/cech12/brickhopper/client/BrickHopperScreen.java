package de.cech12.brickhopper.client;

import de.cech12.brickhopper.Constants;
import de.cech12.brickhopper.inventory.BrickHopperContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class BrickHopperScreen extends AbstractContainerScreen<BrickHopperContainer> {
    /** The ResourceLocation containing the gui texture for the hopper */
    private static final ResourceLocation HOPPER_GUI_TEXTURE = Constants.id("textures/gui/container/brick_hopper.png");

    public BrickHopperScreen(BrickHopperContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageHeight = 133;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTicks, int x, int y) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(HOPPER_GUI_TEXTURE, i, j, 0, 0, this.imageWidth, this.imageHeight);
    }
}
