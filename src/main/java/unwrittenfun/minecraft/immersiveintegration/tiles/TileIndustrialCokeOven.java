package unwrittenfun.minecraft.immersiveintegration.tiles;

import blusunrize.immersiveengineering.api.CokeOvenRecipe;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;
import net.minecraftforge.oredict.OreDictionary;
import unwrittenfun.minecraft.immersiveintegration.ImmersiveIntegration;
import unwrittenfun.minecraft.immersiveintegration.client.gui.GuiIndustrialCokeOven;
import unwrittenfun.minecraft.immersiveintegration.gui.IGuiProvider;
import unwrittenfun.minecraft.immersiveintegration.gui.containers.ContainerIndustrialCokeOven;
import unwrittenfun.minecraft.immersiveintegration.utils.TileUtils;

public class TileIndustrialCokeOven extends TileEntity implements IMultiblockTile, IGuiProvider, IInventory, IFluidHandler {
  public int[] offset;
  public ItemStack replaced;
  public boolean formed;
  public ItemStack[] items = new ItemStack[10];
  public FluidTank tank = new FluidTank(64000);

  public int[] processTime = new int[4];
  public int[] processTimeMax = new int[4];
  public int clientFluidAmount = 0;
  public int[] clientProgress = new int[4];
  private float speedBoost = 0.5f;

  @Override
  public void updateEntity() {
    if (hasWorldObj() && !worldObj.isRemote && isFormed() && isMaster()) {
      for (int i = 0; i < 4; i++) {
        int slot = i * 2 + 1;
        if (processTime[i] > 0) {
          processTime[i]++;
          //          ImmersiveIntegration.log.info("Process Time (" + i + "): " + processTime[i] + " / " + processTimeMax[i]);
          if (processTime[i] > processTimeMax[i]) {
            CokeOvenRecipe recipe = CokeOvenRecipe.findRecipe(getStackInSlot(slot));
            if (recipe != null) {
              if (TileUtils.addStack(this, recipe.output.copy(), i * 2, true) == 0) {
                TileUtils.addStack(this, recipe.output.copy(), i * 2, false);
                decrStackSize(slot, 1);
                this.tank.fill(new FluidStack(IEContent.fluidCreosote, (int) (recipe.creosoteOutput * 1.5)), true);
                //                ImmersiveIntegration.log.info(this.tank.getFluidAmount());
                //                markDirty();
              }
            }
            processTime[i] = 0;
            processTimeMax[i] = 0;
          }
        } else {
          CokeOvenRecipe recipe = CokeOvenRecipe.findRecipe(getStackInSlot(slot));
          if (recipe != null) {
            processTime[i] = 1;
            processTimeMax[i] = (int) (recipe.time * speedBoost);
          }
        }
      }

      if (tank.getFluidAmount() > 0 && tank.getFluid() != null && (getStackInSlot(9) == null || getStackInSlot(9).stackSize < getStackInSlot(9).getMaxStackSize())) {
        ItemStack filledContainer = Utils.fillFluidContainer(tank, getStackInSlot(8), getStackInSlot(9));
        if (filledContainer != null) {
          if (getStackInSlot(9) != null && OreDictionary.itemMatches(getStackInSlot(9), filledContainer, true))
            getStackInSlot(9).stackSize += filledContainer.stackSize;
          else if (getStackInSlot(9) == null)
            setInventorySlotContents(9, filledContainer.copy());
          this.decrStackSize(8, filledContainer.stackSize);
        }
      }
    }
  }

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
  public boolean isFormed() {
    return formed;
  }

  @Override
  public void setFormed(boolean formed) {
    if (this.formed && !formed) {
      TileUtils.dropInventory(worldObj, xCoord, yCoord, zCoord);
    }
    this.formed = formed;
  }

  @Override
  public void writeToNBT(NBTTagCompound compound) {
    super.writeToNBT(compound);
    writeCustomNBT(compound);

    TileUtils.writeInventoryToNBT(compound, this);
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    readCustomNBT(compound);

    TileUtils.readInventoryFromNBT(compound, this);
  }

  public void writeCustomNBT(NBTTagCompound compound) {
    compound.setIntArray("offset", offset);
    compound.setBoolean("formed", formed);

    compound.setTag("tank", tank.writeToNBT(new NBTTagCompound()));

    compound.setIntArray("processTime", processTime);
    compound.setIntArray("processTimeMax", processTimeMax);

    if (replaced != null) {
      NBTTagCompound stackCompound = new NBTTagCompound();
      replaced.writeToNBT(stackCompound);
      compound.setTag("replaced", stackCompound);
    }
  }

  public void readCustomNBT(NBTTagCompound compound) {
    offset = compound.getIntArray("offset");
    formed = compound.getBoolean("formed");

    tank.readFromNBT(compound.getCompoundTag("tank"));

    processTime = compound.getIntArray("processTime");
    if (processTime.length != 4) processTime = new int[4];
    processTimeMax = compound.getIntArray("processTimeMax");
    if (processTimeMax.length != 4) processTimeMax = new int[4];

    if (compound.hasKey("replaced")) {
      NBTTagCompound stackCompound = compound.getCompoundTag("replaced");
      replaced = ItemStack.loadItemStackFromNBT(stackCompound);
    }
  }

