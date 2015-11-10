package unwrittenfun.minecraft.immersiveintegration.items.blocks;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import unwrittenfun.minecraft.immersiveintegration.blocks.IIBlocks;

import java.util.List;

public class ItemBlockSteelDecoration extends ItemBlock {
  public ItemBlockSteelDecoration(Block block) {
    super(block);
    setHasSubtypes(true);
  }

  @Override
  public String getUnlocalizedName(ItemStack stack) {
    return getUnlocalizedName() + IIBlocks.STEEL_BLOCKS_KEYS[stack.getItemDamage()];
  }

  @Override
  public int getMetadata(int meta) {
    return meta;
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean p_77624_4_) {
    list.add(StatCollector.translateToLocal("immersiveintegration.tooltip.safe"));
  }
}
