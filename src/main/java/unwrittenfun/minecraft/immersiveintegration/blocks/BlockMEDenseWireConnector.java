package unwrittenfun.minecraft.immersiveintegration.blocks;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import unwrittenfun.minecraft.immersiveintegration.tiles.TileMEDenseWireConnector;

public class BlockMEDenseWireConnector extends BlockMEWireConnector {
  public BlockMEDenseWireConnector(String key) {
    super(key);
  }

  @Override
  public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
    return new TileMEDenseWireConnector();
  }
}
