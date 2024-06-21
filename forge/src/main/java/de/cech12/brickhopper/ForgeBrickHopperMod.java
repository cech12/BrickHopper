package de.cech12.brickhopper;

import de.cech12.brickhopper.client.BrickHopperScreen;
import de.cech12.brickhopper.platform.ForgeRegistryHelper;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@SuppressWarnings("unused")
@Mod(Constants.MOD_ID)
@Mod.EventBusSubscriber(modid= Constants.MOD_ID, bus= Mod.EventBusSubscriber.Bus.MOD)
public class ForgeBrickHopperMod {

    public ForgeBrickHopperMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ForgeRegistryHelper.BLOCKS.register(modEventBus);
        ForgeRegistryHelper.ITEMS.register(modEventBus);
        ForgeRegistryHelper.BLOCK_ENTITY_TYPES.register(modEventBus);
        ForgeRegistryHelper.MENU_TYPES.register(modEventBus);
        CommonLoader.init();
    }

    @SubscribeEvent
    public static void onClientRegister(FMLClientSetupEvent event) {
        MenuScreens.register(Constants.BRICK_HOPPER_MENU_TYPE.get(), BrickHopperScreen::new);
    }

    @SubscribeEvent
    public static void addItemsToTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
            event.accept(Constants.BRICK_HOPPER_ITEM);
        }
    }

}
