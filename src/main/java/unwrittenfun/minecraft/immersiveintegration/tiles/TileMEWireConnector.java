package unwrittenfun.minecraft.immersiveintegration.tiles;

import appeng.api.AEApi;
import appeng.api.exceptions.FailedConnection;
import appeng.api.networking.*;
import appeng.api.util.AECableType;
import appeng.api.util.AEColor;
import appeng.api.util.DimensionalCoord;
import appeng.me.GridException;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.WireType;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.util.ForgeDirection;
import unwrittenfun.minecraft.immersiveintegration.ImmersiveIntegration;
import unwrittenfun.minecraft.immersiveintegration.blocks.IIBlocks;
import unwrittenfun.minecraft.immersiveintegration.wires.IIWires;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Set;

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

      Set<ImmersiveNetHandler.Connection> connections = ImmersiveNetHandler.INSTANCE.getConnections(worldObj, Utils.toCC(this));
      if (connections != null) {
        for (ImmersiveNetHandler.Connection connection : connections) {
          ChunkCoordinates opposite = connection.end;
          if (opposite.equals(Utils.toCC(this))) {
            break;
          }

          connectTo(opposite.posX, opposite.posY, opposite.posZ);
        }
      }
    }
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
  public WireType getCableLimiter(TargetingInfo target) {
    return IIWires.fluixWire;
  }

  @Override
  public void removeCable(ImmersiveNetHandler.Connection connection) {
    if (!worldObj.isRemote) {
      ChunkCoordinates opposite = connection.end;
      if (opposite.equals(Utils.toCC(this))) {
        return;
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

  @Override
  public void onChunkUnload() {
    if (worldObj != null && !worldObj.isRemote) {
      destroyAELink();
    }
  }

  /// IGridBlock

  @Override
  public double getIdlePowerUsage() {
    return ImmersiveIntegration.cfg.meWireConnectorDrain;
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
    return EnumSet.noneOf(ForgeDirection.class);
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

  @Override
  public void connectTo(int x, int y, int z) {
    TileEntity teOpposite = worldObj.getTileEntity(x, y, z);
    if (teOpposite instanceof IGridHost) {
      IGridNode nodeA = ((IGridHost) teOpposite).getGridNode(ForgeDirection.UNKNOWN);
      IGridNode nodeB = getGridNode(ForgeDirection.UNKNOWN);
      try {
        gridConnections.add(AEApi.instance().createGridConnection(nodeA, nodeB));
      } catch (FailedConnection failedConnection) {
        if (ImmersiveIntegration.cfg.verboseLogging) ImmersiveIntegration.log.info(failedConnection.getMessage());
      } catch (GridException ignored) {
      }
    }
  }

  @Override
  public int getRenderRadiusIncrease() {
    return IIWires.fluixWire.getMaxLength();
  }

  @Override
  public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket) {
    super.writeCustomNBT(nbt, descPacket);

    if (theGridNode == null) {
      createAELink();
    }
  }
}
