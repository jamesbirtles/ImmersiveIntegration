package unwrittenfun.minecraft.immersiveintegration.special;

import cpw.mods.fml.common.event.FMLServerStartingEvent;
import unwrittenfun.minecraft.immersiveintegration.ImmersiveIntegration;
import unwrittenfun.minecraft.immersiveintegration.special.network.SpecialNetwork;

public class Special {
  public static SpecialNetwork network;

  public static void preInit() {
    network = new SpecialNetwork();

    ImmersiveIntegration.proxy.registerClientSpecial();
  }

  public static void serverLoad(FMLServerStartingEvent event) {
    event.registerServerCommand(new SpecialCommand());
  }
}
