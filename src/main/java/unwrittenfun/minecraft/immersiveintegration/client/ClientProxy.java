package unwrittenfun.minecraft.immersiveintegration.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import unwrittenfun.minecraft.immersiveintegration.CommonProxy;
import unwrittenfun.minecraft.immersiveintegration.client.renderers.BlockRenderMEConnector;
import unwrittenfun.minecraft.immersiveintegration.client.renderers.BlockRenderMEDenseConnector;
import unwrittenfun.minecraft.immersiveintegration.client.renderers.TileRenderMEConnector;
import unwrittenfun.minecraft.immersiveintegration.client.renderers.TileRenderMEDenseConnector;
import unwrittenfun.minecraft.immersiveintegration.tiles.TileMEDenseWireConnector;
import unwrittenfun.minecraft.immersiveintegration.tiles.TileMEWireConnector;

public class ClientProxy extends CommonProxy {
  @Override
  public void registerRenderers() {
    RenderingRegistry.registerBlockHandler(new BlockRenderMEConnector());
    RenderingRegistry.registerBlockHandler(new BlockRenderMEDenseConnector());
    ClientRegistry.bindTileEntitySpecialRenderer(TileMEWireConnector.class, new TileRenderMEConnector());
    ClientRegistry.bindTileEntitySpecialRenderer(TileMEDenseWireConnector.class, new TileRenderMEDenseConnector());
  }
}
