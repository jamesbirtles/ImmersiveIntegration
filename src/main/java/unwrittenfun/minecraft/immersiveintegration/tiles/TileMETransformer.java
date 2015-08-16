package unwrittenfun.minecraft.immersiveintegration.tiles;

import appeng.api.AEApi;
import appeng.api.exceptions.FailedConnection;
import appeng.api.networking.*;
import appeng.api.util.AECableType;
import appeng.api.util.AEColor;
import appeng.api.util.DimensionalCoord;
import appeng.me.GridNode;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.WireType;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;
import unwrittenfun.minecraft.immersiveintegration.ImmersiveIntegration;
import unwrittenfun.minecraft.immersiveintegration.blocks.IIBlocks;
import unwrittenfun.minecraft.immersiveintegration.wires.IIWires;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class TileMETransformer extends TileEntity implements IGridHost, IGridBlock, IImmersiveConnectable, IWireConnector {
  public IGrid theGrid;
  public IGridNode theGridNode;
  public ArrayList<IGridConnection> gridConnections = new ArrayList<>();
  private boolean loaded = false;

  @Override
  public void updateEntity() {
    if (!loaded && hasWorldObj() && !worldObj.isRemote) {
      loaded = true;
      createAELink();
      for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
        TileEntity tileEntity = worldObj.getTileEntity(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);
        if (tileEntity instanceof IGridHost) {
          IGridNode node = ((IGridHost) tileEntity).getGridNode(direction);
          if (node != null) {
            node.updateState();
          }
        }
      }

      if ((getBlockMetadata() & 8) != 8) {
        TileEntity tileEntity = worldObj.getTileEntity(xCoord, yCoord + 1, zCoord);
        if (tileEntity instanceof TileMETransformer) {
          try {
            gridConnections.add(AEApi.instance().createGridConnection(((TileMETransformer) tileEntity).getGridNode(ForgeDirection.DOWN), getGridNode(ForgeDirection.UP)));
          } catch (FailedConnection failedConnection) {
            failedConnection.printStackTrace();
          }
        }
      }

      for (ImmersiveNetHandler.Connection connection : ImmersiveNetHandler.INSTANCE.getConnections(worldObj, Utils.toCC(this))) {
        ChunkCoordinates opposite = connection.end;
        if (connection.end.equals(Utils.toCC(this))) {
          opposite = connection.start;
        }

        TileEntity teOpposite = worldObj.getTileEntity(opposite.posX, opposite.posY, opposite.posZ);
        if (teOpposite instanceof TileMEWireConnector) {
          GridNode nodeA = (GridNode) ((TileMEWireConnector) teOpposite).getGridNode(ForgeDirection.UNKNOWN);
          GridNode nodeB = (GridNode) getGridNode(ForgeDirection.UNKNOWN);
          if (!nodeA.hasConnection(nodeB) && !nodeB.hasConnection(nodeA)) {
            try {
              gridConnections.add(AEApi.instance().createGridConnection(nodeA, nodeB));
            } catch (FailedConnection failedConnection) {
              failedConnection.printStackTrace();
            }
          }
        }
      }
    }
  }

  @Override
  public Packet getDescriptionPacket() {
    NBTTagCompound compound = new NBTTagCompound();

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

  @Override
  public double getIdlePowerUsage() {
    return (getBlockMetadata() & 8) == 8 ? 0 : ImmersiveIntegration.cfg.meTransformerPowerDrain;
  }

  @Override
  public EnumSet<GridFlags> getFlags() {
    return EnumSet.noneOf(GridFlags.class);
  }

  @Override
  public boolean isWorldAccessible() {
    return true;
  }

  @Override
  public DimensionalCoord getLocation() {
    return new DimensionalCoord(this);
  }

  @Override
  public AEColor getGridColor() {
    return AEColor.Transparent;
  }

  @Override
  public void onGridNotification(GridNotification notification) {

  }

  @Override
  public void setNetworkStatus(IGrid grid, int channelsInUse) {
    theGrid = grid;
  }

  @Override
  public EnumSet<ForgeDirection> getConnectableSides() {
    return getBlockMetadata() == 0 ? EnumSet.range(ForgeDirection.DOWN, ForgeDirection.EAST) : EnumSet.noneOf(ForgeDirection.class);
  }

  @Override
  public IGridHost getMachine() {
    return this;
  }

  @Override
  public void gridChanged() {

  }

  @Override
  public ItemStack getMachineRepresentation() {
    return (getBlockMetadata() & 8) == 8 ? null : new ItemStack(IIBlocks.meTransformer);
  }

  @Override
  public IGridNode getGridNode(ForgeDirection dir) {
    if (theGridNode == null) createAELink();
    return theGridNode;
  }

  @Override
  public AECableType getCableConnectionType(ForgeDirection dir) {
    return AECableType.SMART;
  }

  @Override
  public void securityBreak() {

  }

  public void createAELink() {
    if (!worldObj.isRemote) {
      if (theGridNode == null) theGridNode = AEApi.instance().createGridNode(this);
      theGridNode.updateState();
    }
  }

  public void destroyAELink() {
    if (theGridNode != null) theGridNode.destroy();
  }

  @Override
  public void invalidate() {
    super.invalidate();
    if (worldObj != null && !worldObj.isRemote) {
      destroyAELink();
    }

    if (worldObj != null && !worldObj.isRemote) {
      ImmersiveNetHandler.INSTANCE.clearAllConnectionsFor(Utils.toCC(this), worldObj);
    }
  }

  @Override
  public void onChunkUnload() {
    if (worldObj != null && !worldObj.isRemote) {
      destroyAELink();
    }
  }


  // IImmersiveConnectable

  @Override
  public boolean canConnect() {
    return (getBlockMetadata() & 8) == 8;
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
    return getCableLimiter(target) == cableType;
  }

  @Override
  public void connectCable(WireType cableType, TargetingInfo target) {

  }

  @Override
  public WireType getCableLimiter(TargetingInfo target) {
    return IIWires.fluixWire;
  }

  @Override
  public boolean allowEnergyToPass(ImmersiveNetHandler.Connection con) {
    return false;
  }

  @Override
  public void removeCable(ImmersiveNetHandler.Connection connection) {
    if (!worldObj.isRemote) {
      ChunkCoordinates opposite = connection.end;
      if (connection.end.equals(Utils.toCC(this))) {
        opposite = connection.start;
      }

      for (IGridConnection gridConnection : gridConnections) {
        DimensionalCoord locA = gridConnection.a().getGridBlock().getLocation();
        DimensionalCoord locB = gridConnection.b().getGridBlock().getLocation();
        if ((opposite.posX == locA.x && opposite.posZ == locA.z && opposite.posY == locA.y) || (opposite.posX == locB.x && opposite.posZ == locB.z && opposite.posY == locB.y)) {
          gridConnection.destroy();
          gridConnections.remove(gridConnection);
          break;
        }
      }
    }

    this.markDirty();
    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
  }

  @Override
  public Vec3 getRaytraceOffset() {
    return Vec3.createVectorHelper(0.5, 1.5, .5);
  }

  @Override
  public Vec3 getConnectionOffset(ImmersiveNetHandler.Connection con) {
    switch (getBlockMetadata() & 7) {
      case 2:
        return Vec3.createVectorHelper(.2, 1.45, .5);
      case 3:
        return Vec3.createVectorHelper(.8, 1.45, .5);
      case 4:
        return Vec3.createVectorHelper(.5, 1.45, .8);
      case 5:
        return Vec3.createVectorHelper(.5, 1.45, .2);
    }
    return Vec3.createVectorHelper(.5, 1.45, .5);
  }

  public void connectTo(int x, int y, int z) {
    TileEntity tileEntity = worldObj.getTileEntity(x, y, z);
    if (tileEntity instanceof TileMETransformer) {
      TileMETransformer connector = (TileMETransformer) tileEntity;
      try {
        gridConnections.add(AEApi.instance().createGridConnection(connector.getGridNode(ForgeDirection.UNKNOWN), getGridNode(ForgeDirection.UNKNOWN)));
      } catch (FailedConnection failedConnection) {
        failedConnection.printStackTrace();
        ImmersiveIntegration.log.error("Something went wrong connecting the fluix wire!");
      }
    }
  }

  public AxisAlignedBB getRenderBoundingBox() {
    return AxisAlignedBB.getBoundingBox(xCoord, yCoord - 1, zCoord, xCoord + 1, yCoord + 2, zCoord + 1);
  }
}
