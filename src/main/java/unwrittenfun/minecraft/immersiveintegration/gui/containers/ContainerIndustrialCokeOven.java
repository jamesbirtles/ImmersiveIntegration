package unwrittenfun.minecraft.immersiveintegration.gui.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import unwrittenfun.minecraft.immersiveintegration.client.gui.SlotValid;
import unwrittenfun.minecraft.immersiveintegration.tiles.TileIndustrialCokeOven;

public class ContainerIndustrialCokeOven extends Container {
  public TileIndustrialCokeOven cokeOven;
  public InventoryPlayer inventoryPlayer;
  public int prevFluidAmount = 0;
  public int[] prevProgressValues = new int[4];

  public ContainerIndustrialCokeOven(TileIndustrialCokeOven cokeOven, InventoryPlayer inventoryPlayer) {
    this.cokeOven = cokeOven;
    this.inventoryPlayer = inventoryPlayer;

    for (int i = 0; i < 4; i++) {
      addSlotToContainer(new SlotValid(cokeOven, i * 2, 14 + 28 * i, 13));
      addSlotToContainer(new SlotValid(cokeOven, i * 2 + 1, 14 + 28 * i, 55));
    }

    addSlotToContainer(new SlotValid(cokeOven, 8, 146, 17));
    addSlotToContainer(new SlotValid(cokeOven, 9, 146, 53));

    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 9; j++) {
        addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 85 + i * 18));
      }
    }

    for (int i = 0; i < 9; i++) {
      addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 143));
    }
  }

  @Override
  public boolean canInteractWith(EntityPlayer player) {
    return true;
  }

  @Override
  public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
    return null;
  }

  @Override
  public void addCraftingToCrafters(ICrafting crafter) {
    super.addCraftingToCrafters(crafter);

    crafter.sendProgressBarUpdate(this, 4, cokeOven.tank.getFluidAmount());

    for (int i = 0; i < 4; i++) {
      crafter.sendProgressBarUpdate(this, i, cokeOven.getProgressFor(i));
    }
  }

  @Override
  public void detectAndSendChanges() {
    super.detectAndSendChanges();
    if (prevFluidAmount != cokeOven.tank.getFluidAmount()) {
      prevFluidAmount = cokeOven.tank.getFluidAmount();
      for (Object crafter : crafters) {
        ((ICrafting) crafter).sendProgressBarUpdate(this, 4, prevFluidAmount);
      }
    }

    for (int i = 0; i < 4; i++) {
      if (prevProgressValues[i] != cokeOven.getProgressFor(i)) {
        prevProgressValues[i] = cokeOven.getProgressFor(i);
        for (Object crafter : crafters) {
          ((ICrafting) crafter).sendProgressBarUpdate(this, i, prevProgressValues[i]);
        }
      }
    }
  }

  @Override
  public void updateProgressBar(int id, int value) {
    switch (id) {
      case 0:
      case 1:
      case 2:
      case 3:
        cokeOven.clientProgress[id] = value;
        break;
      case 4:
        cokeOven.clientFluidAmount = value;
        break;
    }
  }
}
