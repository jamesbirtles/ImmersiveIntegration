package unwrittenfun.minecraft.immersiveintegration.wires;

import blusunrize.immersiveengineering.api.energy.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.WireType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import unwrittenfun.minecraft.immersiveintegration.ImmersiveIntegration;
import unwrittenfun.minecraft.immersiveintegration.items.IIItems;

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
  public int getColour(ImmersiveNetHandler.Connection connection) {
    return 6171760;
  }

  @Override
  public int getMaxLength() {
    return ImmersiveIntegration.cfg.fluixWireRange;
  }

  @Override
  public ItemStack getWireCoil() {
    return new ItemStack(IIItems.aeWireCoil, 1, IIItems.FLUIX_COIL_META);
  }

  @Override
  public double getRenderDiameter() {
    return 0.06f;
  }

  @Override
  public boolean isEnergyWire() {
    return false;
  }

  @Override
  public double getSlack() {
    return 1.005;
  }

  @Override
  public IIcon getIcon(ImmersiveNetHandler.Connection connection) {
    return iconDefaultWire;
  }
}