  @Override
  public Packet getDescriptionPacket() {
    NBTTagCompound compound = new NBTTagCompound();
    writeCustomNBT(compound);
    return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 3, compound);
  }

  @Override
  public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
    readCustomNBT(packet.func_148857_g());
  }

  @Override
  public Object getContainer(int id, EntityPlayer player) {
    return new ContainerIndustrialCokeOven(this, player.inventory);
  }

  @Override
  public Object getGuiContainer(int id, EntityPlayer player) {
    return new GuiIndustrialCokeOven((ContainerIndustrialCokeOven) getContainer(id, player));
  }

  public boolean isMaster() {
    return offset[0] == 0 && offset[1] == 0 && offset[2] == 0;
  }

  public TileIndustrialCokeOven getMaster() {
    if (isMaster()) {
      return null;
    }
    TileEntity master = worldObj.getTileEntity(xCoord - offset[0], yCoord - offset[1], zCoord - offset[2]);
    return master instanceof TileIndustrialCokeOven ? (TileIndustrialCokeOven) master : null;
  }


  /// IInventory

  @Override
  public int getSizeInventory() {
    return items.length;
  }

  @Override
  public ItemStack getStackInSlot(int slot) {
    return slot < getSizeInventory() ? items[slot] : null;
  }

  @Override
  public ItemStack decrStackSize(int slot, int amount) {
    ItemStack stack = getStackInSlot(slot);
    if (stack != null) {
      if (stack.stackSize <= amount) {
        setInventorySlotContents(slot, null);
      } else {
        stack = stack.splitStack(amount);
        markDirty();
      }
    }
    return stack;
  }

  @Override
  public ItemStack getStackInSlotOnClosing(int slot) {
    ItemStack stack = getStackInSlot(slot);
    setInventorySlotContents(slot, null);
    return stack;
  }

  @Override
  public void setInventorySlotContents(int slot, ItemStack stack) {
    items[slot] = stack;
    markDirty();
  }

  @Override
  public String getInventoryName() {
    return "Industrial Coke Oven";
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
    return true;
  }

  @Override
  public void openInventory() {

  }

  @Override
  public void closeInventory() {

  }

  @Override
  public boolean isItemValidForSlot(int slot, ItemStack stack) {
    if (slot < 8) {
      if (slot % 2 == 1) { // Bottom row
        ImmersiveIntegration.log.info(slot);
        return CokeOvenRecipe.findRecipe(stack) != null;
      }
    } else if (slot == 8) {
      return FluidContainerRegistry.isEmptyContainer(stack);
    } else if (slot == 9) {
      return FluidContainerRegistry.isFilledContainer(stack) && FluidContainerRegistry.getFluidForFilledItem(stack).containsFluid(new FluidStack(IEContent.fluidCreosote, 1));
    }
    return false;
  }


  /// IFluidHandler

  @Override
  public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
    return 0;
  }

  @Override
  public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
    if (isFormed()) {
      TileIndustrialCokeOven master = getMaster();
      if (master != null) {
        return getMaster().drain(from, resource, doDrain);
      }

      return drain(from, resource.amount, doDrain);
    }

    return null;
  }

  @Override
  public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
    if (isFormed()) {
      TileIndustrialCokeOven master = getMaster();
      if (master != null) {
        return getMaster().drain(from, maxDrain, doDrain);
      }

      FluidStack fluidStack = tank.drain(maxDrain, doDrain);
      markDirty();
      worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

      return fluidStack;
    }

    return null;
  }

  @Override
  public boolean canFill(ForgeDirection from, Fluid fluid) {
    return false;
  }

  @Override
  public boolean canDrain(ForgeDirection from, Fluid fluid) {
    return isFormed(); // TODO: Only allow extraction from certain block possibly (tbd)
  }

  @Override
  public FluidTankInfo[] getTankInfo(ForgeDirection from) {
    if (isFormed()) {
      TileIndustrialCokeOven master = getMaster();
      if (master != null) {
        return getMaster().getTankInfo(from);
      }

      return new FluidTankInfo[] { tank.getInfo() };
    }

    return new FluidTankInfo[0];
  }

  public Fluid getFluid() {
    if (tank.getFluid() != null) {
      return tank.getFluid().getFluid();
    }

    return null;
  }

  public int getProgressFor(int i) {
    if (processTimeMax[i] == 0) return 12;
    return (int) (((float) processTime[i] / (float) processTimeMax[i]) * 12);
  }

  @Override
  public void markDirty() {
    super.markDirty();

    for (int i = 0; i < 4; i++) {
      int slot = i * 2 + 1;
      if (processTime[i] > 0) {
        CokeOvenRecipe recipe = CokeOvenRecipe.findRecipe(getStackInSlot(slot));
        if (recipe != null) {
          //          processTime[i] = 1;
          processTimeMax[i] = (int) (recipe.time * speedBoost);
        } else {
          processTime[i] = 0;
          processTimeMax[i] = 0;
        }
      }
    }
  }
}
