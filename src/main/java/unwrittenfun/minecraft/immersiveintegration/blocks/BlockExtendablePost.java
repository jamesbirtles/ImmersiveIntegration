package unwrittenfun.minecraft.immersiveintegration.blocks;

import blusunrize.immersiveengineering.api.IPostBlock;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import unwrittenfun.minecraft.immersiveintegration.ImmersiveIntegration;
import unwrittenfun.minecraft.immersiveintegration.client.IIRenderIDs;

import java.util.ArrayList;

import static unwrittenfun.minecraft.immersiveintegration.IIUtils.*;

public class BlockExtendablePost extends Block implements IPostBlock {
  public BlockExtendablePost(String key) {
    super(Material.wood);
    setBlockTextureName(key);
    setBlockName(key);
    setCreativeTab(ImmersiveIntegration.iiCreativeTab);
    setStepSound(Block.soundTypeWood);
    setHardness(2f);
  }

  public static boolean canArmConnectToBlock(IBlockAccess world, int x, int y, int z, boolean down) {
    if (world.isAirBlock(x, y, z) || world.getBlock(x, y, z) instanceof IPostBlock)
      return false;
    world.getBlock(x, y, z).setBlockBoundsBasedOnState(world, x, y, z);
    return down ? world.getBlock(x, y, z).getBlockBoundsMaxY() >= 1 : world.getBlock(x, y, z).getBlockBoundsMinY() <= 0;
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
    return IIRenderIDs.EXTENDABLE_POST;
  }

  @Override
  public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
    if (!world.isRemote && player.getHeldItem() != null) {
      if (player.getHeldItem().isItemEqual(new ItemStack(IEContent.blockWoodenDecoration, 1, 1))) {
        for (int i = 0; i < world.getActualHeight() - y; i++) {
          if (world.isAirBlock(x, y + i, z)) {
            world.setBlock(x, y + i, z, IEContent.blockWoodenDecoration, 1, 1);
            if (!player.capabilities.isCreativeMode) {
              player.getHeldItem().stackSize--;
            }
            return true;
          } else if (world.getBlock(x, y + i, z) != this) {
            return true;
          }
        }
      } else if (Utils.isHammer(player.getHeldItem())) {
        int meta = world.getBlockMetadata(x, y, z);
        if (meta == 1 || meta == 2) {
          if (side > 1 && isAirFromSide(world, x, y, z, side)) {
            setBlockAtSide(world, x, y, z, side, this, 1 + side);
          }
        } else if (meta != 0) {
          world.setBlockToAir(x, y, z);
        }
      }
    }
    return Utils.isHammer(player.getHeldItem()) || (player.getHeldItem() != null && player.getHeldItem().isItemEqual(new ItemStack(IEContent.blockWoodenDecoration, 1, 1)));
  }

  @Override
  public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
    Block aboveBlock = world.getBlock(x, y + 1, z);
    int aboveMeta = world.getBlockMetadata(x, y + 1, z);
    int thisMeta = world.getBlockMetadata(x, y, z);

    if (thisMeta == 1 || thisMeta == 2) {
      if (getBlockFromDirection(world, x, y, z, ForgeDirection.DOWN) != this) {
        dropBlockAsItem(world, x, y, z, thisMeta, 1);
        world.setBlockToAir(x, y, z);
        return;
      }
    }

    if (aboveBlock == IEContent.blockWoodenDecoration && aboveMeta == 1) {
      setBlockAtDirection(world, x, y, z, ForgeDirection.UP, this, 2);
      if (thisMeta == 2) {
        world.setBlockMetadataWithNotify(x, y, z, 1, 3);
      }
    } else if (thisMeta == 1 && aboveBlock.isAir(world, x, y + 1, z)) {
      world.setBlockMetadataWithNotify(x, y, z, 2, 3);
    } else if (thisMeta > 2) {
      int side = thisMeta - 1;
      ForgeDirection dir = ForgeDirection.getOrientation(side).getOpposite();
      if (getBlockFromDirection(world, x, y, z, dir) != this) {
        world.setBlockToAir(x, y, z);
      }
    }
  }

  @Override
  public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune) {
    if (meta == 0) return super.getDrops(world, x, y, z, meta, fortune);
    ArrayList<ItemStack> drops = new ArrayList<>();
    if (meta < 3) drops.add(new ItemStack(IEContent.blockWoodenDecoration, 1, 1));
    return drops;
  }

  @Override
  public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
    int meta = world.getBlockMetadata(x, y, z);
    switch (meta) {
      case 0:
        setBlockBounds(0.25f, 0f, 0.25f, 0.75f, 1f, 0.75f);
        break;
      case 1:
        setBlockBounds(0.3125f, 0f, 0.3125f, 0.6875f, 1f, 0.6875f);
        break;
      case 2:
        float minX = 0.3125f;
        float maxX = 0.6875f;
        float minZ = 0.3125f;
        float maxZ = 0.6875f;

        setBlockBounds(minX, 0f, minZ, maxX, 1f, maxZ);
        break;
      case 3:
      case 4:
      case 5:
      case 6:
        float fd = 0.34375f;
        float fu = 1f;
        if (canArmConnectToBlock(world, x, y - 1, z, true)) {
          fd = 0f;
          fu = 0.65625f;

          if (canArmConnectToBlock(world, x, y + 1, z, false)) fu = 1f;
        }
        setBlockBounds(meta == 6 ? -0.25f : .3125f, fd, meta == 4 ? -0.25f : .3125f, meta == 5 ? 1.25f : .6875f, fu, meta == 3 ? 1.25f : .6875f);
        break;
      default:
        setBlockBounds(0, 0, 0, 1, 1, 1);
    }
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
  public boolean isLadder(IBlockAccess world, int x, int y, int z, EntityLivingBase entity) {
    return world.getBlockMetadata(x, y, z) > 0;
  }

  @Override
  public boolean canConnectTransformer(IBlockAccess world, int x, int y, int z) {
    return world.getBlockMetadata(x, y, z) > 0;
  }
}
