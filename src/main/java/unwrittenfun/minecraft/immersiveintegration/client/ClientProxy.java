package unwrittenfun.minecraft.immersiveintegration.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import unwrittenfun.minecraft.immersiveintegration.CommonProxy;
import unwrittenfun.minecraft.immersiveintegration.client.renderers.*;
import unwrittenfun.minecraft.immersiveintegration.tiles.TileExtendedPost;
import unwrittenfun.minecraft.immersiveintegration.tiles.TileMEDenseWireConnector;
import unwrittenfun.minecraft.immersiveintegration.tiles.TileMEWireConnector;

public class ClientProxy extends CommonProxy {
  @Override
  public void registerRenderers() {
    RenderingRegistry.registerBlockHandler(new BlockRenderMEConnector());
    RenderingRegistry.registerBlockHandler(new BlockRenderMEDenseConnector());
    RenderingRegistry.registerBlockHandler(new BlockRenderExtendedPost());
    ClientRegistry.bindTileEntitySpecialRenderer(TileMEWireConnector.class, new TileRenderMEConnector());
    ClientRegistry.bindTileEntitySpecialRenderer(TileMEDenseWireConnector.class, new TileRenderMEDenseConnector());
    ClientRegistry.bindTileEntitySpecialRenderer(TileExtendedPost.class, new TileRenderExtendedPost());
  }
}
