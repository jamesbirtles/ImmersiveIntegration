package unwrittenfun.minecraft.immersiveintegration.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import unwrittenfun.minecraft.immersiveintegration.CommonProxy;
import unwrittenfun.minecraft.immersiveintegration.ModInfo;
import unwrittenfun.minecraft.immersiveintegration.blocks.IIBlocks;
import unwrittenfun.minecraft.immersiveintegration.client.renderers.*;
import unwrittenfun.minecraft.immersiveintegration.tiles.TileExtendedPost;
import unwrittenfun.minecraft.immersiveintegration.tiles.TileMEDenseWireConnector;
import unwrittenfun.minecraft.immersiveintegration.tiles.TileMEWireConnector;

public class ClientProxy extends CommonProxy {
  @Override
  public void registerRenderers() {
    RenderingRegistry.registerBlockHandler(new BlockRenderIIBlocks());
    RenderingRegistry.registerBlockHandler(new BlockRenderExtendedPost());
    ClientRegistry.bindTileEntitySpecialRenderer(TileMEWireConnector.class, new TileRenderWireConnector(new ModelIIObj("immersiveengineering:models/connectorMV.obj", IIBlocks.meWireConnector)));
    ClientRegistry.bindTileEntitySpecialRenderer(TileMEDenseWireConnector.class, new TileRenderWireConnector(new ModelIIObj(ModInfo.MOD_ID + ":models/meDenseWireConnector.obj", IIBlocks.meDenseWireConnector)));
    ClientRegistry.bindTileEntitySpecialRenderer(TileExtendedPost.class, new TileRenderExtendedPost());
  }
}
