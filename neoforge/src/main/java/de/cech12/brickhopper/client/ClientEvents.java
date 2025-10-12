package de.cech12.brickhopper.client;

import de.cech12.brickhopper.Constants;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid= Constants.MOD_ID)
public class ClientEvents {

    @SubscribeEvent
    public static void onMenuScreenRegister(RegisterMenuScreensEvent event) {
        event.register(Constants.BRICK_HOPPER_MENU_TYPE.get(), BrickHopperScreen::new);
    }

}
