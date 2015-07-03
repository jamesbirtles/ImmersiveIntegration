package unwrittenfun.minecraft.immersiveintegration.wires;

import blusunrize.immersiveengineering.api.WireType;
import unwrittenfun.minecraft.immersiveintegration.ImmersiveIntegration;

public class IIWires {
  public static WireType fluixWire;
  public static WireType denseWire;

  public static void registerWires() {
    if (ImmersiveIntegration.cfg.enableAE) registerAE();
  }

  private static void registerAE() {
    fluixWire = new FluixWire();
    denseWire = new DenseWire();
  }
}
