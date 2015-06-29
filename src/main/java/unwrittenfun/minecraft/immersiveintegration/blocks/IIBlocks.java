package unwrittenfun.minecraft.immersiveintegration.blocks;

import appeng.api.AEApi;
import appeng.api.definitions.IDefinitions;
import appeng.api.util.AEColor;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import unwrittenfun.minecraft.immersiveintegration.ModInfo;
import unwrittenfun.minecraft.immersiveintegration.tiles.TileMEDenseWireConnector;
import unwrittenfun.minecraft.immersiveintegration.tiles.TileMEWireConnector;

public class IIBlocks {
  public static final String ME_WIRE_CONNECTOR_KEY = "meWireConnector";
  public static final String ME_DENSE_CONNECTOR_KEY = "meDenseWireConnector";

  public static Block meWireConnector;
  public static Block meDenseWireConnector;

  public static void registerBlocks() {
    meWireConnector = new BlockMEWireConnector(ModInfo.MOD_ID + ":" + ME_WIRE_CONNECTOR_KEY);
    meDenseWireConnector = new BlockMEDenseWireConnector(ModInfo.MOD_ID + ":" + ME_DENSE_CONNECTOR_KEY);

    GameRegistry.registerBlock(meWireConnector, ME_WIRE_CONNECTOR_KEY);
    GameRegistry.registerBlock(meDenseWireConnector, ME_DENSE_CONNECTOR_KEY);

    GameRegistry.registerTileEntity(TileMEWireConnector.class, ME_WIRE_CONNECTOR_KEY + "Tile");
    GameRegistry.registerTileEntity(TileMEDenseWireConnector.class, ME_DENSE_CONNECTOR_KEY + "Tile");
  }

  public static void registerRecipes() {
    IDefinitions ae = AEApi.instance().definitions();
    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(meWireConnector, 4), " c ", "fsf", "sfs", 'c', ae.parts().cableGlass().stack(AEColor.Transparent, 1), 'f', "dustFluix", 's', ae.blocks().skyStone().maybeBlock().get()));
    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(meDenseWireConnector, 2), " d ", "rcr", "cgc", 'd', ae.parts().cableDense().stack(AEColor.Transparent, 1), 'r', "dustRedstone", 'c', meWireConnector, 'g', "dustGlowstone"));
  }
}
