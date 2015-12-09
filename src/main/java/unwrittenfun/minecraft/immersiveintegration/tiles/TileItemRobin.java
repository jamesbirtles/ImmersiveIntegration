package unwrittenfun.minecraft.immersiveintegration.tiles;

import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockOverlayText;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import unwrittenfun.minecraft.immersiveintegration.utils.MathUtils;
import unwrittenfun.minecraft.immersiveintegration.utils.SidedItemStack;
import unwrittenfun.minecraft.immersiveintegration.utils.TileUtils;

import java.util.ArrayList;

public class TileItemRobin extends TileEntity implements ISidedInventory, IBlockOverlayText {
  public int currentSide = 0;
  public ArrayList<SidedItemStack> itemBuffer = new ArrayList<>();
  protected int[] sideCount = new int[6];
  protected boolean locked;
  private int cachedSideCount = 0;

  @Override
  public void updateEntity() {
    if (hasWorldObj() && !worldObj.isRemote) {
      if (itemBuffer.size() > 0) {
        SidedItemStack sidedStack = itemBuffer.get(0);
        if (currentSide != sidedStack.getSide().ordinal() && cachedSideCount > 0) {
          ForgeDirection currentDir = ForgeDirection.getOrientation(currentSide);
          TileEntity tileEntity = worldObj.getTileEntity(xCoord + currentDir.offsetX, yCoord + currentDir.offsetY, zCoord + currentDir.offsetZ);
          ItemStack smallStack = sidedStack.getStack().copy();
          smallStack.stackSize = Math.min(sidedStack.getStack().stackSize, cachedSideCount);
          if (tileEntity instanceof IInventory && Utils.canInsertStackIntoInventory((IInventory) tileEntity, smallStack, ForgeDirection.OPPOSITES[currentSide])) {
            int prevSize = smallStack.stackSize;
            smallStack = Utils.insertStackIntoInventory((IInventory) tileEntity, smallStack, ForgeDirection.OPPOSITES[currentSide]);
            if (smallStack == null) {
              cachedSideCount -= prevSize;
              sidedStack.getStack().stackSize -= prevSize;
            } else {
              cachedSideCount -= prevSize - smallStack.stackSize;
              sidedStack.getStack().stackSize -= prevSize - smallStack.stackSize;
            }
            if (sidedStack.getStack().stackSize == 0) {
              itemBuffer.remove(sidedStack);
            }
          } else {
            cachedSideCount = 0;
          }
        }
        if (cachedSideCount == 0) {
          currentSide = MathUtils.increment(currentSide, 5);
          cachedSideCount = sideCount[currentSide];
        }
      }
    }
  }

  @Override
  public void invalidate() {
    super.invalidate();
    if (!worldObj.isRemote) {
      for (SidedItemStack sidedStack : itemBuffer) {
        TileUtils.dropItemStack(sidedStack.getStack(), worldObj, xCoord, yCoord + 1, zCoord);
      }
    }
  }

  @Override
  public void writeToNBT(NBTTagCompound compound) {
    super.writeToNBT(compound);
    compound.setIntArray("sideCount", sideCount);
    compound.setInteger("currentSide", currentSide);
    compound.setInteger("cachedSideCount", cachedSideCount);
    compound.setBoolean("locked", locked);

    NBTTagList itemList = new NBTTagList();
    for (SidedItemStack sidedStack : itemBuffer) {
      NBTTagCompound stackCompound = new NBTTagCompound();
      sidedStack.writeToNBT(stackCompound);
      itemList.appendTag(stackCompound);
    }
    compound.setTag("items", itemList);
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);

    sideCount = compound.getIntArray("sideCount");
    currentSide = compound.getInteger("currentSide");
    cachedSideCount = compound.getInteger("cachedSideCount");
    locked = compound.getBoolean("locked");

    NBTTagList itemList = compound.getTagList("items", 10);
    for (int i = 0; i < itemList.tagCount(); i++) {
      NBTTagCompound stackCompound = itemList.getCompoundTagAt(i);
      itemBuffer.add(SidedItemStack.loadFromNBT(stackCompound));
    }
  }

  @Override
  public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
    super.onDataPacket(net, pkt);
    sideCount = pkt.func_148857_g().getIntArray("sideCount");
    locked = pkt.func_148857_g().getBoolean("locked");
  }

  @Override
  public Packet getDescriptionPacket() {
    NBTTagCompound compound = new NBTTagCompound();
    compound.setIntArray("sideCount", sideCount);
    compound.setBoolean("locked", locked);
    return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 3, compound);
  }

  // ISidedInventory

  public void setSideCount(int side, int count) {
    sideCount[side] = count;
    if (currentSide == side) cachedSideCount = count;
  }

  @Override
  public int[] getAccessibleSlotsFromSide(int side) {
    return new int[] { side };
  }

  @Override
  public boolean canInsertItem(int slot, ItemStack stack, int side) {
    return side == slot;
  }

  @Override
  public boolean canExtractItem(int slot, ItemStack stack, int side) {
    return false;
  }

  // IInventory

  @Override
  public int getSizeInventory() {
    return 1;
  }

  @Override
  public ItemStack getStackInSlot(int stack) {
    return null;
  }

  @Override
  public ItemStack decrStackSize(int slot, int amount) {
    return null;
  }

  @Override
  public ItemStack getStackInSlotOnClosing(int stack) {
    return null;
  }

  @Override
  public void setInventorySlotContents(int slot, ItemStack stack) {
    itemBuffer.add(new SidedItemStack(stack, slot));
  }

  @Override
  public String getInventoryName() {
    return "Item Robin";
  }

  @Override
  public boolean hasCustomInventoryName() {
    return false;
  }

  @Override
  public int getInventoryStackLimit() {
    return 64;
  }

  @Override
  public boolean isUseableByPlayer(EntityPlayer player) {
    return false;
  }

  @Override
  public void openInventory() {

  }

  @Override
  public void closeInventory() {

  }

  @Override
  public boolean isItemValidForSlot(int slot, ItemStack stack) {
    return true;
  }


  /// IBlockOverlayText

  @Override
  public String[] getOverlayText(EntityPlayer player, MovingObjectPosition mop, boolean hammer) {
    if (!hammer) return null;
    return new String[] {
        StatCollector.translateToLocal("desc.ImmersiveEngineering.info.blockSide." + ForgeDirection.getOrientation(mop.sideHit)),
        StatCollector.translateToLocalFormatted("immersiveintegration.chat.itemRobin.itemCount", sideCount[mop.sideHit]),
        locked ? StatCollector.translateToLocal("immersiveintegration.chat.itemRobin.locked") : ""
    };
  }

  public void toggleLocked() {
    this.locked = !locked;
  }

  public boolean isLocked() {
    return locked;
  }

  @Override
  public boolean useNixieFont(EntityPlayer player, MovingObjectPosition mop) {
    return false;
  }
}
