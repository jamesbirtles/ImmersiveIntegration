package unwrittenfun.minecraft.immersiveintegration.blocks;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import unwrittenfun.minecraft.immersiveintegration.client.renderers.BlockRenderMEConnector;
import unwrittenfun.minecraft.immersiveintegration.tiles.TileMEWireConnector;

public class BlockMEWireConnector extends BlockWireConnector {
  protected BlockMEWireConnector(String key) {
    super(key);
  }

  @Override
  public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
    return new TileMEWireConnector();
  }

  @Override
  public int getRenderType() {
    return BlockRenderMEConnector.RENDER_ID;
  }
}
