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
  public int[] prevProgressValues = new int[4];

  public ContainerIndustrialCokeOven(TileIndustrialCokeOven cokeOven, InventoryPlayer inventoryPlayer) {
    this.cokeOven = cokeOven;
    this.inventoryPlayer = inventoryPlayer;

    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 9; j++) {
        addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 85 + i * 18));
      }
    }

    for (int i = 0; i < 9; i++) {
      addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 143));
    }

    for (int i = 0; i < 4; i++) {
      addSlotToContainer(new SlotValid(cokeOven, i * 2, 14 + 28 * i, 13));
      addSlotToContainer(new SlotValid(cokeOven, i * 2 + 1, 14 + 28 * i, 55));
    }

    addSlotToContainer(new SlotValid(cokeOven, 8, 146, 17));
    addSlotToContainer(new SlotValid(cokeOven, 9, 146, 53));
  }

  @Override
  public boolean canInteractWith(EntityPlayer player) {
    return true;
  }

  @Override
  public ItemStack transferStackInSlot(EntityPlayer player, int i) {
    Slot slot = getSlot(i);
    if (slot != null && slot.getHasStack()) {
      ItemStack stack = slot.getStack();
      ItemStack result = stack.copy();
      if (i >= 36) {
        if (!mergeItemStack(stack, 0, 36, false)) return null;
      } else {
        boolean merged = false;
        for (int j = 0; j < cokeOven.getSizeInventory(); j++) {
          if (cokeOven.isItemValidForSlot(j, stack)) {
            if (mergeItemStack(stack, 36 + j, 37 + j, false)) {
              merged = true;
              break;
            }
          }
        }
        if (!merged) return null;
      }
      if (stack.stackSize == 0) slot.putStack(null);
      else slot.onSlotChanged();
      slot.onPickupFromSlot(player, stack);
      return result;
    }
    return null;
  }

  @Override
  public void addCraftingToCrafters(ICrafting crafter) {
    super.addCraftingToCrafters(crafter);

    for (int i = 0; i < 4; i++) {
      crafter.sendProgressBarUpdate(this, i, cokeOven.getProgressFor(i));
    }
  }

  @Override
  public void detectAndSendChanges() {
    super.detectAndSendChanges();

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
    }
  }
}
