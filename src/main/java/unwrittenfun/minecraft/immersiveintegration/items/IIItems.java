package unwrittenfun.minecraft.immersiveintegration.items;

import appeng.api.AEApi;
import appeng.api.definitions.IDefinitions;
import appeng.api.util.AEColor;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import unwrittenfun.minecraft.immersiveintegration.ModInfo;

public class IIItems {
  public static String COIL_KEY = "wireCoil";

  public static Item wireCoil;

  public static void registerItems() {
    wireCoil = new ItemCoil(ModInfo.MOD_ID + ":" + COIL_KEY);

    GameRegistry.registerItem(wireCoil, COIL_KEY);
  }

  public static void registerRecipes() {
    IDefinitions ae = AEApi.instance().definitions();
    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(wireCoil, 4, ItemCoil.FLUIX_COIL_META), "cfc", "fsf", "cfc", 'c', ae.parts().cableGlass().stack(AEColor.Transparent, 1), 'f', "crystalPureFluix", 's', "stickWood"));
    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(wireCoil, 4, ItemCoil.FLUIX_COIL_META), "cfc", "fsf", "cfc", 'c', ae.parts().cableGlass().stack(AEColor.Transparent, 1), 'f', "crystalFluix", 's', "stickWood"));
    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(wireCoil, 2, ItemCoil.DENSE_COIL_META), "wcw", "csc", "wcw", 'c', ae.parts().cableDense().stack(AEColor.Transparent, 1), 'w', new ItemStack(wireCoil, 1, 0), 's', "stickWood"));
  }
}
