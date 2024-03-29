package de.cech12.brickhopper;

import de.cech12.brickhopper.block.NeoForgeBrickHopperItemHandler;
import de.cech12.brickhopper.client.BrickHopperScreen;
import de.cech12.brickhopper.platform.NeoForgeRegistryHelper;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@Mod(Constants.MOD_ID)
@Mod.EventBusSubscriber(modid= Constants.MOD_ID, bus= Mod.EventBusSubscriber.Bus.MOD)
public class NeoForgeBrickHopperMod {

    public NeoForgeBrickHopperMod(IEventBus modEventBus) {
        NeoForgeRegistryHelper.BLOCKS.register(modEventBus);
        NeoForgeRegistryHelper.ITEMS.register(modEventBus);
        NeoForgeRegistryHelper.BLOCK_ENTITY_TYPES.register(modEventBus);
        NeoForgeRegistryHelper.MENU_TYPES.register(modEventBus);
        CommonLoader.init();
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onClientRegister(FMLClientSetupEvent event) {
        MenuScreens.register(NeoForgeRegistryHelper.BRICK_HOPPER_MENU_TYPE.get(), BrickHopperScreen::new);
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, NeoForgeRegistryHelper.BRICK_HOPPER_BLOCK_ENTITY_TYPE.get(), (blockEntity, side) -> new NeoForgeBrickHopperItemHandler(blockEntity));
    }

    @SubscribeEvent
    public static void addItemsToTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
            event.accept(NeoForgeRegistryHelper.BRICK_HOPPER_ITEM);
        }
    }

}
