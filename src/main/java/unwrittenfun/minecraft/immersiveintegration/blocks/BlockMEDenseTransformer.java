package unwrittenfun.minecraft.immersiveintegration.blocks;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import unwrittenfun.minecraft.immersiveintegration.tiles.TileMEDenseTransformer;

public class BlockMEDenseTransformer extends BlockMETransformer {
  public BlockMEDenseTransformer(String key) {
    super(key);
  }

  @Override
  public TileEntity createNewTileEntity(World world, int meta) {
    return new TileMEDenseTransformer();
  }
}
