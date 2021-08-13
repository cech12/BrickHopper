package cech12.brickhopper.client;

import cech12.brickhopper.BrickHopperMod;
import cech12.brickhopper.inventory.BrickHopperContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class BrickHopperScreen extends ContainerScreen<BrickHopperContainer> {
    /** The ResourceLocation containing the gui texture for the hopper */
    private static final ResourceLocation HOPPER_GUI_TEXTURE = new ResourceLocation(BrickHopperMod.MOD_ID, "textures/gui/container/brick_hopper.png");

    public BrickHopperScreen(BrickHopperContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.passEvents = false;
        this.imageHeight = 133;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@Nonnull MatrixStack matrixStack, float partialTicks, int x, int y) {
        if (this.minecraft != null) {
            this.minecraft.getTextureManager().bind(HOPPER_GUI_TEXTURE);
            int i = (this.width - this.imageWidth) / 2;
            int j = (this.height - this.imageHeight) / 2;
            this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
        }
    }
}
