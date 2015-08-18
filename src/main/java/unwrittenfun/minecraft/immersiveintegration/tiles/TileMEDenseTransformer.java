package unwrittenfun.minecraft.immersiveintegration.tiles;

import appeng.api.networking.GridFlags;
import appeng.api.util.AECableType;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.WireType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import unwrittenfun.minecraft.immersiveintegration.ImmersiveIntegration;
import unwrittenfun.minecraft.immersiveintegration.blocks.IIBlocks;
import unwrittenfun.minecraft.immersiveintegration.wires.IIWires;

import java.util.EnumSet;

public class TileMEDenseTransformer extends TileMETransformer {
  @Override
  public double getIdlePowerUsage() {
    return (getBlockMetadata() & 8) == 8 ? 0 : ImmersiveIntegration.cfg.meDenseTransformerPowerDrain;
  }

  @Override
  public AECableType getCableConnectionType(ForgeDirection dir) {
    return AECableType.DENSE;
  }

  @Override
  public EnumSet<GridFlags> getFlags() {
    return EnumSet.of(GridFlags.DENSE_CAPACITY);
  }

  @Override
  public ItemStack getMachineRepresentation() {
    return (getBlockMetadata() & 8) == 8 ? null : new ItemStack(IIBlocks.meDenseTransformer);
  }

  public boolean canConnectCable(WireType cableType, TargetingInfo target) {
    return cableType == IIWires.denseWire;
  }
}
