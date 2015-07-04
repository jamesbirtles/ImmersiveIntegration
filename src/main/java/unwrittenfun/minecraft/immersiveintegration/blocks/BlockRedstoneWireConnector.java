package unwrittenfun.minecraft.immersiveintegration.blocks;

import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import unwrittenfun.minecraft.immersiveintegration.tiles.TileRedstoneWireConnector;

public class BlockRedstoneWireConnector extends BlockWireConnector {
  protected BlockRedstoneWireConnector(String key) {
    super(key);
  }

  @Override
  public TileEntity createNewTileEntity(World world, int meta) {
    return new TileRedstoneWireConnector();
  }

  @Override
  public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
    if (!Utils.isHammer(player.getCurrentEquippedItem())) {
      if (player.getCurrentEquippedItem() == null) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof TileRedstoneWireConnector) {
          TileRedstoneWireConnector wireConnector = (TileRedstoneWireConnector) tileEntity;
          if (!world.isRemote) {
            player.addChatComponentMessage(new ChatComponentText("Channel: " + wireConnector.getChannelName()));
            player.addChatComponentMessage(new ChatComponentText("Mode: " + (wireConnector.redstoneMode ? "Input" : "Output")));

            //            player.addChatComponentMessage(new ChatComponentText("Network ID: " + wireConnector.wireNetwork.hashCode()));
            //            player.addChatComponentMessage(new ChatComponentText("Network Size: " + wireConnector.wireNetwork.connectors.size()));
            //            player.addChatComponentMessage(new ChatComponentText("Network Outputs: " + Arrays.toString(wireConnector.wireNetwork.channelValues)));
          }
        }
      }
      return false;
    }

    TileEntity tileEntity = world.getTileEntity(x, y, z);
    if (tileEntity instanceof TileRedstoneWireConnector) {
      TileRedstoneWireConnector wireConnector = (TileRedstoneWireConnector) tileEntity;

      if (player.isSneaking()) {
        wireConnector.incrementChannel();
        if (!world.isRemote)
          player.addChatComponentMessage(new ChatComponentText("Channel Set: " + wireConnector.getChannelName()));
      } else {
        wireConnector.toggleMode();
        if (!world.isRemote)
          player.addChatComponentMessage(new ChatComponentText("Mode Set: " + (wireConnector.redstoneMode ? "Input" : "Output")));
      }
    }

    return true;
  }

  @Override
  public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
    TileEntity tileEntity = world.getTileEntity(x, y, z);
    if (tileEntity instanceof TileRedstoneWireConnector) {
      ((TileRedstoneWireConnector) tileEntity).wireNetwork.updateValues();
    }
    super.onNeighborBlockChange(world, x, y, z, block);
  }

  @Override
  public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side) {
    TileEntity tileEntity = world.getTileEntity(x, y, z);
    if (tileEntity instanceof TileRedstoneWireConnector) {
      TileRedstoneWireConnector wireConnector = (TileRedstoneWireConnector) tileEntity;
      if (wireConnector.isOutput()) {
        return wireConnector.getPowerOutput();
      }
    }

    return 0;
  }

  @Override
  public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side) {
    return isProvidingStrongPower(world, x, y, z, side);
  }
}
