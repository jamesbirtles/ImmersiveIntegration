package unwrittenfun.minecraft.immersiveintegration.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import unwrittenfun.minecraft.immersiveintegration.ImmersiveIntegration;
import unwrittenfun.minecraft.immersiveintegration.client.IIRenderIDs;
import unwrittenfun.minecraft.immersiveintegration.client.renderers.block.BlockRenderIIBlocks;

public abstract class BlockWireConnector extends BlockContainer {
  public BlockWireConnector(String key) {
    super(Material.iron);
    setBlockName(key);
    setBlockTextureName(key);
    setCreativeTab(ImmersiveIntegration.iiCreativeTab);
    setHardness(2f);
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
  public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta) {
    return ForgeDirection.OPPOSITES[side];
  }

  @Override
  public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
    TileEntity tileEntity = world.getTileEntity(x, y, z);
    if (tileEntity != null) {
      float length = 0.5f;
      switch (tileEntity.getBlockMetadata()) {
        case 0://UP
          this.setBlockBounds(.3125f, 0, .3125f, .6875f, length, .6875f);
          break;
        case 1://DOWN
          this.setBlockBounds(.3125f, 1 - length, .3125f, .6875f, 1, .6875f);
          break;
        case 2://SOUTH
          this.setBlockBounds(.3125f, .3125f, 0, .6875f, .6875f, length);
          break;
        case 3://NORTH
          this.setBlockBounds(.3125f, .3125f, 1 - length, .6875f, .6875f, 1);
          break;
        case 4://EAST
          this.setBlockBounds(0, .3125f, .3125f, length, .6875f, .6875f);
          break;
        case 5://WEST
          this.setBlockBounds(1 - length, .3125f, .3125f, 1, .6875f, .6875f);
          break;
      }
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
  public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
    ForgeDirection fd = ForgeDirection.getOrientation(world.getBlockMetadata(x, y, z));
    if (world.isAirBlock(x + fd.offsetX, y + fd.offsetY, z + fd.offsetZ)) {
      dropBlockAsItem(world, x, y, z, 0, 0);
      world.setBlockToAir(x, y, z);
    }
  }

  @Override
  public int getRenderType() {
    return IIRenderIDs.BLOCKS;
  }
}
