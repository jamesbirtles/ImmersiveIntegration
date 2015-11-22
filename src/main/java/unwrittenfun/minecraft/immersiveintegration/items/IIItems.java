package unwrittenfun.minecraft.immersiveintegration.items;

import appeng.api.AEApi;
import appeng.api.definitions.IDefinitions;
import appeng.api.util.AEColor;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.metal.BlockMetalDevices;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import unwrittenfun.minecraft.immersiveintegration.ImmersiveIntegration;
import unwrittenfun.minecraft.immersiveintegration.ModInfo;
import unwrittenfun.minecraft.immersiveintegration.recipe.CapacitorBoxRecipe;
import unwrittenfun.minecraft.immersiveintegration.wires.IIWires;

public class IIItems {
  public static final int FLUIX_COIL_META = 0;
  public static final int DENSE_COIL_META = 1;
  public static final int REDSTONE_COIL_META = 0;
  public static String AE_COIL_KEY = "wireCoil"; // Should be aeWireCoil but backwards compatibility.
  public static String II_COIL_KEY = "iiWireCoil";
  public static String CAPACITOR_BOX = "capacitorBox";
  public static Item aeWireCoil;
  public static Item iiWireCoil;
  public static Item capacitorBox;

  public static void registerItems() {
    iiWireCoil = new ItemCoil(ModInfo.MOD_ID + ":" + II_COIL_KEY, new String[]{"Redstone"}, IIWires.redstoneWire);
    capacitorBox = new ItemCapacitorBox(ModInfo.MOD_ID + ":" + CAPACITOR_BOX);

    GameRegistry.registerItem(iiWireCoil, II_COIL_KEY);
    GameRegistry.registerItem(capacitorBox, CAPACITOR_BOX);

    if (ImmersiveIntegration.cfg.enableAE) registerAE(false);
  }

  public static void registerRecipes() {
    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(iiWireCoil, 4, REDSTONE_COIL_META), " e ", "rsr", " e ", 'r', "dustRedstone", 'e', "ingotElectrum", 's', "stickWood"));

    GameRegistry.addRecipe(new CapacitorBoxRecipe(new ItemStack(IEContent.blockMetalDevice, 1, BlockMetalDevices.META_capacitorLV), new ItemStack(capacitorBox, 1, 0)));
    GameRegistry.addRecipe(new CapacitorBoxRecipe(new ItemStack(IEContent.blockMetalDevice, 1, BlockMetalDevices.META_capacitorMV), new ItemStack(capacitorBox, 1, 1)));
    GameRegistry.addRecipe(new CapacitorBoxRecipe(new ItemStack(IEContent.blockMetalDevice, 1, BlockMetalDevices.META_capacitorHV), new ItemStack(capacitorBox, 1, 2)));

    if (ImmersiveIntegration.cfg.enableAE) registerAE(true);

    if (ImmersiveIntegration.cfg.enableTinkers) {
      FMLInterModComms.sendMessage("TConstruct", "addFluxBattery", new ItemStack(capacitorBox));
    }
  }

  private static void registerAE(boolean recipes) {
    if (recipes) {
      IDefinitions ae = AEApi.instance().definitions();
      GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(aeWireCoil, 4, FLUIX_COIL_META), "cfc", "fsf", "cfc", 'c', ae.parts().cableGlass().stack(AEColor.Transparent, 1), 'f', "crystalPureFluix", 's', "stickWood"));
      GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(aeWireCoil, 4, FLUIX_COIL_META), "cfc", "fsf", "cfc", 'c', ae.parts().cableGlass().stack(AEColor.Transparent, 1), 'f', "crystalFluix", 's', "stickWood"));
      GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(aeWireCoil, 2, DENSE_COIL_META), "wcw", "csc", "wcw", 'c', ae.parts().cableDense().stack(AEColor.Transparent, 1), 'w', new ItemStack(aeWireCoil, 1, FLUIX_COIL_META), 's', "stickWood"));
    } else {
      aeWireCoil = new ItemCoil(ModInfo.MOD_ID + ":" + AE_COIL_KEY, new String[]{"Fluix", "Dense"}, IIWires.fluixWire, IIWires.denseWire);
      GameRegistry.registerItem(aeWireCoil, AE_COIL_KEY);
    }
  }
}
