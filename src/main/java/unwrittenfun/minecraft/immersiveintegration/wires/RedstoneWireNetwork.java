package unwrittenfun.minecraft.immersiveintegration.wires;

import blusunrize.immersiveengineering.api.ImmersiveNetHandler;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import unwrittenfun.minecraft.immersiveintegration.ImmersiveIntegration;
import unwrittenfun.minecraft.immersiveintegration.blocks.IIBlocks;
import unwrittenfun.minecraft.immersiveintegration.tiles.TileRedstoneWireConnector;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RedstoneWireNetwork {
  public int[] channelValues = new int[16];

  public List<WeakReference<TileRedstoneWireConnector>> connectors = new ArrayList<>();

  public RedstoneWireNetwork add(TileRedstoneWireConnector connector) {
    connectors.add(new WeakReference<>(connector));
    return this;
  }

  public boolean isLonely() {
    return connectors.size() == 1;
  }

  public void mergeNetwork(RedstoneWireNetwork wireNetwork) {
    for (WeakReference<TileRedstoneWireConnector> connectorRef : wireNetwork.connectors) {
      TileRedstoneWireConnector connector = connectorRef.get();
      if (connector != null) {
        connector.wireNetwork = add(connector);
      }
    }

    for (WeakReference<TileRedstoneWireConnector> connectorRef : wireNetwork.connectors) {
      TileRedstoneWireConnector connector = connectorRef.get();
      if (connector != null) {
        connector.getWorldObj().notifyBlockOfNeighborChange(connector.xCoord, connector.yCoord, connector.zCoord, IIBlocks.redstoneWireConnector);
      }
    }
  }

  public void removeFromNetwork(TileRedstoneWireConnector removedConnector) {
    ChunkCoordinates removedCC = Utils.toCC(removedConnector);
    for (WeakReference<TileRedstoneWireConnector> connectorRef : connectors) {
      TileRedstoneWireConnector connector = connectorRef.get();
      if (connector != null) {
        connector.wireNetwork = new RedstoneWireNetwork().add(connector);
      }
    }

    for (WeakReference<TileRedstoneWireConnector> connectorRef : connectors) {
      TileRedstoneWireConnector connector = connectorRef.get();
      if (connector != null) {
        ChunkCoordinates conCC = Utils.toCC(connector);
        List<ImmersiveNetHandler.Connection> connections = ImmersiveNetHandler.INSTANCE.getConnections(connector.getWorldObj(), conCC);
        for (ImmersiveNetHandler.Connection connection : connections) {
          ChunkCoordinates node = connection.start;
          if (node.equals(conCC)) {
            node = connection.end;
          }
          if (!node.equals(removedCC)) {
            TileEntity tileEntity = connector.getWorldObj().getTileEntity(node.posX, node.posY, node.posZ);
            if (tileEntity instanceof TileRedstoneWireConnector) {
              TileRedstoneWireConnector wireConnector = (TileRedstoneWireConnector) tileEntity;
              if (connector.wireNetwork != wireConnector.wireNetwork) {
                connector.wireNetwork.mergeNetwork(wireConnector.wireNetwork);
              }
            }
          }
        }

        connector.getWorldObj().notifyBlocksOfNeighborChange(connector.xCoord, connector.yCoord, connector.zCoord, IIBlocks.redstoneWireConnector);
      }
    }
  }

  public void updateValues() {
    int[] oldValues = channelValues;
    channelValues = new int[16];
    for (WeakReference<TileRedstoneWireConnector> connectorRef : connectors) {
      TileRedstoneWireConnector connector = connectorRef.get();
      if (connector != null) {
        if (connector.isInput()) {
          channelValues[connector.redstoneChannel] = Math.max(connector.getWorldObj().getStrongestIndirectPower(connector.xCoord, connector.yCoord, connector.zCoord), channelValues[connector.redstoneChannel]);
        }
      }
    }

    if (!Arrays.equals(oldValues, channelValues)) {
      for (WeakReference<TileRedstoneWireConnector> connectorRef : connectors) {
        TileRedstoneWireConnector connector = connectorRef.get();
        if (connector != null) {
          connector.getWorldObj().notifyBlocksOfNeighborChange(connector.xCoord, connector.yCoord, connector.zCoord, IIBlocks.redstoneWireConnector);
        }
      }
    }
  }

  public int getPowerOutput(int redstoneChannel) {
    return channelValues[redstoneChannel];
  }
}
