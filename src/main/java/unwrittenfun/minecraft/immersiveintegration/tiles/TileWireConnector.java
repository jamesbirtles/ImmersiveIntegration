package unwrittenfun.minecraft.immersiveintegration.tiles;

import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.WireType;
import blusunrize.immersiveengineering.common.blocks.TileEntityImmersiveConnectable;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class TileWireConnector extends TileEntityImmersiveConnectable implements IWireConnector {
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
  public Vec3 getRaytraceOffset(IImmersiveConnectable link) {
    ForgeDirection fd = ForgeDirection.getOrientation(getBlockMetadata()).getOpposite();
    return Vec3.createVectorHelper(.5 + .5 * fd.offsetX, .5 + .5 * fd.offsetY, .5 + .5 * fd.offsetZ);
  }

  @Override
  public Vec3 getConnectionOffset(ImmersiveNetHandler.Connection con) {
    return Vec3.createVectorHelper(0.5, 0.5, 0.5);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public AxisAlignedBB getRenderBoundingBox() {
    int inc = getRenderRadiusIncrease();
    return AxisAlignedBB.getBoundingBox(xCoord - inc, yCoord - inc, zCoord - inc, xCoord + inc + 1, yCoord + inc + 1, zCoord + inc + 1);
  }

  public abstract int getRenderRadiusIncrease();
}
