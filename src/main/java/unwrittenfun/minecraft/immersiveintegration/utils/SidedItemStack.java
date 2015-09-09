package unwrittenfun.minecraft.immersiveintegration.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class SidedItemStack {
  private ItemStack stack;
  private ForgeDirection side;

  public SidedItemStack(ItemStack stack, ForgeDirection side) {
    this.stack = stack;
    this.side = side;
  }

  public SidedItemStack(ItemStack stack, int side) {
    this(stack, ForgeDirection.getOrientation(side));
  }

  public static SidedItemStack loadFromNBT(NBTTagCompound compound) {
    return new SidedItemStack(ItemStack.loadItemStackFromNBT(compound), compound.getInteger("side"));
  }

  public ItemStack getStack() {
    return stack;
  }

  public ForgeDirection getSide() {
    return side;
  }

  public void writeToNBT(NBTTagCompound compound) {
    compound.setInteger("side", side.ordinal());
    stack.writeToNBT(compound);
  }
}
