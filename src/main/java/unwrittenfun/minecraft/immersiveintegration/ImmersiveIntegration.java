package unwrittenfun.minecraft.immersiveintegration;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import org.apache.logging.log4j.Logger;
import unwrittenfun.minecraft.immersiveintegration.blocks.IIBlocks;
import unwrittenfun.minecraft.immersiveintegration.items.IIItems;
import unwrittenfun.minecraft.immersiveintegration.wires.IIWires;

@Mod(modid = ModInfo.MOD_ID, name = ModInfo.NAME, version = ModInfo.VERSION, dependencies = "required-after:Forge;required-after:ImmersiveEngineering;required-after:appliedenergistics2")
public class ImmersiveIntegration {
  @Mod.Instance
  public static ImmersiveIntegration instance;

  @SidedProxy(serverSide = "unwrittenfun.minecraft.immersiveintegration.CommonProxy", clientSide = "unwrittenfun.minecraft.immersiveintegration.client.ClientProxy")
  public static CommonProxy proxy;

  public static Logger log;

  public static CreativeTabs iiCreativeTab = new CreativeTabs(ModInfo.MOD_ID) {
    @Override
    public Item getTabIconItem() {
      return IIItems.wireCoil;
    }
  };

  @Mod.EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    log = event.getModLog();

    IIWires.registerWires();

    IIBlocks.registerBlocks();
    IIItems.registerItems();

    proxy.registerRenderers();
  }

  @Mod.EventHandler
  public void postInit(FMLPostInitializationEvent event) {
    IIBlocks.registerRecipes();
    IIItems.registerRecipes();
  }
}
