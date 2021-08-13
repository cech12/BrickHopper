package cech12.brickhopper;

import cech12.brickhopper.client.BrickHopperScreen;
import cech12.brickhopper.config.ServerConfig;
import cech12.brickhopper.inventory.BrickHopperContainer;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.loading.FMLConfig;
import net.minecraftforge.fml.loading.FMLPaths;

import static cech12.brickhopper.BrickHopperMod.MOD_ID;
import static cech12.brickhopper.api.inventory.BrickHopperContainerTypes.BRICK_HOPPER;

@Mod(MOD_ID)
@Mod.EventBusSubscriber(modid= MOD_ID, bus= Mod.EventBusSubscriber.Bus.MOD)
public class BrickHopperMod {

    public static final String MOD_ID = "brickhopper";

    public BrickHopperMod() {
        //Config
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ServerConfig.SERVER_CONFIG);
        ServerConfig.loadConfig(ServerConfig.SERVER_CONFIG, FMLPaths.GAMEDIR.get().resolve(FMLConfig.defaultConfigPath()).resolve(MOD_ID + "-server.toml"));
    }

    @SubscribeEvent
    public static void registerContainers(RegistryEvent.Register<ContainerType<?>> event) {
        event.getRegistry().register(BRICK_HOPPER);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onClientRegister(FMLClientSetupEvent event) {
        ScreenManager.register((ContainerType<BrickHopperContainer>) BRICK_HOPPER, BrickHopperScreen::new);
    }

}
