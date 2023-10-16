package cech12.brickhopper.api.inventory;

import cech12.brickhopper.BrickHopperMod;
import cech12.brickhopper.inventory.BrickHopperContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BrickHopperMenuTypes {

    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, BrickHopperMod.MOD_ID);

    public static final RegistryObject<MenuType<BrickHopperContainer>> BRICK_HOPPER = MENU_TYPES.register("brickhopper", () -> IForgeMenuType.create((pWindowID, pInventory, pData) -> new BrickHopperContainer(pWindowID, pInventory)));

}
