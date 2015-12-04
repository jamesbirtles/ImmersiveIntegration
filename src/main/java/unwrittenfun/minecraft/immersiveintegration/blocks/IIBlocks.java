package unwrittenfun.minecraft.immersiveintegration.blocks;

import appeng.api.AEApi;
import appeng.api.definitions.IDefinitions;
import appeng.api.util.AEColor;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.metal.BlockMetalDecoration;
import blusunrize.immersiveengineering.common.blocks.metal.BlockMetalDevices;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import unwrittenfun.minecraft.immersiveintegration.ImmersiveIntegration;
import unwrittenfun.minecraft.immersiveintegration.ModInfo;
import unwrittenfun.minecraft.immersiveintegration.items.IIItems;
import unwrittenfun.minecraft.immersiveintegration.items.blocks.*;
import unwrittenfun.minecraft.immersiveintegration.tiles.*;

import java.lang.reflect.InvocationTargetException;

public class IIBlocks {
  public static final String ME_WIRE_CONNECTOR_KEY = "meWireConnector";
  public static final String ME_DENSE_CONNECTOR_KEY = "meDenseWireConnector";
  public static final String EXTENDED_POST_KEY = "extendedPost";
  public static final String EXTENDABLE_POST_KEY = "extendablePost";
  public static final String STEEL_TRAPDOOR = "steelTrapdoor";
  public static final String REDSTONE_WIRE_CONNECTOR_KEY = "redstoneWireConnector";
  public static final String INDUSTRIAL_COKE_OVEN = "industrialCokeOven";
  public static final String ITEM_ROBIN_KEY = "itemRobin";
  public static final String ME_TRANSFORMER_KEY = "meTransformer";
  public static final String ME_DENSE_TRANSFORMER_KEY = "meDenseTransformer";
  public static final String STEEL_BLOCKS_KEY = "steelDecoration";
  public static final String AE_DECORATIONS_KEY = "aeDecoration";
  public static final String INDUCTION_CHARGER_KEY = "inductionCharger";
  public static final String WALL_CONNECTOR_KEY = "wallConnector";

  public static final String[] STEEL_BLOCKS_KEYS = new String[]{
     "OvenWall", "OvenWallHeated", "OvenWallPort"
  };
  public static final String[] AE_DECORATION_KEYS = new String[]{
     "FluixCoil", "DenseCoil"
  };

  public static Block meWireConnector;
  public static Block meDenseWireConnector;
  public static Block meTransformer, meDenseTransformer;
  public static Block redstoneWireConnector;
  public static Block wallConnector;

  public static Block extendedPost;
  public static Block extendablePost;
  public static Block steelTrapdoor;
  public static Block aeDecoration;
  public static Block industrialCokeOven;
  public static Block itemRobin;
  public static Block steelDecoration;
  public static Block inductionCharger;

  public static void registerBlocks() {
    // Basic Blocks
    extendablePost = registerBlock(BlockExtendablePost.class, EXTENDABLE_POST_KEY);
    steelTrapdoor = registerBlock(BlockSteelTrapdoor.class, STEEL_TRAPDOOR);
    steelDecoration = registerBlock(BlockSteelDecoration.class, ItemBlockSteelDecoration.class, STEEL_BLOCKS_KEY);

    // Tile Blocks
    extendedPost = registerBlock(BlockExtendedPost.class, ItemBlockExtendedPost.class, EXTENDED_POST_KEY, TileExtendedPost.class);
    industrialCokeOven = registerBlock(BlockIndustrialCokeOven.class, INDUSTRIAL_COKE_OVEN, TileIndustrialCokeOven.class);
    itemRobin = registerBlock(BlockItemRobin.class, ITEM_ROBIN_KEY, TileItemRobin.class);
    inductionCharger = registerBlock(BlockInductionCharger.class, ItemBlockInductionCharger.class, INDUCTION_CHARGER_KEY);

    // Wire Connectors
    redstoneWireConnector = registerBlock(BlockRedstoneWireConnector.class, REDSTONE_WIRE_CONNECTOR_KEY, TileRedstoneWireConnector.class);
//    wallConnector = registerBlock(BlockWallConnector.class, WALL_CONNECTOR_KEY, TileWallConnector.class);

    GameRegistry.registerTileEntity(TileInductionChargerLV.class, ModInfo.MOD_ID + ":" + INDUCTION_CHARGER_KEY + "LVTile");
    GameRegistry.registerTileEntity(TileInductionChargerMV.class, ModInfo.MOD_ID + ":" + INDUCTION_CHARGER_KEY + "MVTile");
    GameRegistry.registerTileEntity(TileInductionChargerHV.class, ModInfo.MOD_ID + ":" + INDUCTION_CHARGER_KEY + "HVTile");

    if (ImmersiveIntegration.cfg.enableAE) registerAE2(false);
  }

