package unwrittenfun.minecraft.immersiveintegration.wires;

import blusunrize.immersiveengineering.api.WireType;

public class IIWires {
  public static WireType fluixWire;
  public static WireType denseWire;

  public static void registerWires() {
    fluixWire = new FluixWire();
    denseWire = new DenseWire();
  }
}
