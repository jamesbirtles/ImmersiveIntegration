package unwrittenfun.minecraft.immersiveintegration.items;

import blusunrize.immersiveengineering.common.IEContent;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import unwrittenfun.minecraft.immersiveintegration.blocks.IIBlocks;

public class ItemBlockExtendedPost extends ItemBlock {
  public ItemBlockExtendedPost(Block block) {
    super(block);
  }

  @Override
  public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
    if (!player.canPlayerEdit(x, y + 1, z, side, stack) || !world.getBlock(x, y + 1, z).isReplaceable(world, x, y, z)){
      return false;
    }

    if (!(world.getBlock(x, y - 1, z) == IEContent.blockWoodenDevice && world.getBlockMetadata(x, y-1, z) == 0) && world.getBlock(x, y - 1, z) != IIBlocks.extendedPost){
      return false;
    }

    world.setBlock(x, y + 1, z, this.field_150939_a, 1, 3);

    return super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
  }
}
