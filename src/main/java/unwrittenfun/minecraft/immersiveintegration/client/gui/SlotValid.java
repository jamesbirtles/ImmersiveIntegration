package unwrittenfun.minecraft.immersiveintegration.client.gui;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotValid extends Slot {
  public SlotValid(IInventory inventory, int slotNumber, int p_i1824_3_, int p_i1824_4_) {
    super(inventory, slotNumber, p_i1824_3_, p_i1824_4_);
  }

  @Override
  public boolean isItemValid(ItemStack stack) {
    return inventory.isItemValidForSlot(getSlotIndex(), stack);
  }
}
