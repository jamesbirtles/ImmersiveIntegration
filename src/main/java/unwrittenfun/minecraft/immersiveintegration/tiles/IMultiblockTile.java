package unwrittenfun.minecraft.immersiveintegration.tiles;

import net.minecraft.item.ItemStack;

public interface IMultiblockTile {
  int[] getOffset();

  void setOffset(int[] offset);

  ItemStack getReplaced();

  void setReplaced(ItemStack stack);

  boolean isFormed();

  void setFormed(boolean formed);
}
