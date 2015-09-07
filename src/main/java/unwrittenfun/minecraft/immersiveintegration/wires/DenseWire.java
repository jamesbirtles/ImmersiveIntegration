package unwrittenfun.minecraft.immersiveintegration.wires;

import net.minecraft.item.ItemStack;
import unwrittenfun.minecraft.immersiveintegration.ImmersiveIntegration;
import unwrittenfun.minecraft.immersiveintegration.items.IIItems;

public class DenseWire extends FluixWire {
  @Override
  public String getUniqueName() {
    return "DENSE";
  }

  @Override
  public int getMaxLength() {
    return ImmersiveIntegration.cfg.denseWireRange;
  }

  @Override
  public ItemStack getWireCoil() {
    return new ItemStack(IIItems.aeWireCoil, 1, IIItems.DENSE_COIL_META);
  }

  @Override
  public double getRenderDiameter() {
    return 0.1f;
  }

  @Override
  public double getSlack() {
    return 1.008;
  }
}
