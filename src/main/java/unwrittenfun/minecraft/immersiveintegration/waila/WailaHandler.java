package unwrittenfun.minecraft.immersiveintegration.waila;

import mcp.mobius.waila.api.IWailaRegistrar;
import unwrittenfun.minecraft.immersiveintegration.blocks.BlockIndustrialCokeOven;

public class WailaHandler {
  public static void init(IWailaRegistrar registrar) {
    registrar.registerStackProvider(new CokeOvenDataProvider(), BlockIndustrialCokeOven.class);
  }
}
