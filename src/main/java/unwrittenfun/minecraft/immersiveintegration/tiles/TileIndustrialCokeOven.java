package unwrittenfun.minecraft.immersiveintegration.tiles;

import blusunrize.immersiveengineering.api.crafting.CokeOvenRecipe;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
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

import java.util.Random;

public class TileIndustrialCokeOven extends TileEntity implements IMultiblockTile, IGuiProvider, ISidedInventory, IFluidHandler {
  public int[] offset;
  public ItemStack replaced;
  public boolean formed;
  public ItemStack[] items = new ItemStack[10];
  public FluidTank tank = new FluidTank(64000);

  public int[] processTime = new int[4];
  public int[] processTimeMax = new int[4];
  public int[] clientProgress = new int[4];
  public Random random = new Random();

  @Override
  public void updateEntity() {
    if (hasWorldObj() && !worldObj.isRemote && isFormed()) {
      if (isMaster()) {
        for (int i = 0; i < 4; i++) {
          int slot = i * 2 + 1;
          if (processTime[i] > 0) {
            processTime[i]++;
            if (processTime[i] > processTimeMax[i]) {
              CokeOvenRecipe recipe = CokeOvenRecipe.findRecipe(getStackInSlot(slot));
              if (recipe != null) {
                ItemStack outputStack = recipe.output.copy();
                if (TileUtils.addStack(this, outputStack, i * 2, true) == 0) {
                  if (random.nextInt(ImmersiveIntegration.cfg.cokeOvenDoubleChance) == 0) {
                    outputStack.stackSize += 1;
                  }
                  TileUtils.addStack(this, outputStack, i * 2, false);
                  decrStackSize(slot, 1);
                  this.tank.fill(new FluidStack(IEContent.fluidCreosote, (int) (recipe.creosoteOutput * ImmersiveIntegration.cfg.cokeOvenCreosoteMultiplier)), true);
                  this.markDirty();
                  this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                }
              }
              processTime[i] = 0;
              processTimeMax[i] = 0;
            }
          } else {
            CokeOvenRecipe recipe = CokeOvenRecipe.findRecipe(getStackInSlot(slot));
            if (isValidRecipe(recipe, i)) {
              processTime[i] = 1;
              processTimeMax[i] = (int) (recipe.time * ImmersiveIntegration.cfg.cokeOvenTimeMultiplier);
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
      } else {
        if (isIOSide()) {
          doAutoOutput(getFacing());
          doAutoOutput(getFacing().getOpposite());
        }
      }
    }
  }

  public void doAutoOutput(ForgeDirection side) {
    TileEntity tileEntity = worldObj.getTileEntity(xCoord + side.offsetX, yCoord + side.offsetY, zCoord + side.offsetZ);
    if (tileEntity instanceof IInventory && !(tileEntity instanceof TileIndustrialCokeOven)) {
      IInventory inv = (IInventory) tileEntity;
      for (int i = 0; i < 4; i++) {
        outputStackInSlot(i * 2, inv, side.getOpposite());
      }
      outputStackInSlot(9, inv, side.getOpposite());
    }
  }

  public void outputStackInSlot(int slot, IInventory inv, ForgeDirection side) {
    ItemStack stack = getStackInSlot(slot);
    if (stack != null) {
      int leftover = TileUtils.addStackToInventory(inv, side, stack);
      if (leftover <= 0) {
        setInventorySlotContents(slot, null);
      } else {
        stack.stackSize = leftover;
      }
    }
  }

  public boolean isIOSide() {
    int x = getFacing().offsetX != 0 ? 2 : 0;
    int z = getFacing().offsetX != 0 ? 0 : 2;
    return offset[1] == -1 && (Math.abs(offset[x]) == 3 || Math.abs(offset[x]) == 1) && (Math.abs(offset[z]) == 4 || offset[z] == 0);
  }

  private boolean isValidRecipe(CokeOvenRecipe recipe, int i) {
    return recipe != null && (getStackInSlot(i * 2) == null || (recipe.output.isItemEqual(getStackInSlot(i * 2)) && (getStackInSlot(i * 2).getMaxStackSize() - getStackInSlot(i * 2).stackSize) >= recipe.output.stackSize));
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
    return isFormed() && offset[0] == 0 && offset[1] == 0 && offset[2] == 0;
  }

  public TileIndustrialCokeOven getMaster() {
    if (!isFormed() || isMaster() || !hasWorldObj()) {
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
    TileIndustrialCokeOven master = getMaster();
    if (master != null) return master.getStackInSlot(slot);
    return slot < getSizeInventory() ? items[slot] : null;
  }

  @Override
  public ItemStack decrStackSize(int slot, int amount) {
    TileIndustrialCokeOven master = getMaster();
    if (master != null) return master.decrStackSize(slot, amount);

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
    if (isFormed()) {
      TileIndustrialCokeOven master = getMaster();
      if (master != null) {
        master.setInventorySlotContents(slot, stack);
        return;
      }
      items[slot] = stack;
      markDirty();
    }
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
    TileIndustrialCokeOven master = getMaster();
    if (master != null) { return master.isItemValidForSlot(slot, stack); }

    if (slot < 8) {
      if (slot % 2 == 1) { // Bottom row
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
        if (isValidRecipe(recipe, i)) {
          processTimeMax[i] = (int) (recipe.time * ImmersiveIntegration.cfg.cokeOvenTimeMultiplier);
        } else {
          processTime[i] = 0;
          processTimeMax[i] = 0;
        }
      }
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public int[] getAccessibleSlotsFromSide(int side) {
    if (isFormed()) {
      ForgeDirection sideDir = ForgeDirection.getOrientation(side);
      if (Math.abs(sideDir.offsetX) != Math.abs(getFacing().offsetX) || Math.abs(sideDir.offsetZ) != Math.abs(getFacing().offsetZ))
        return new int[0];
      int mult = getFacing().offsetX != 0 ? getFacing().offsetX : -getFacing().offsetZ;
      int x = getFacing().offsetX != 0 ? 2 : 0;
      int z = getFacing().offsetX != 0 ? 0 : 2;
      if (offset[1] == -1) {
        if ((offset[z] == 0 && offset[x] == -3 * mult) || (Math.abs(offset[z]) == 4 && offset[x] == 3 * mult)) {
          return new int[] { 0, 2, 4, 6, 8, 9, 7 };
        } else if ((offset[z] == 0 && offset[x] == -1 * mult) || (Math.abs(offset[z]) == 4 && offset[x] == mult)) {
          return new int[] { 0, 2, 4, 6, 8, 9, 5 };
        } else if ((offset[z] == 0 && offset[x] == mult) || (Math.abs(offset[z]) == 4 && offset[x] == -1 * mult)) {
          return new int[] { 0, 2, 4, 6, 8, 9, 3 };
        } else if ((offset[z] == 0 && offset[x] == 3 * mult) || (Math.abs(offset[z]) == 4 && offset[x] == -3 * mult)) {
          return new int[] { 0, 2, 4, 6, 8, 9, 1 };
        }
      }
    }
    return new int[0];
  }

  public ForgeDirection getFacing() {
    return ForgeDirection.getOrientation(offset[3]);
  }

  @Override
  public boolean canInsertItem(int slot, ItemStack stack, int side) {
    return isFormed() && slot != 9 && isItemValidForSlot(slot, stack);
  }

  @Override
  public boolean canExtractItem(int slot, ItemStack stack, int side) {
    return isFormed() && ((slot % 2 == 0 && slot < 8) || slot == 9);
  }
}
