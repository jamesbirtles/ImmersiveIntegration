package unwrittenfun.minecraft.immersiveintegration.tiles;

import blusunrize.immersiveengineering.common.Config;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockOverlayText;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.util.ForgeDirection;
import unwrittenfun.minecraft.immersiveintegration.utils.TileUtils;

public class TileInductionCharger extends TileEntity implements IEnergyReceiver, IBlockOverlayText {
  public ItemStack chargingStack;
  public EntityItem chargingStackEntity;
  public EnergyStorage energyStorage = new EnergyStorage(Config.getInt("capacitorLV_storage") / 2, Config.getInt("capacitorLV_input"));

  @Override
  public void updateEntity() {
    if (hasWorldObj() && !worldObj.isRemote) {
      if (chargingStack != null) {
        IEnergyContainerItem energyContainer = (IEnergyContainerItem) chargingStack.getItem();
        int energyToInsert = energyStorage.extractEnergy(256, true);
        int energyExtracted = energyContainer.receiveEnergy(chargingStack, energyToInsert, false);
        energyStorage.extractEnergy(energyExtracted, false);
      }
    }
  }

  @Override
  public void invalidate() {
    super.invalidate();

    if (hasWorldObj() && !worldObj.isRemote && chargingStack != null) TileUtils.dropItemStack(chargingStack, worldObj, xCoord, yCoord + 1, zCoord);
  }

  public void setChargingStack(ItemStack stack) {
    if (stack == null) {
      chargingStack = null;
    } else {
      chargingStack = stack.copy();
      chargingStack.stackSize = 1;
    }
    this.markDirty();
    worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
  }

  @Override
  public void writeToNBT(NBTTagCompound compound) {
    super.writeToNBT(compound);
    writeDataToNBT(compound);
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    readDataFromNBT(compound);
  }

  @Override
  public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
    super.onDataPacket(net, pkt);
    readDataFromNBT(pkt.func_148857_g());
    if (chargingStack == null) {
      chargingStackEntity = null;
    } else {
      chargingStackEntity = new EntityItem(worldObj, xCoord, yCoord, zCoord, chargingStack);
      chargingStackEntity.hoverStart = 0;
    }
  }

  @Override
  public Packet getDescriptionPacket() {
    NBTTagCompound compound = new NBTTagCompound();
    writeDataToNBT(compound);
    return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 3, compound);
  }

  public void writeDataToNBT(NBTTagCompound compound) {
    if (chargingStack != null) {
      NBTTagCompound stackCompound = new NBTTagCompound();
      chargingStack.writeToNBT(stackCompound);
      compound.setTag("ChargingStack", stackCompound);
    }

    energyStorage.writeToNBT(compound);
  }

  /// IEnergyReceiver

  public void readDataFromNBT(NBTTagCompound compound) {
    if (compound.hasKey("ChargingStack")) {
      chargingStack = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("ChargingStack"));
    } else {
      chargingStack = null;
    }

    energyStorage.readFromNBT(compound);
  }

  @Override
  public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
    return energyStorage.receiveEnergy(maxReceive, simulate);
  }

  @Override
  public int getEnergyStored(ForgeDirection from) {
    return energyStorage.getEnergyStored();
  }

  @Override
  public int getMaxEnergyStored(ForgeDirection from) {
    return energyStorage.getMaxEnergyStored();
  }


  /// IEnergyConnection

  @Override
  public boolean canConnectEnergy(ForgeDirection from) {
    return from != ForgeDirection.UP;
  }


  /// IBlockOverlayText
  @Override
  public String[] getOverlayText(EntityPlayer player, MovingObjectPosition mop, boolean hammer) {
    if (chargingStack != null) {
      IEnergyContainerItem energyContainer = (IEnergyContainerItem) chargingStack.getItem();
      return new String[] {
         chargingStack.getDisplayName(),
         energyContainer.getEnergyStored(chargingStack) + "/" + energyContainer.getMaxEnergyStored(chargingStack) + "RF"
      };
    }
    return new String[0];
  }
}
