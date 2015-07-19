package unwrittenfun.minecraft.immersiveintegration.multiblocks;

import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.api.MultiblockHandler.IMultiblock;

public class IIMultiblocks {
  public static IMultiblock industrialCokeOven;

  public static void registerMultiblocks() {
    MultiblockHandler.registerMultiblock(industrialCokeOven = new MultiblockIndustrialCokeOven());
  }
}
