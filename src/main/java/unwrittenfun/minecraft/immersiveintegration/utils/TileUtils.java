package unwrittenfun.minecraft.immersiveintegration.utils;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class TileUtils {
  public static <T extends TileEntity> T getTileEntity(IBlockAccess world, int x, int y, int z, Class<T> tileClass) {
    TileEntity tileEntity = world.getTileEntity(x, y, z);
    if (tileClass.isInstance(tileEntity)) {
      return tileClass.cast(tileEntity);
    }
    return null;
  }

  public static void writeInventoryToNBT(NBTTagCompound compound, IInventory inventory) {
    NBTTagList items = new NBTTagList();
    for (byte slot = 0; slot < inventory.getSizeInventory(); slot++) {
      ItemStack stack = inventory.getStackInSlot(slot);
      if (stack != null) {
        NBTTagCompound item = new NBTTagCompound();
        item.setByte("Slot", slot);
        stack.writeToNBT(item);
        items.appendTag(item);
      }
    }
    compound.setTag("InventoryItems", items);
  }

  public static void readInventoryFromNBT(NBTTagCompound compound, IInventory inventory) {
    NBTTagList items = compound.getTagList("InventoryItems", 10);
    for (int i = 0; i < items.tagCount(); i++) {
      NBTTagCompound item = items.getCompoundTagAt(i);
      int slot = item.getByte("Slot");
      inventory.setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(item));
    }
  }

  public static void dropInventory(World world, int x, int y, int z) {
    TileEntity tileEntity = world.getTileEntity(x, y, z);
    if (tileEntity instanceof IInventory) {
      IInventory inventory = (IInventory) tileEntity;
      for (int i = 0; i < inventory.getSizeInventory(); i++) {
        ItemStack stack = inventory.getStackInSlotOnClosing(i);
        if (stack != null) {
          dropItemStack(stack, world, x, y, z);
        }
      }
    }
  }

  public static void dropItemStack(ItemStack stack, World world, int x, int y, int z) {
    float spawnX = x + world.rand.nextFloat();
    float spawnY = y + world.rand.nextFloat();
    float spawnZ = z + world.rand.nextFloat();
    EntityItem droppedItem = new EntityItem(world, spawnX, spawnY, spawnZ, stack);
    droppedItem.motionX = (-0.5f + world.rand.nextFloat()) * 0.05F;
    droppedItem.motionY = (4f + world.rand.nextFloat()) * 0.05F;
    droppedItem.motionZ = (-0.5f + world.rand.nextFloat()) * 0.05F;
    world.spawnEntityInWorld(droppedItem);
  }

  /**
   * @param simulate Should the items actually be inserted
   * @return Number of items in stack that couldn't fit
   */
  public static int addStack(IInventory inventory, ItemStack stack, int slot, boolean simulate) {
    ItemStack stackInSlot = inventory.getStackInSlot(slot);
    if (stackInSlot == null) {
      if (!simulate) {
        inventory.setInventorySlotContents(slot, stack.copy());
      }
      return 0;
    } else {
      if (stackInSlot.isItemEqual(stack)) {
        if ((stackInSlot.getMaxStackSize() - stackInSlot.stackSize) >= stack.stackSize) {
          if (!simulate) {
            stackInSlot.stackSize += stack.stackSize;
          }
          return 0;
        } else {
          int insert = (stackInSlot.getMaxStackSize() - stackInSlot.stackSize);
          if (!simulate) {
            stackInSlot.stackSize += insert;
            stack.stackSize -= insert;
          }
          return stack.stackSize - insert;
        }
      } else {
        return stack.stackSize;
      }
    }
  }

  public static int addStackToInventory(IInventory inventory, ForgeDirection side, ItemStack stack) {
    stack = stack.copy();
    if (inventory instanceof ISidedInventory) {
      ISidedInventory sidedInventory = (ISidedInventory) inventory;
      int[] slots = sidedInventory.getAccessibleSlotsFromSide(side.ordinal());
      for (int slot : slots) {
        if (sidedInventory.canInsertItem(slot, stack, side.ordinal())) {
          stack.stackSize = addStack(inventory, stack, slot, false);
          if (stack.stackSize <= 0) {
            break;
          }
        }
      }
    } else {
      for (int slot = 0; slot < inventory.getSizeInventory(); slot++) {
        if (inventory.isItemValidForSlot(slot, stack)) {
          stack.stackSize = addStack(inventory, stack, slot, false);
          if (stack.stackSize <= 0) {
            break;
          }
        }
      }
    }

    return stack.stackSize;
  }
}
