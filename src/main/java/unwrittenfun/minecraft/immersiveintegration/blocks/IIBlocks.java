package unwrittenfun.minecraft.immersiveintegration.blocks;

import appeng.api.AEApi;
import appeng.api.definitions.IDefinitions;
import appeng.api.util.AEColor;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.metal.BlockMetalDecoration;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import unwrittenfun.minecraft.immersiveintegration.ImmersiveIntegration;
import unwrittenfun.minecraft.immersiveintegration.ModInfo;
import unwrittenfun.minecraft.immersiveintegration.items.ItemBlockExtendedPost;
import unwrittenfun.minecraft.immersiveintegration.tiles.TileExtendedPost;
import unwrittenfun.minecraft.immersiveintegration.tiles.TileMEDenseWireConnector;
import unwrittenfun.minecraft.immersiveintegration.tiles.TileMEWireConnector;

public class IIBlocks {
  public static final String ME_WIRE_CONNECTOR_KEY = "meWireConnector";
  public static final String ME_DENSE_CONNECTOR_KEY = "meDenseWireConnector";
  public static final String EXTENDED_POST_KEY = "extendedPost";
  public static final String STEEL_TRAPDOOR = "steelTrapdoor";

  public static Block meWireConnector;
  public static Block meDenseWireConnector;
  public static Block extendedPost;
  public static Block steelTrapdoor;

  public static void registerBlocks() {
    extendedPost = new BlockExtendedPost(ModInfo.MOD_ID + ":" + EXTENDED_POST_KEY);
    steelTrapdoor = new BlockSteelTrapdoor(ModInfo.MOD_ID + ":" + STEEL_TRAPDOOR);

    GameRegistry.registerBlock(extendedPost, ItemBlockExtendedPost.class, EXTENDED_POST_KEY);
    GameRegistry.registerBlock(steelTrapdoor, STEEL_TRAPDOOR);

    GameRegistry.registerTileEntity(TileExtendedPost.class, EXTENDED_POST_KEY + "Tile");

    if (ImmersiveIntegration.cfg.enableAE) registerAE2(false);
  }

  public static void registerRecipes() {
    GameRegistry.addRecipe(new ShapedOreRecipe(extendedPost, "p", "p", 'p', new ItemStack(IEContent.blockWoodenDecoration, 1, 1)));
    GameRegistry.addRecipe(new ItemStack(steelTrapdoor, 2), "sss", "sss", 's', new ItemStack(IEContent.blockMetalDecoration, 1, BlockMetalDecoration.META_scaffolding));

    if (ImmersiveIntegration.cfg.enableAE) registerAE2(true);
  }

  private static void registerAE2(boolean recipes) {
    if (recipes) {
      IDefinitions ae = AEApi.instance().definitions();
      GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(meWireConnector, 4), " c ", "fsf", "sfs", 'c', ae.parts().cableGlass().stack(AEColor.Transparent, 1), 'f', "dustFluix", 's', ae.blocks().skyStone().maybeBlock().get()));
      GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(meDenseWireConnector, 2), " d ", "rcr", "cgc", 'd', ae.parts().cableDense().stack(AEColor.Transparent, 1), 'r', "dustRedstone", 'c', meWireConnector, 'g', "dustGlowstone"));
    } else {
      meWireConnector = new BlockMEWireConnector(ModInfo.MOD_ID + ":" + ME_WIRE_CONNECTOR_KEY);
      meDenseWireConnector = new BlockMEDenseWireConnector(ModInfo.MOD_ID + ":" + ME_DENSE_CONNECTOR_KEY);

      GameRegistry.registerBlock(meWireConnector, ME_WIRE_CONNECTOR_KEY);
      GameRegistry.registerBlock(meDenseWireConnector, ME_DENSE_CONNECTOR_KEY);

      GameRegistry.registerTileEntity(TileMEWireConnector.class, ME_WIRE_CONNECTOR_KEY + "Tile");
      GameRegistry.registerTileEntity(TileMEDenseWireConnector.class, ME_DENSE_CONNECTOR_KEY + "Tile");
    }
  }
}
