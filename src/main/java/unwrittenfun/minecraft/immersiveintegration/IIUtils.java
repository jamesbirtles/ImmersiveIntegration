package unwrittenfun.minecraft.immersiveintegration;

import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class IIUtils {
  public static Block getBlockFromSide(IBlockAccess world, int x, int y, int z, int side) {
    return IIUtils.getBlockFromDirection(world, x, y, z, ForgeDirection.getOrientation(side));
  }

  public static Block getBlockFromDirection(IBlockAccess world, int x, int y, int z, ForgeDirection direction) {
    return world.getBlock(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ);
  }

  public static void setBlockAtSide(World world, int x, int y, int z, int side, Block block, int meta) {
    IIUtils.setBlockAtDirection(world, x, y, z, ForgeDirection.getOrientation(side), block, meta);
  }

  public static void setBlockAtDirection(World world, int x, int y, int z, ForgeDirection direction, Block block, int meta) {
    world.setBlock(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ, block, meta, 3);
  }

  public static boolean isAirFromSide(IBlockAccess world, int x, int y, int z, int side) {
    return IIUtils.isAirInDirection(world, x, y, z, ForgeDirection.getOrientation(side));
  }

  public static boolean isAirInDirection(IBlockAccess world, int x, int y, int z, ForgeDirection direction) {
    return world.isAirBlock(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ);
  }
}
