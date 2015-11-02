package unwrittenfun.minecraft.immersiveintegration.client.renderers;

import blusunrize.immersiveengineering.client.models.ModelIEObj;
import net.minecraft.block.Block;
import net.minecraft.util.IIcon;

public class ModelIIObj extends ModelIEObj {
  protected Block block;
  protected int meta;

  public ModelIIObj(String path, Block block) {
    this(path, block, 0);
  }

  public ModelIIObj(String path, Block block, int meta) {
    super(path);
    this.block = block;
    this.meta = meta;
  }

  @Override
  public IIcon getBlockIcon(String groupName) {
    return block.getIcon(0, meta); // TODO: Get meta and side from constructor, not needed yet so hasn't been done
  }
}
