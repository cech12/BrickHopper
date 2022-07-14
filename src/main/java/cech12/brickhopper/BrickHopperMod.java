package cech12.brickhopper;

import cech12.brickhopper.api.block.BrickHopperBlocks;
import cech12.brickhopper.api.blockentity.BrickHopperBlockEntities;
import cech12.brickhopper.api.inventory.BrickHopperMenuTypes;
import cech12.brickhopper.api.item.BrickHopperItems;
import cech12.brickhopper.client.BrickHopperScreen;
import cech12.brickhopper.config.ServerConfig;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLConfig;
import net.minecraftforge.fml.loading.FMLPaths;

import static cech12.brickhopper.BrickHopperMod.MOD_ID;
import static cech12.brickhopper.api.inventory.BrickHopperMenuTypes.BRICK_HOPPER;

@Mod(MOD_ID)
@Mod.EventBusSubscriber(modid= MOD_ID, bus= Mod.EventBusSubscriber.Bus.MOD)
public class BrickHopperMod {

    public static final String MOD_ID = "brickhopper";

    public BrickHopperMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BrickHopperBlocks.BLOCKS.register(modEventBus);
        BrickHopperItems.ITEMS.register(modEventBus);
        BrickHopperBlockEntities.BLOCK_ENTITY_TYPES.register(modEventBus);
        BrickHopperMenuTypes.MENU_TYPES.register(modEventBus);
        //Config
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ServerConfig.SERVER_CONFIG);
        ServerConfig.loadConfig(ServerConfig.SERVER_CONFIG, FMLPaths.GAMEDIR.get().resolve(FMLConfig.defaultConfigPath()).resolve(MOD_ID + "-server.toml"));
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onClientRegister(FMLClientSetupEvent event) {
        MenuScreens.register(BRICK_HOPPER.get(), BrickHopperScreen::new);
    }

}
