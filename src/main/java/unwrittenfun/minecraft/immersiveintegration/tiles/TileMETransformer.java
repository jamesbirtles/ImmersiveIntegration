package unwrittenfun.minecraft.immersiveintegration.tiles;

import appeng.api.AEApi;
import appeng.api.exceptions.FailedConnection;
import appeng.api.networking.*;
import appeng.api.util.AECableType;
import appeng.api.util.AEColor;
import appeng.api.util.DimensionalCoord;
import appeng.me.GridException;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.WireType;
import blusunrize.immersiveengineering.common.blocks.TileEntityImmersiveConnectable;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;
import unwrittenfun.minecraft.immersiveintegration.ImmersiveIntegration;
import unwrittenfun.minecraft.immersiveintegration.blocks.IIBlocks;
import unwrittenfun.minecraft.immersiveintegration.wires.IIWires;

import java.util.EnumSet;
import java.util.Set;

public class TileMETransformer extends TileEntityImmersiveConnectable implements IGridHost, IGridBlock, IWireConnector {
  public IGridNode theGridNode;
  public IGridConnection theConnection;

  private boolean loaded = false;


  @Override
  public void updateEntity() {
    if (!loaded && hasWorldObj() && !worldObj.isRemote) {
      loaded = true;
      createAELink();

      if (canConnect()) {
        Set<ImmersiveNetHandler.Connection> connections = ImmersiveNetHandler.INSTANCE.getConnections(worldObj, Utils.toCC(this));
        if (connections != null && connections.iterator().hasNext()) {
          ImmersiveNetHandler.Connection connection = connections.iterator().next();
          ChunkCoordinates opposite = connection.end;
          if (opposite.equals(Utils.toCC(this))) {
            return;
          }

          connectTo(opposite.posX, opposite.posY, opposite.posZ);
        }
      }
    }
  }

  @Override
  public double getIdlePowerUsage() {
    return canConnect() ? 0 : ImmersiveIntegration.cfg.meTransformerPowerDrain;
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
  }

  @Override
  public EnumSet<ForgeDirection> getConnectableSides() {
    return canConnect() ? EnumSet.of(ForgeDirection.DOWN) : EnumSet.range(ForgeDirection.DOWN, ForgeDirection.EAST);
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
    return canConnect() ? null : new ItemStack(IIBlocks.meTransformer);
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
  public boolean canConnectCable(WireType cableType, TargetingInfo target) {
    return cableType == IIWires.fluixWire && limitType == null;
  }

  @Override
  public boolean allowEnergyToPass(ImmersiveNetHandler.Connection con) {
    return false;
  }

  @Override
  public void removeCable(ImmersiveNetHandler.Connection connection) {
    if (theConnection != null && !worldObj.isRemote) {
      theConnection.destroy();
      theConnection = null;
    }

    super.removeCable(connection);
  }

  @Override
  public Vec3 getRaytraceOffset(IImmersiveConnectable link) {
    return Vec3.createVectorHelper(0.5, 1.5, .5);
  }

  @Override
  public Vec3 getConnectionOffset(ImmersiveNetHandler.Connection con) {
    return Vec3.createVectorHelper(.5, 1.40, .5);
  }

  public void connectTo(int x, int y, int z) {
    TileEntity teOpposite = worldObj.getTileEntity(x, y, z);
    if (teOpposite instanceof IGridHost) {
      IGridNode nodeA = ((IGridHost) teOpposite).getGridNode(ForgeDirection.UNKNOWN);
      IGridNode nodeB = getGridNode(ForgeDirection.UNKNOWN);
      try {
        if (theConnection != null) {
          theConnection.destroy();
          theConnection = null;
        }
        theConnection = AEApi.instance().createGridConnection(nodeA, nodeB);
      } catch (FailedConnection failedConnection) {
        if (ImmersiveIntegration.cfg.verboseLogging) ImmersiveIntegration.log.info(failedConnection.getMessage());
      } catch (GridException ignored) {}
    }
  }

  public AxisAlignedBB getRenderBoundingBox() {
    return AxisAlignedBB.getBoundingBox(xCoord, yCoord - 1, zCoord, xCoord + 1, yCoord + 2, zCoord + 1);
  }

  @Override
  public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket) {
    super.writeCustomNBT(nbt, descPacket);

    if (theGridNode == null) {
      createAELink();
    }
  }
}
