package unwrittenfun.minecraft.immersiveintegration.items.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import unwrittenfun.minecraft.immersiveintegration.blocks.BlockInductionCharger;

public class ItemBlockInductionCharger extends ItemBlock {
  public ItemBlockInductionCharger(Block block) {
    super(block);
    setHasSubtypes(true);
  }

  @Override
  public String getUnlocalizedName(ItemStack stack) {
    return getUnlocalizedName() + BlockInductionCharger.KEYS[stack.getItemDamage()];
  }

  @Override
  public int getMetadata(int meta) {
    return meta;
  }
}
