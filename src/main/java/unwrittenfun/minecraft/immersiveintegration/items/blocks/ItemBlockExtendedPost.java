package unwrittenfun.minecraft.immersiveintegration.items.blocks;

import blusunrize.immersiveengineering.common.IEContent;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import unwrittenfun.minecraft.immersiveintegration.blocks.IIBlocks;

import java.util.List;

public class ItemBlockExtendedPost extends ItemBlock {
  public ItemBlockExtendedPost(Block block) {
    super(block);
  }

  @Override
  public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
    if (!player.canPlayerEdit(x, y + 1, z, side, stack) || !world.getBlock(x, y + 1, z).isReplaceable(world, x, y, z)) {
      return false;
    }

    if (!(world.getBlock(x, y - 1, z) == IEContent.blockWoodenDevice && world.getBlockMetadata(x, y - 1, z) == 0) && world.getBlock(x, y - 1, z) != IIBlocks.extendedPost) {
      return false;
    }

    world.setBlock(x, y + 1, z, this.field_150939_a, 1, 3);

    return super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List lines, boolean p_77624_4_) {
    super.addInformation(stack, player, lines, p_77624_4_);

    lines.add(EnumChatFormatting.RED + "This will be removed soon.");
    lines.add(EnumChatFormatting.GREEN + "Place in crafting table to reclaim fence posts.");
  }
}