  private static void registerAE2(boolean recipes) {
    if (recipes) {
      IDefinitions ae = AEApi.instance().definitions();
      GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(meWireConnector, 4), " c ", "fsf", "sfs", 'c', ae.parts().cableGlass().stack(AEColor.Transparent, 1), 'f', "dustFluix", 's', ae.blocks().skyStone().maybeBlock().get()));
      GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(meDenseWireConnector, 2), " d ", "rcr", "cgc", 'd', ae.parts().cableDense().stack(AEColor.Transparent, 1), 'r', "dustRedstone", 'c', meWireConnector, 'g', "dustGlowstone"));
      GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(aeDecoration, 1, 0), "www", "wiw", "www", 'w', new ItemStack(IIItems.aeWireCoil, 1, 0), 'i', "ingotIron"));
      GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(aeDecoration, 1, 1), "www", "wiw", "www", 'w', new ItemStack(IIItems.aeWireCoil, 1, 1), 'i', "ingotIron"));
      GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(meTransformer), " w ", "ibi", "iii", 'w', meWireConnector, 'b', new ItemStack(aeDecoration, 1, 0), 'i', "ingotIron"));
      GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(meDenseTransformer), " w ", "ibi", "iii", 'w', meDenseWireConnector, 'b', new ItemStack(aeDecoration, 1, 1), 'i', "ingotIron"));

    } else {
      meWireConnector = registerBlock(BlockMEWireConnector.class, ME_WIRE_CONNECTOR_KEY, TileMEWireConnector.class);
      meDenseWireConnector = registerBlock(BlockMEDenseWireConnector.class, ME_DENSE_CONNECTOR_KEY, TileMEDenseWireConnector.class);
      meTransformer = registerBlock(BlockMETransformer.class, ItemBlockMETransformer.class, ME_TRANSFORMER_KEY, TileMETransformer.class);
      meDenseTransformer = registerBlock(BlockMEDenseTransformer.class, ItemBlockMETransformer.class, ME_DENSE_TRANSFORMER_KEY, TileMEDenseTransformer.class);
      aeDecoration = registerBlock(BlockAEDecoration.class, ItemBlockAEDecoration.class, AE_DECORATIONS_KEY);
    }
  }

  public static void registerRecipes() {
    GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(IEContent.blockWoodenDecoration, 2, 1), extendedPost));
    GameRegistry.addRecipe(new ShapelessOreRecipe(extendablePost, new ItemStack(IEContent.blockWoodenDecoration, 1, 1), Blocks.stonebrick));
    GameRegistry.addRecipe(new ItemStack(steelTrapdoor, 2), "sss", "sss", 's', new ItemStack(IEContent.blockMetalDecoration, 1, BlockMetalDecoration.META_scaffolding));
    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(redstoneWireConnector, 8), "beb", " r ", "beb", 'b', "blockRedstone", 'e', "ingotElectrum", 'r', "dustRedstone"));

    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(steelDecoration, 8, 0), "ibi", "sbs", "ibi", 's', new ItemStack(IEContent.itemMaterial, 1, 12), 'b', Items.bucket, 'i', "ingotIron"));
    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(steelDecoration, 8, 1), "ibi", "sbs", "ibi", 's', new ItemStack(IEContent.itemMaterial, 1, 12), 'b', Items.lava_bucket, 'i', "ingotIron"));
    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(steelDecoration, 4, 2), "ibi", "isi", "ibi", 's', new ItemStack(IEContent.itemMaterial, 1, 12), 'b', Items.bucket, 'i', "ingotIron"));
    GameRegistry.addRecipe(new ShapedOreRecipe(itemRobin, " r ", "scs", " r ", 's', new ItemStack(IEContent.itemMaterial, 1, 12), 'r', "dustRedstone", 'c', new ItemStack(IEContent.blockWoodenDevice, 1, 4)));

    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(inductionCharger, 1, 0), "scs", "wew", 's', "slabTreatedWood", 'c', new ItemStack(IEContent.blockStorage, 1, 8), 'w', "plankTreatedWood", 'e', new ItemStack(IEContent.blockMetalDevice, 1, BlockMetalDevices.META_capacitorLV)));
    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(inductionCharger, 1, 1), "scs", "wew", 's', "slabTreatedWood", 'c', new ItemStack(IEContent.blockStorage, 1, 9), 'w', "plankTreatedWood", 'e', new ItemStack(IEContent.blockMetalDevice, 1, BlockMetalDevices.META_capacitorMV)));
    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(inductionCharger, 1, 2), "scs", "wew", 's', "slabTreatedWood", 'c', new ItemStack(IEContent.blockStorage, 1, 10), 'w', "plankTreatedWood", 'e', new ItemStack(IEContent.blockMetalDevice, 1, BlockMetalDevices.META_capacitorHV)));

    if (ImmersiveIntegration.cfg.enableAE) registerAE2(true);
  }

  public static Block registerBlock(Class<? extends Block> blockClass, Class<? extends ItemBlock> itemClass, String key, Class<? extends TileEntity> tileClass) {
    try {
      Block block = blockClass.getConstructor(String.class).newInstance(ModInfo.MOD_ID + ":" + key);
      if (itemClass == null) {
        GameRegistry.registerBlock(block, key);
      } else {
        GameRegistry.registerBlock(block, itemClass, key);
      }
      if (tileClass != null) {
        GameRegistry.registerTileEntity(tileClass, ModInfo.MOD_ID + ":" + key + "Tile");
      }
      return block;
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      ImmersiveIntegration.log.error("Something went horribly wrong instantiating a block for " + blockClass.getName());
      e.printStackTrace();
      ImmersiveIntegration.log.error("Report this to UnwrittenFun. Do NOT ignore. Exiting Java.");
      FMLCommonHandler.instance().exitJava(0, false);
    }
    return null;
  }

  public static Block registerBlock(Class<? extends Block> blockClass, String key, Class<? extends TileEntity> tileClass) {
    return registerBlock(blockClass, null, key, tileClass);
  }

  public static Block registerBlock(Class<? extends Block> blockClass, String key) {
    return registerBlock(blockClass, null, key, null);
  }

  public static Block registerBlock(Class<? extends Block> blockClass, Class<? extends ItemBlock> itemClass, String key) {
    return registerBlock(blockClass, itemClass, key, null);
  }
}