package unwrittenfun.minecraft.immersiveintegration.tiles;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public class TileIndustrialCokeOven extends TileEntity implements IMultiblockTile {
  public int[] offset;
  public ItemStack replaced;
  public boolean formed;

  @Override
  public int[] getOffset() {
    return offset;
  }

  @Override
  public void setOffset(int[] offset) {
    this.offset = offset;
  }

  @Override
  public ItemStack getReplaced() {
    return replaced;
  }

  @Override
  public void setReplaced(ItemStack stack) {
    this.replaced = stack;
  }

  @Override
  public AxisAlignedBB getRenderBoundingBox() {
    if (getBlockMetadata() == 1 || getBlockMetadata() == 2) {
      return AxisAlignedBB.getBoundingBox(xCoord - 10, yCoord - 10, zCoord - 10, xCoord + 10, yCoord + 10, zCoord + 10);
    } else {
      return super.getRenderBoundingBox();
    }
  }

  @Override
  public void setFormed(boolean formed) {
    this.formed = formed;
  }

  @Override
  public boolean isFormed() {
    return formed;
  }

  @Override
  public void writeToNBT(NBTTagCompound compound) {
    super.writeToNBT(compound);
    compound.setIntArray("offset", offset);
    compound.setBoolean("formed", formed);
    if (replaced != null) {
      NBTTagCompound stackCompound = new NBTTagCompound();
      replaced.writeToNBT(stackCompound);
      compound.setTag("replaced", stackCompound);
    }
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    offset = compound.getIntArray("offset");
    formed = compound.getBoolean("formed");
    if (compound.hasKey("replaced")) {
      NBTTagCompound stackCompound = compound.getCompoundTag("replaced");
      replaced = ItemStack.loadItemStackFromNBT(stackCompound);
    }
  }
}
