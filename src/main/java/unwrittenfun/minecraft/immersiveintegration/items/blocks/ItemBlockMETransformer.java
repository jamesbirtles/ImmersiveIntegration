package unwrittenfun.minecraft.immersiveintegration.items.blocks;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ItemBlockMETransformer extends ItemBlock {
  public ItemBlockMETransformer(Block block) {
    super(block);
  }

  @Override
  public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
    if (!player.canPlayerEdit(x, y + 1, z, side, stack) || !world.getBlock(x, y + 1, z).isReplaceable(world, x, y, z)) {
      return false;
    }

    int playerViewQuarter = MathHelper.floor_double(player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
    int f = playerViewQuarter == 0 ? 2 : playerViewQuarter == 1 ? 5 : playerViewQuarter == 2 ? 3 : 4;

    world.setBlock(x, y + 1, z, field_150939_a, f | 8, 3);
    return super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
  }
}
