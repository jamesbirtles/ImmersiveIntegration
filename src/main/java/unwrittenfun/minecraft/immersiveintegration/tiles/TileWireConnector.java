package unwrittenfun.minecraft.immersiveintegration.tiles;

import blusunrize.immersiveengineering.api.energy.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.WireType;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public abstract class TileWireConnector extends TileEntity implements IImmersiveConnectable {
  @Override
  public boolean canConnect() {
    return true;
  }

  @Override
  public boolean isEnergyOutput() {
    return false;
  }

  @Override
  public int outputEnergy(int amount, boolean simulate, int energyType) {
    return 0;
  }

  @Override
  public boolean canConnectCable(WireType cableType, TargetingInfo target) {
    return cableType == getCableLimiter(target);
  }

  @Override
  public void connectCable(WireType cableType, TargetingInfo target) {

  }

  @Override
  public boolean allowEnergyToPass(ImmersiveNetHandler.Connection con) {
    return false;
  }

  @Override
  public void removeCable(ImmersiveNetHandler.Connection connection) {
    this.markDirty();
    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
  }

  @Override
  public Vec3 getRaytraceOffset() {
    ForgeDirection fd = ForgeDirection.getOrientation(getBlockMetadata()).getOpposite();
    return Vec3.createVectorHelper(.5 + .5 * fd.offsetX, .5 + .5 * fd.offsetY, .5 + .5 * fd.offsetZ);
  }

  @Override
  public Vec3 getConnectionOffset(ImmersiveNetHandler.Connection con) {
    return Vec3.createVectorHelper(0.5, 0.5, 0.5);
  }

  @Override
  public Packet getDescriptionPacket() {
    NBTTagCompound compound = new NBTTagCompound();

    writeNBT(compound);

    if (worldObj != null && !worldObj.isRemote) {
      NBTTagList connectionList = new NBTTagList();
      List<ImmersiveNetHandler.Connection> conL = ImmersiveNetHandler.INSTANCE.getConnections(worldObj, Utils.toCC(this));
      for (ImmersiveNetHandler.Connection con : conL) {
        connectionList.appendTag(con.writeToNBT());
      }
      compound.setTag("connectionList", connectionList);
    }


    return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, compound);
  }

  @Override
  public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
    NBTTagCompound compound = pkt.func_148857_g();

    readNBT(compound);
    worldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
    if (worldObj != null && worldObj.isRemote) {
      NBTTagList connectionList = compound.getTagList("connectionList", 10);
      ImmersiveNetHandler.INSTANCE.clearConnectionsOriginatingFrom(Utils.toCC(this), worldObj);
      for (int i = 0; i < connectionList.tagCount(); i++) {
        NBTTagCompound conTag = connectionList.getCompoundTagAt(i);
        ImmersiveNetHandler.Connection con = ImmersiveNetHandler.Connection.readFromNBT(conTag);
        if (con != null) {
          ImmersiveNetHandler.INSTANCE.addConnection(worldObj, Utils.toCC(this), con);
        }
      }
    }
  }

  public void writeNBT(NBTTagCompound compound) {
  }

  public void readNBT(NBTTagCompound compound) {
  }

  @Override
  public void invalidate() {
    super.invalidate();
    if (worldObj != null && !worldObj.isRemote) {
      ImmersiveNetHandler.INSTANCE.clearAllConnectionsFor(Utils.toCC(this), worldObj);
    }
  }

  public void connectTo(int x, int y, int z) {

  }
}
