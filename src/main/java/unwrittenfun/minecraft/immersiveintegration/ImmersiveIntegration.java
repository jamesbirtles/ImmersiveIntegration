package unwrittenfun.minecraft.immersiveintegration;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Logger;
import unwrittenfun.minecraft.immersiveintegration.blocks.IIBlocks;
import unwrittenfun.minecraft.immersiveintegration.compat.CCCompat;
import unwrittenfun.minecraft.immersiveintegration.compat.CompatModule;
import unwrittenfun.minecraft.immersiveintegration.gui.GuiHandler;
import unwrittenfun.minecraft.immersiveintegration.items.IIItems;
import unwrittenfun.minecraft.immersiveintegration.multiblocks.IIMultiblocks;
import unwrittenfun.minecraft.immersiveintegration.special.Special;
import unwrittenfun.minecraft.immersiveintegration.wires.IIWires;

@Mod(modid = ModInfo.MOD_ID, name = ModInfo.NAME, version = ModInfo.VERSION, dependencies = "required-after:Forge;required-after:ImmersiveEngineering;after:appliedenergistics2", guiFactory = ModInfo.GUI_FACTORY_CLASS)
public class ImmersiveIntegration {
  @Mod.Instance
  public static ImmersiveIntegration instance;

  @SidedProxy(serverSide = "unwrittenfun.minecraft.immersiveintegration.CommonProxy", clientSide = "unwrittenfun.minecraft.immersiveintegration.client.ClientProxy")
  public static CommonProxy proxy;

  public static Logger log;

  public static Config cfg;

  public static CreativeTabs iiCreativeTab = new CreativeTabs(ModInfo.MOD_ID) {
    @Override
    public Item getTabIconItem() {
      return Item.getItemFromBlock(IIBlocks.itemRobin);
    }
  };

  @Mod.EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    cfg = new Config(new Configuration(event.getSuggestedConfigurationFile()));

    log = event.getModLog();

    IIWires.registerWires();

    IIBlocks.registerBlocks();
    IIItems.registerItems();

    proxy.registerRenderers();
    FMLCommonHandler.instance().bus().register(cfg);

    Special.preInit();

    IIMultiblocks.registerMultiblocks();

    NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

    CompatModule.eventPreInit();
  }

  @Mod.EventHandler
  public void init(FMLInitializationEvent event) {
    CompatModule.eventInit();
  }

  @Mod.EventHandler
  public void postInit(FMLPostInitializationEvent event) {
    IIBlocks.registerRecipes();
    IIItems.registerRecipes();

    CompatModule.eventPostInit();
  }

  @Mod.EventHandler
  public void loadComplete(FMLLoadCompleteEvent event) {
    proxy.registerManualPages();
  }

  @Mod.EventHandler
  public void serverLoad(FMLServerStartingEvent event) {
    Special.serverLoad(event);
  }
}
