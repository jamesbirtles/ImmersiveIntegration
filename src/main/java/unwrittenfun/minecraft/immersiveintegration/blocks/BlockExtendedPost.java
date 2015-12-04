package unwrittenfun.minecraft.immersiveintegration.blocks;

import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import unwrittenfun.minecraft.immersiveintegration.ImmersiveIntegration;
import unwrittenfun.minecraft.immersiveintegration.client.IIRenderIDs;
import unwrittenfun.minecraft.immersiveintegration.client.renderers.block.BlockRenderExtendedPost;
import unwrittenfun.minecraft.immersiveintegration.tiles.TileExtendedPost;

import java.util.Random;

public class BlockExtendedPost extends BlockContainer {
  public BlockExtendedPost(String key) {
    super(Material.wood);
    setBlockTextureName(key);
    setBlockName(key);
//    setCreativeTab(ImmersiveIntegration.iiCreativeTab);
    setBlockBounds(0.3125f, 0f, 0.3125f, 0.6875f, 1f, 0.6875f);
    setHardness(2f);
  }

  @Override
  public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
    return new TileExtendedPost();
  }

  @Override
  public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
    return false;
  }

  @Override
  public boolean renderAsNormalBlock() {
    return false;
  }

  @Override
  public boolean isOpaqueCube() {
    return false;
  }

  @Override
  public int getRenderType() {
    return IIRenderIDs.EXTENDED_POST;
  }

  @Override
  public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
    int meta = world.getBlockMetadata(x, y, z);
    switch (meta) {
      case 0:
        setBlockBounds(0.3125f, 0f, 0.3125f, 0.6875f, 1f, 0.6875f);
        break;
      case 1:
        setBlockBounds(world.getBlockMetadata(x - 1, y, z) == 4 ? 0 : .3125f, 0, world.getBlockMetadata(x, y, z - 1) == 2 ? 0 : .3125f, world.getBlockMetadata(x + 1, y, z) == 5 ? 1 : .6875f, 1f, world.getBlockMetadata(x, y, z + 1) == 3 ? 1 : .6875f);
        break;
      case 2:
      case 3:
      case 4:
      case 5:
        float fd = .5f;
        float fu = 1f;
        if (canArmConnectToBlock(world, x, y - 1, z, true)) {
          fd = 0f;
          fu = 0.5f;

          if (canArmConnectToBlock(world, x, y + 1, z, false)) fu = 1f;
        }
        this.setBlockBounds(meta == 5 ? 0 : .3125f, fd, meta == 3 ? 0 : .3125f, meta == 4 ? 1 : .6875f, fu, meta == 2 ? 1 : .6875f);
        break;
    }
  }

  protected boolean canArmConnectToBlock(IBlockAccess world, int x, int y, int z, boolean down) {
    if (world.isAirBlock(x, y, z))
      return false;
    world.getBlock(x, y, z).setBlockBoundsBasedOnState(world, x, y, z);
    return down ? world.getBlock(x, y, z).getBlockBoundsMaxY() >= 1 : world.getBlock(x, y, z).getBlockBoundsMinY() <= 0;
  }

  @Override
  public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
    setBlockBoundsBasedOnState(world, x, y, z);
    return super.getCollisionBoundingBoxFromPool(world, x, y, z);
  }

  @Override
  public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
    setBlockBoundsBasedOnState(world, x, y, z);
    return super.getSelectedBoundingBoxFromPool(world, x, y, z);
  }

  @Override
  public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
    switch (meta) {
      case 0:
        if (world.getTileEntity(x, y + 1, z) instanceof TileExtendedPost) {
          world.setBlockToAir(x, y + 1, z);
        }
        break;
      case 1:
        if (world.getTileEntity(x, y - 1, z) instanceof TileExtendedPost) {
          world.setBlockToAir(x, y - 1, z);
        }
        for (ForgeDirection direction : new ForgeDirection[] { ForgeDirection.NORTH, ForgeDirection.EAST, ForgeDirection.SOUTH, ForgeDirection.WEST }) {
          if (world.getBlock(x + direction.offsetX, y, z + direction.offsetZ) == IIBlocks.extendedPost && world.getBlockMetadata(x + direction.offsetX, y, z + direction.offsetZ) >= 2) {
            world.setBlockToAir(x + direction.offsetX, y, z + direction.offsetZ);
          }
        }
        break;
    }

    if (meta == 0 && !world.isRemote && world.getGameRules().getGameRuleBooleanValue("doTileDrops") && !world.restoringBlockSnapshots) {
      world.spawnEntityInWorld(new EntityItem(world, x + .5, y + .5, z + .5, new ItemStack(this, 1, 0)));
    }

    super.breakBlock(world, x, y, z, block, meta);
  }

  @Override
  public int quantityDropped(Random p_149745_1_) {
    return 0;
  }

  @Override
  public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
    if (!Utils.isHammer(player.getCurrentEquippedItem())) return false;

    int type = world.getBlockMetadata(x, y, z);
    if (type == 1) {
      ForgeDirection fd = ForgeDirection.getOrientation(side);
      ForgeDirection rot0 = fd.getRotation(ForgeDirection.UP);
      ForgeDirection rot1 = rot0.getOpposite();
      if (!world.isAirBlock(x + fd.offsetX, y + fd.offsetY, z + fd.offsetZ)) return false;
      if (world.getTileEntity(x + rot0.offsetX, y + rot0.offsetY, z + rot0.offsetZ) instanceof TileExtendedPost && world.getBlockMetadata(x + rot0.offsetX, y + rot0.offsetY, z + rot0.offsetZ) == rot0.ordinal())
        return false;
      if (world.getTileEntity(x + rot1.offsetX, y + rot1.offsetY, z + rot1.offsetZ) instanceof TileExtendedPost && world.getBlockMetadata(x + rot1.offsetX, y + rot1.offsetY, z + rot1.offsetZ) == rot1.ordinal())
        return false;
      world.setBlock(x + fd.offsetX, y, z + fd.offsetZ, this, 0, 0x3);
      if (world.getTileEntity(x + fd.offsetX, y, z + fd.offsetZ) instanceof TileExtendedPost)
        world.setBlockMetadataWithNotify(x + fd.offsetX, y, z + fd.offsetZ, side, 3);
    } else if (type == 2 || type == 3 || type == 4 || type == 5) {
      world.setBlockToAir(x, y, z);
    }

    return true;
  }

  @Override
  public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
    if (world.getBlockMetadata(x, y, z) == 0) {
      if (!(world.getBlock(x, y - 1, z) == IEContent.blockWoodenDevice && world.getBlockMetadata(x, y - 1, z) == 0) && world.getBlock(x, y - 1, z) != IIBlocks.extendedPost) {
        world.setBlockToAir(x, y, z);
      }
    }
  }

  @Override
  public boolean isLadder(IBlockAccess world, int x, int y, int z, EntityLivingBase entity) {
    return true;
  }
}
