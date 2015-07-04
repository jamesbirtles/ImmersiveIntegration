package unwrittenfun.minecraft.immersiveintegration.tiles;

import appeng.api.AEApi;
import appeng.api.exceptions.FailedConnection;
import appeng.api.networking.*;
import appeng.api.util.AECableType;
import appeng.api.util.AEColor;
import appeng.api.util.DimensionalCoord;
import appeng.me.GridNode;
import blusunrize.immersiveengineering.api.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.WireType;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;
import unwrittenfun.minecraft.immersiveintegration.ImmersiveIntegration;
import unwrittenfun.minecraft.immersiveintegration.blocks.IIBlocks;
import unwrittenfun.minecraft.immersiveintegration.wires.IIWires;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class TileMEWireConnector extends TileWireConnector implements IGridHost, IGridBlock {
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
  }

  @Override
  public void writeNBT(NBTTagCompound compound) {
    super.writeNBT(compound);

    if (theGridNode == null) {
      createAELink();
    }
  }

  public void createAELink() {
    if (!worldObj.isRemote) {
      if (theGridNode == null) theGridNode = AEApi.instance().createGridNode(this);
      theGridNode.updateState();
    }
  }

  public void destroyAELink() {
    theGridNode.destroy();
  }

  @Override
  public WireType getCableLimiter(TargetingInfo target) {
    return IIWires.fluixWire;
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

    super.removeCable(connection);
  }

  @Override
  public void invalidate() {
    super.invalidate();
    if (worldObj != null && !worldObj.isRemote) {
      destroyAELink();
    }
  }

  /// IGridBlock

  @Override
  public double getIdlePowerUsage() {
    return 0;
  }

  @Override
  public EnumSet<GridFlags> getFlags() {
    return EnumSet.noneOf(GridFlags.class);
  }

  @Override
  public boolean isWorldAccessible() {
    return false;
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
    return EnumSet.of(ForgeDirection.getOrientation(getBlockMetadata()));
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
    return new ItemStack(IIBlocks.meWireConnector);
  }


  /// IGridHost

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

  public void connectTo(int x, int y, int z) {
    TileEntity tileEntity = worldObj.getTileEntity(x, y, z);
    if (tileEntity instanceof TileMEWireConnector) {
      TileMEWireConnector connector = (TileMEWireConnector) tileEntity;
      try {
        gridConnections.add(AEApi.instance().createGridConnection(connector.getGridNode(ForgeDirection.UNKNOWN), getGridNode(ForgeDirection.UNKNOWN)));
      } catch (FailedConnection failedConnection) {
        failedConnection.printStackTrace();
        ImmersiveIntegration.log.error("Something went wrong connecting the flux wire!");
      }
    }
  }
}
