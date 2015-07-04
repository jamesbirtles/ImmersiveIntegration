package unwrittenfun.minecraft.immersiveintegration.wires;

import blusunrize.immersiveengineering.api.energy.WireType;
import net.minecraft.item.ItemStack;
import unwrittenfun.minecraft.immersiveintegration.items.IIItems;

public class RedstoneWire extends WireType {
  @Override
  public String getUniqueName() {
    return "REDSTONE";
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
    return 12981011;
  }

  @Override
  public int getMaxLength() {
    return 32;
  }

  @Override
  public ItemStack getWireCoil() {
    return new ItemStack(IIItems.iiWireCoil, 1, IIItems.REDSTONE_COIL_META);
  }

  @Override
  public double getRenderDiameter() {
    return 0.03125;
  }

  @Override
  public boolean isEnergyWire() {
    return false;
  }
}
