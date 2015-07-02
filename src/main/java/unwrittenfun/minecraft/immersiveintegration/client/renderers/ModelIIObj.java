package unwrittenfun.minecraft.immersiveintegration.client.renderers;

import blusunrize.immersiveengineering.client.models.ModelIEObj;
import net.minecraft.block.Block;
import net.minecraft.util.IIcon;

public class ModelIIObj extends ModelIEObj {
  protected Block block;

  public ModelIIObj(String path, Block block) {
    super(path);
    this.block = block;
  }

  @Override
  public IIcon getBlockIcon() {
    return block.getIcon(0, 0); // TODO: Get meta and side from constructor, not needed yet so hasn't been done
  }
}
