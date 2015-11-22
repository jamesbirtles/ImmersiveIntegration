package unwrittenfun.minecraft.immersiveintegration.compat;

import cpw.mods.fml.common.event.FMLInterModComms;

public class WailaCompat extends CompatModule {
  @Override
  public String getModId() {
    return "Waila";
  }

  @Override
  public void init() {
    FMLInterModComms.sendMessage("Waila", "register", "unwrittenfun.minecraft.immersiveintegration.waila.WailaHandler.init");
  }
}
