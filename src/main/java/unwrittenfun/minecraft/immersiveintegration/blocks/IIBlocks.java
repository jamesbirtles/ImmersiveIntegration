package unwrittenfun.minecraft.immersiveintegration.blocks;

import appeng.api.AEApi;
import appeng.api.definitions.IDefinitions;
import appeng.api.util.AEColor;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.metal.BlockMetalDecoration;
import blusunrize.immersiveengineering.common.blocks.metal.BlockMetalDevices;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import unwrittenfun.minecraft.immersiveintegration.ImmersiveIntegration;
import unwrittenfun.minecraft.immersiveintegration.ModInfo;
import unwrittenfun.minecraft.immersiveintegration.items.ItemBlockExtendedPost;
import unwrittenfun.minecraft.immersiveintegration.items.ItemBlockSteelDecoration;
import unwrittenfun.minecraft.immersiveintegration.tiles.*;

public class IIBlocks {
  public static final String ME_WIRE_CONNECTOR_KEY = "meWireConnector";
  public static final String ME_DENSE_CONNECTOR_KEY = "meDenseWireConnector";
  public static final String EXTENDED_POST_KEY = "extendedPost";
  public static final String STEEL_TRAPDOOR = "steelTrapdoor";
  public static final String REDSTONE_WIRE_CONNECTOR_KEY = "redstoneWireConnector";
  public static final String INDUSTRIAL_COKE_OVEN = "industrialCokeOven";
  public static final String ITEM_ROBIN_KEY = "itemRobin";
  public static final String STEEL_BLOCKS_KEY = "steelDecoration";
  public static final String[] STEEL_BLOCKS_KEYS = new String[] {
      "OvenWall", "OvenWallHeated", "OvenWallPort"
  };

  public static Block meWireConnector;
  public static Block meDenseWireConnector;
  public static Block extendedPost;
  public static Block steelTrapdoor;
  public static Block redstoneWireConnector;
  public static BlockIndustrialCokeOven industrialCokeOven;
  public static Block itemRobin;
  public static Block steelDecoration;

  public static void registerBlocks() {
    extendedPost = new BlockExtendedPost(ModInfo.MOD_ID + ":" + EXTENDED_POST_KEY);
    steelTrapdoor = new BlockSteelTrapdoor(ModInfo.MOD_ID + ":" + STEEL_TRAPDOOR);
    redstoneWireConnector = new BlockRedstoneWireConnector(ModInfo.MOD_ID + ":" + REDSTONE_WIRE_CONNECTOR_KEY);
    industrialCokeOven = new BlockIndustrialCokeOven(ModInfo.MOD_ID + ":" + INDUSTRIAL_COKE_OVEN);
    itemRobin = new BlockItemRobin(ModInfo.MOD_ID + ":" + ITEM_ROBIN_KEY);
    steelDecoration = new BlockSteelDecoration(ModInfo.MOD_ID + ":" + STEEL_BLOCKS_KEY, STEEL_BLOCKS_KEYS);

    GameRegistry.registerBlock(extendedPost, ItemBlockExtendedPost.class, EXTENDED_POST_KEY);
    GameRegistry.registerBlock(steelTrapdoor, STEEL_TRAPDOOR);
    GameRegistry.registerBlock(redstoneWireConnector, REDSTONE_WIRE_CONNECTOR_KEY);
    GameRegistry.registerBlock(industrialCokeOven, INDUSTRIAL_COKE_OVEN);
    GameRegistry.registerBlock(itemRobin, ITEM_ROBIN_KEY);
    GameRegistry.registerBlock(steelDecoration, ItemBlockSteelDecoration.class, STEEL_BLOCKS_KEY);

    GameRegistry.registerTileEntity(TileExtendedPost.class, ModInfo.MOD_ID + ":" + EXTENDED_POST_KEY + "Tile");
    GameRegistry.registerTileEntity(TileRedstoneWireConnector.class, ModInfo.MOD_ID + ":" + REDSTONE_WIRE_CONNECTOR_KEY + "Tile");
    GameRegistry.registerTileEntity(TileIndustrialCokeOven.class, ModInfo.MOD_ID + ":" + INDUSTRIAL_COKE_OVEN + "Tile");
    GameRegistry.registerTileEntity(TileItemRobin.class, ModInfo.MOD_ID + ":" + ITEM_ROBIN_KEY + "Tile");

    if (ImmersiveIntegration.cfg.enableAE) registerAE2(false);
  }

  public static void registerRecipes() {
    GameRegistry.addRecipe(new ShapedOreRecipe(extendedPost, "p", "p", 'p', new ItemStack(IEContent.blockWoodenDecoration, 1, 1)));
    GameRegistry.addRecipe(new ItemStack(steelTrapdoor, 2), "sss", "sss", 's', new ItemStack(IEContent.blockMetalDecoration, 1, BlockMetalDecoration.META_scaffolding));
    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(redstoneWireConnector, 8), "beb", " r ", "beb", 'b', "blockRedstone", 'e', "ingotElectrum", 'r', "dustRedstone"));

    GameRegistry.addRecipe(new ItemStack(steelDecoration, 1, 0), "sbs", "sbs", "sbs", 's', new ItemStack(IEContent.itemMaterial, 1, 12), 'b', Items.bucket);
    GameRegistry.addRecipe(new ItemStack(steelDecoration, 1, 1), "sbs", "sbs", "sbs", 's', new ItemStack(IEContent.itemMaterial, 1, 12), 'b', Items.lava_bucket);
    GameRegistry.addRecipe(new ItemStack(steelDecoration, 1, 2), "sbs", "scs", "sbs", 's', new ItemStack(IEContent.itemMaterial, 1, 12), 'b', Items.bucket, 'c', new ItemStack(IEContent.blockMetalDevice, 1, BlockMetalDevices.META_conveyorBelt));
    GameRegistry.addRecipe(new ShapedOreRecipe(itemRobin, " r ", "scs", " r ", 's', new ItemStack(IEContent.itemMaterial, 1, 12), 'r', "dustRedstone", 'c', new ItemStack(IEContent.blockWoodenDevice, 1, 4)));

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

      GameRegistry.registerTileEntity(TileMEWireConnector.class, ModInfo.MOD_ID + ":" + ME_WIRE_CONNECTOR_KEY + "Tile");
      GameRegistry.registerTileEntity(TileMEDenseWireConnector.class, ModInfo.MOD_ID + ":" + ME_DENSE_CONNECTOR_KEY + "Tile");
    }
  }
}
