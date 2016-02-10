package unwrittenfun.minecraft.immersiveintegration.compat;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.redstone.IBundledRedstoneProvider;
import net.minecraftforge.common.util.ForgeDirection;
import unwrittenfun.minecraft.immersiveintegration.ImmersiveIntegration;
import unwrittenfun.minecraft.immersiveintegration.blocks.IIBlocks;
import unwrittenfun.minecraft.immersiveintegration.tiles.TileRedstoneWireConnector;
import unwrittenfun.minecraft.immersiveintegration.wires.RedstoneWireNetwork;

public class CCCompat extends CompatModule {
  @Override
  public String getModId() {
    return "ComputerCraft";
  }

  @Override
  public void init() {
    ComputerCraftAPI.registerBundledRedstoneProvider((IBundledRedstoneProvider) IIBlocks.redstoneWireConnector);
  }

  public static void updateRedstoneValues(RedstoneWireNetwork redstoneWireNetwork, TileRedstoneWireConnector connector) {
    for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
      int values = ComputerCraftAPI.getBundledRedstoneOutput(connector.getWorldObj(), connector.xCoord + direction.offsetX, connector.yCoord + direction.offsetY, connector.zCoord + direction.offsetZ, direction.getOpposite().ordinal());
      if (values != -1) {
        for (int i = 0; i < 16; i++) {
          redstoneWireNetwork.channelValues[i] = (byte) Math.max(redstoneWireNetwork.channelValues[i], ((values >> i) & 1) * 15);
        }
      }
    }
  }
}
