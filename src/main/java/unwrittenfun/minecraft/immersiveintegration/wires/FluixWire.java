package unwrittenfun.minecraft.immersiveintegration.wires;

import blusunrize.immersiveengineering.api.WireType;
import net.minecraft.item.ItemStack;
import unwrittenfun.minecraft.immersiveintegration.ImmersiveIntegration;
import unwrittenfun.minecraft.immersiveintegration.items.IIItems;
import unwrittenfun.minecraft.immersiveintegration.items.ItemCoil;

public class FluixWire extends WireType {
  @Override
  public String getUniqueName() {
    return "FLUIX";
  }

  @Override
  public double getLossRatio() {
    return 0;
  }

  @Override
  public int getTransferRate() {
    return 0;
  }

  @Override
  public int getColour() {
    return 6171760;
  }

  @Override
  public int getMaxLength() {
    return ImmersiveIntegration.cfg.fluixWireRange;
  }

  @Override
  public ItemStack getWireCoil() {
    return new ItemStack(IIItems.wireCoil, 1, ItemCoil.FLUIX_COIL_META);
  }

  @Override
  public double getRenderDiameter() {
    return 0.06f;
  }
}
