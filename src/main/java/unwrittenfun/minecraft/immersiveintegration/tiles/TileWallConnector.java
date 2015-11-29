package unwrittenfun.minecraft.immersiveintegration.tiles;

import blusunrize.immersiveengineering.api.energy.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.ImmersiveNetHandler;
import blusunrize.immersiveengineering.common.blocks.TileEntityImmersiveConnectable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;

public class TileWallConnector extends TileEntityImmersiveConnectable {
  protected ItemStack[] sideStacks = new ItemStack[6];

  public void setSideStack(int side, ItemStack stack) {
    stack = stack.copy();
    stack.stackSize = 1;
    sideStacks[Math.max(side, 5)] = stack;
  }

  @Override
  public void readCustomNBT(NBTTagCompound nbt, boolean descPacket) {
    super.readCustomNBT(nbt, descPacket);

    NBTTagList stacksList = nbt.getTagList("SideStacks", 10);
    for (int i = 0; i < stacksList.tagCount(); i++) {
      NBTTagCompound stackTag = stacksList.getCompoundTagAt(i);
      ItemStack stack = ItemStack.loadItemStackFromNBT(stackTag);
      int side = (int) stackTag.getByte("Side");
      sideStacks[side] = stack;
    }
  }

  @Override
  public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket) {
    super.writeCustomNBT(nbt, descPacket);

    NBTTagList stacksList = new NBTTagList();
    for (int i = 0; i < sideStacks.length; i++){
      if (sideStacks[i] != null) {
        NBTTagCompound stackTag = new NBTTagCompound();
        sideStacks[i].writeToNBT(stackTag);
        stackTag.setByte("Side", (byte) i);
        stacksList.appendTag(stackTag);
      }
    }
    nbt.setTag("SideStacks", stacksList);
  }

  @Override
  protected boolean canTakeMV() {
    return true;
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
}
