package unwrittenfun.minecraft.immersiveintegration.compat;

import cpw.mods.fml.common.event.FMLInterModComms;
import net.minecraft.item.ItemStack;
import unwrittenfun.minecraft.immersiveintegration.ImmersiveIntegration;
import unwrittenfun.minecraft.immersiveintegration.blocks.IIBlocks;

public class FMPCompat extends CompatModule {
  @Override
  public String getModId() {
    return "ForgeMicroblock";
  }

  @Override
  public void init() {
    FMLInterModComms.sendMessage("ForgeMicroblock", "microMaterial", new ItemStack(IIBlocks.steelDecoration, 1, 0));
    FMLInterModComms.sendMessage("ForgeMicroblock", "microMaterial", new ItemStack(IIBlocks.steelDecoration, 1, 1));
    FMLInterModComms.sendMessage("ForgeMicroblock", "microMaterial", new ItemStack(IIBlocks.steelDecoration, 1, 2));

    if (ImmersiveIntegration.cfg.enableAE) {
      FMLInterModComms.sendMessage("ForgeMicroblock", "microMaterial", new ItemStack(IIBlocks.aeDecoration, 1, 0));
      FMLInterModComms.sendMessage("ForgeMicroblock", "microMaterial", new ItemStack(IIBlocks.aeDecoration, 1, 1));
    }
  }
}
