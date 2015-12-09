package unwrittenfun.minecraft.immersiveintegration.tiles;

import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.WireType;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockOverlayText;
import mrtjp.projectred.api.IBundledTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import unwrittenfun.minecraft.immersiveintegration.wires.IIWires;
import unwrittenfun.minecraft.immersiveintegration.wires.RedstoneWireNetwork;

public class TileRedstoneWireConnector extends TileWireConnector implements IBundledTile, IBlockOverlayText {
  public static final String[] UNLOC_COLORS = new String[] { "white", "orange", "magenta", "lightBlue", "yellow", "lime", "pink", "grey", "lightGrey", "cyan", "purple", "blue", "brown", "green", "red", "black" };
  private static final int NUM_CHANNELS = 16;
  public boolean redstoneMode = false; // False - Output, True - Input
  public int redstoneChannel = 0;

  public RedstoneWireNetwork wireNetwork = new RedstoneWireNetwork().add(this);
  private boolean loaded = false;

  @Override
  public void updateEntity() {
    if (hasWorldObj() && !worldObj.isRemote && !loaded) {
      loaded = true;
      wireNetwork.removeFromNetwork(null);
    }
  }

  @Override
  public boolean canConnectCable(WireType cableType, TargetingInfo target) {
    return super.canConnectCable(cableType, target);
  }

  @Override
  public WireType getCableLimiter(TargetingInfo target) {
    return IIWires.redstoneWire;
  }

  public void toggleMode() {
    redstoneMode = !redstoneMode;
    wireNetwork.updateValues();
    wireNetwork.notifyOfChange(worldObj, xCoord, yCoord, zCoord);
    worldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
  }

  @Override
  public void writeCustomNBT(NBTTagCompound compound, boolean descPacket) {
    super.writeCustomNBT(compound, descPacket);
    compound.setBoolean("redstoneMode", redstoneMode);
    compound.setInteger("redstoneChannel", redstoneChannel);
  }

  @Override
  public void readCustomNBT(NBTTagCompound compound, boolean descPacket) {
    super.readCustomNBT(compound, descPacket);
    redstoneMode = compound.getBoolean("redstoneMode");
    redstoneChannel = compound.getInteger("redstoneChannel");
  }

  public void incrementChannel() {
    redstoneChannel++;
    if (redstoneChannel >= NUM_CHANNELS) {
      redstoneChannel = 0;
    }
    wireNetwork.updateValues();
    wireNetwork.notifyOfChange(worldObj, xCoord, yCoord, zCoord);
  }

  public String getChannelName() {
    return StatCollector.translateToLocal("immersiveintegration.color." + UNLOC_COLORS[redstoneChannel]);
  }

  public boolean isOutput() {
    return !redstoneMode;
  }

  public boolean isInput() {
    return redstoneMode;
  }

  public int getPowerOutput() {
    return wireNetwork.getPowerOutput(redstoneChannel);
  }

  @Override
  public void connectTo(int x, int y, int z) {
    TileEntity tileEntity = worldObj.getTileEntity(x, y, z);
    if (tileEntity instanceof TileRedstoneWireConnector) {
      TileRedstoneWireConnector wireConnector = (TileRedstoneWireConnector) tileEntity;
      if (wireConnector.wireNetwork != wireNetwork) {
        wireNetwork.mergeNetwork(wireConnector.wireNetwork);
      }
    }
  }

  @Override
  public void removeCable(ImmersiveNetHandler.Connection connection) {
    super.removeCable(connection);
    wireNetwork.removeFromNetwork(this);
  }

  @Override
  public int getRenderRadiusIncrease() {
    return IIWires.redstoneWire.getMaxLength();
  }


  /// Project red bundled cable support

  @Override
  public boolean canConnectBundled(int side) {
    return true;
  }

  @Override
  public byte[] getBundledSignal(int dir) {
    return isInput() ? null : wireNetwork.getByteValues();
  }


  /// IBlockOverlayText

  @Override
  public String[] getOverlayText(EntityPlayer player, MovingObjectPosition mop, boolean hammer) {
    if (!hammer) return null;
    return new String[] {
        StatCollector.translateToLocal("immersiveintegration.chat.channel") + " " + getChannelName(),
        StatCollector.translateToLocal("immersiveintegration.chat.mode") + " " + (redstoneMode ? "Input" : "Output")
    };
  }

  @Override
  public boolean useNixieFont(EntityPlayer player, MovingObjectPosition mop) {
    return false;
  }
}
