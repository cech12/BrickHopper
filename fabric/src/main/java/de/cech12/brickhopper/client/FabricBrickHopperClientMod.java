package de.cech12.brickhopper.client;

import de.cech12.brickhopper.Constants;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;

public class FabricBrickHopperClientMod implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        MenuScreens.register(Constants.BRICK_HOPPER_MENU_TYPE.get(), BrickHopperScreen::new);
    }

}
