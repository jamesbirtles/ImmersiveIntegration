package unwrittenfun.minecraft.immersiveintegration.blocks;

import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import unwrittenfun.minecraft.immersiveintegration.tiles.TileRedstoneWireConnector;

public class BlockRedstoneWireConnector extends BlockWireConnector {
  public static IIcon inputIcon;

  protected BlockRedstoneWireConnector(String key) {
    super(key);
  }

  @Override
  public void registerBlockIcons(IIconRegister iconRegister) {
    super.registerBlockIcons(iconRegister);
    inputIcon = iconRegister.registerIcon(getTextureName() + "Input");
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
            player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("immersiveintegration.chat.channel") + " " + wireConnector.getChannelName()));
            player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("immersiveintegration.chat.mode") + " " + (wireConnector.redstoneMode ? "Input" : "Output")));

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
          player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("immersiveintegration.chat.channelSet") + " " + wireConnector.getChannelName()));
      } else {
        wireConnector.toggleMode();
        if (!world.isRemote)
          player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("immersiveintegration.chat.modeSet") + " " + (wireConnector.redstoneMode ? StatCollector.translateToLocal("immersiveintegration.chat.input") : StatCollector.translateToLocal("immersiveintegration.chat.output"))));
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
    if (ForgeDirection.OPPOSITES[side] == world.getBlockMetadata(x, y, z)) {
      return isProvidingWeakPower(world, x, y, z, side);
    }

    return 0;
  }

  @Override
  public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side) {
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
  public boolean canProvidePower() {
    return true;
  }

  @Override
  public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {
    return false;
  }
}
