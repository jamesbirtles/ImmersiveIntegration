package unwrittenfun.minecraft.immersiveintegration.client;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.immersiveengineering.api.ManualPageMultiblock;
import blusunrize.lib.manual.ManualInstance;
import blusunrize.lib.manual.ManualPages;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import unwrittenfun.minecraft.immersiveintegration.CommonProxy;
import unwrittenfun.minecraft.immersiveintegration.ImmersiveIntegration;
import unwrittenfun.minecraft.immersiveintegration.ModInfo;
import unwrittenfun.minecraft.immersiveintegration.blocks.IIBlocks;
import unwrittenfun.minecraft.immersiveintegration.client.renderers.ModelIIObj;
import unwrittenfun.minecraft.immersiveintegration.client.renderers.block.BlockRenderExtendablePost;
import unwrittenfun.minecraft.immersiveintegration.client.renderers.block.BlockRenderExtendedPost;
import unwrittenfun.minecraft.immersiveintegration.client.renderers.block.BlockRenderIIBlocks;
import unwrittenfun.minecraft.immersiveintegration.client.renderers.block.BlockRenderIndustrialCokeOven;
import unwrittenfun.minecraft.immersiveintegration.client.renderers.tile.*;
import unwrittenfun.minecraft.immersiveintegration.items.IIItems;
import unwrittenfun.minecraft.immersiveintegration.multiblocks.IIMultiblocks;
import unwrittenfun.minecraft.immersiveintegration.special.SpecialEventHandler;
import unwrittenfun.minecraft.immersiveintegration.tiles.*;

public class ClientProxy extends CommonProxy {
  @Override
  public void registerRenderers() {
    RenderingRegistry.registerBlockHandler(new BlockRenderIIBlocks());
    RenderingRegistry.registerBlockHandler(new BlockRenderExtendedPost());
    RenderingRegistry.registerBlockHandler(new BlockRenderExtendablePost());
    RenderingRegistry.registerBlockHandler(new BlockRenderIndustrialCokeOven());
    ClientRegistry.bindTileEntitySpecialRenderer(TileExtendedPost.class, new TileRenderExtendedPost());
    ClientRegistry.bindTileEntitySpecialRenderer(TileRedstoneWireConnector.class, new TileRenderRedstoneWireConnector());
    ClientRegistry.bindTileEntitySpecialRenderer(TileIndustrialCokeOven.class, new TileRenderIndustrialCokeOven());
    ClientRegistry.bindTileEntitySpecialRenderer(TileInductionChargerLV.class, new TileRenderInductionCharger(0));
    ClientRegistry.bindTileEntitySpecialRenderer(TileInductionChargerMV.class, new TileRenderInductionCharger(1));
    ClientRegistry.bindTileEntitySpecialRenderer(TileInductionChargerHV.class, new TileRenderInductionCharger(2));
//    ClientRegistry.bindTileEntitySpecialRenderer(TileWallConnector.class, new TileRenderWallConnector());

    if (ImmersiveIntegration.cfg.enableAE) {
      ClientRegistry.bindTileEntitySpecialRenderer(TileMEWireConnector.class, new TileRenderWireConnector(new ModelIIObj("immersiveengineering:models/connectorMV.obj", IIBlocks.meWireConnector)));
      ClientRegistry.bindTileEntitySpecialRenderer(TileMEDenseWireConnector.class, new TileRenderWireConnector(new ModelIIObj(ModInfo.MOD_ID + ":models/meDenseWireConnector.obj", IIBlocks.meDenseWireConnector)));
      ClientRegistry.bindTileEntitySpecialRenderer(TileMETransformer.class, new TileRenderMETransformer());
    }

    MinecraftForge.EVENT_BUS.register(new RenderEventsHandler());
  }

  @Override
  public void registerManualPages() {
    ManualInstance manual = ManualHelper.getManual();
    manual.addEntry("extendablePost", "immersiveintegration",
       new ManualPages.Crafting(manual, "extendablePost0", new ItemStack(IIBlocks.extendablePost))
    );
    manual.addEntry("steelTrapdoor", "immersiveintegration",
       new ManualPages.Crafting(manual, "steelTrapdoor0", new ItemStack(IIBlocks.steelTrapdoor))
    );
    manual.addEntry("redstoneWiring", "immersiveintegration",
       new ManualPages.CraftingMulti(manual, "redstoneWiring0", new ItemStack(IIItems.iiWireCoil, 1, IIItems.REDSTONE_COIL_META), new ItemStack(IIBlocks.redstoneWireConnector)),
       new ManualPages.Text(manual, "redstoneWiring1"),
       new ManualPages.Text(manual, "redstoneWiring2")
    );
    manual.addEntry("industrialCokeOven", "immersiveintegration",
       new ManualPages.CraftingMulti(manual, "industrialCokeOven0", new ItemStack(IIBlocks.steelDecoration, 1, 0), new ItemStack(IIBlocks.steelDecoration, 1, 1), new ItemStack(IIBlocks.steelDecoration, 1, 2)),
       new ManualPages.Text(manual, "industrialCokeOven1"),
       new ManualPageMultiblock(manual, "industrialCokeOven2", IIMultiblocks.industrialCokeOven)
    );
    manual.addEntry("itemRobin", "immersiveintegration",
       new ManualPages.Crafting(manual, "itemRobin0", new ItemStack(IIBlocks.itemRobin)),
       new ManualPages.Text(manual, "itemRobin1")
    );
    manual.addEntry("capacitorBox", "immersiveintegration",
       new ManualPages.CraftingMulti(manual, "capacitorBox0", new ItemStack(IIItems.capacitorBox, 1, 0), new ItemStack(IIItems.capacitorBox, 1, 1), new ItemStack(IIItems.capacitorBox, 1, 2))
    );
    manual.addEntry("inductionCharger", "immersiveintegration",
       new ManualPages.CraftingMulti(manual, "inductionCharger0", new ItemStack(IIBlocks.inductionCharger, 1, 0), new ItemStack(IIBlocks.inductionCharger, 1, 1), new ItemStack(IIBlocks.inductionCharger, 1, 2))
    );

    if (ImmersiveIntegration.cfg.enableAE) {
      manual.addEntry("meWiring", "immersiveintegration",
         new ManualPages.CraftingMulti(manual, "meWiring0", new ItemStack(IIItems.aeWireCoil, 1, IIItems.FLUIX_COIL_META), new ItemStack(IIItems.aeWireCoil, 1, IIItems.DENSE_COIL_META)),
         new ManualPages.CraftingMulti(manual, "meWiring1", new ItemStack(IIBlocks.meTransformer), new ItemStack(IIBlocks.meDenseTransformer)),
         new ManualPages.CraftingMulti(manual, "meWiring2", new ItemStack(IIBlocks.meWireConnector), new ItemStack(IIBlocks.meDenseWireConnector)),
         new ManualPages.CraftingMulti(manual, "meWiring3", new ItemStack(IIBlocks.aeDecoration, 1, 0), new ItemStack(IIBlocks.aeDecoration, 1, 1))
      );
    }
  }

  @Override
  public void registerClientSpecial() {
    MinecraftForge.EVENT_BUS.register(new SpecialEventHandler());
  }
}
