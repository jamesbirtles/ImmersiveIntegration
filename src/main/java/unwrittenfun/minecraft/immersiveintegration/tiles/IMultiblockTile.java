package unwrittenfun.minecraft.immersiveintegration.tiles;

import net.minecraft.item.ItemStack;

public interface IMultiblockTile {
  void setOffset(int[] offset);

  int[] getOffset();

  void setReplaced(ItemStack stack);

  ItemStack getReplaced();

  void setFormed(boolean formed);

  boolean isFormed();
}
