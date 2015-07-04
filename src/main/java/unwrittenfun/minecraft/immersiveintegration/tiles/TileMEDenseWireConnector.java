package unwrittenfun.minecraft.immersiveintegration.tiles;


import appeng.api.networking.GridFlags;
import appeng.api.util.AECableType;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.WireType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import unwrittenfun.minecraft.immersiveintegration.blocks.IIBlocks;
import unwrittenfun.minecraft.immersiveintegration.wires.IIWires;

import java.util.EnumSet;

public class TileMEDenseWireConnector extends TileMEWireConnector {
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
    return new ItemStack(IIBlocks.meDenseWireConnector);
  }
}
