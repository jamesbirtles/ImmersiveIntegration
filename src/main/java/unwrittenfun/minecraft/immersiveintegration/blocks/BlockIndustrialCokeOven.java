package unwrittenfun.minecraft.immersiveintegration.blocks;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import unwrittenfun.minecraft.immersiveintegration.ImmersiveIntegration;
import unwrittenfun.minecraft.immersiveintegration.client.IIRenderIDs;
import unwrittenfun.minecraft.immersiveintegration.client.renderers.block.BlockRenderIndustrialCokeOven;
import unwrittenfun.minecraft.immersiveintegration.tiles.IMultiblockTile;
import unwrittenfun.minecraft.immersiveintegration.tiles.TileIndustrialCokeOven;
import unwrittenfun.minecraft.immersiveintegration.utils.TileUtils;

import java.util.ArrayList;

public class BlockIndustrialCokeOven extends BlockContainer {
  public IIcon mapIcon;

  public BlockIndustrialCokeOven(String key) {
    super(Material.iron);
    setBlockName(key);
    setBlockTextureName(key);
    setHardness(3f);
    setStepSound(Block.soundTypeMetal);
  }

  @Override
  public TileEntity createNewTileEntity(World world, int meta) {
    return new TileIndustrialCokeOven();
  }

  @Override
  public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
    TileEntity tileEntity = world.getTileEntity(target.blockX, target.blockY, target.blockZ);
    if (tileEntity instanceof TileIndustrialCokeOven) {
      TileIndustrialCokeOven industrialCokeOven = (TileIndustrialCokeOven) tileEntity;
      if (industrialCokeOven.getReplaced() != null) {
        return industrialCokeOven.getReplaced().copy();
      }
    }
    return new ItemStack(IIBlocks.steelDecoration);
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
  public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
    return true;
  }

  @Override
  public int getRenderType() {
    return IIRenderIDs.COKE_OVEN;
  }

  @Override
  public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
    if (meta != 0) {
      TileEntity tileEntity = world.getTileEntity(x, y, z);
      if (tileEntity instanceof IMultiblockTile) {
        IMultiblockTile multiblockTile = (IMultiblockTile) tileEntity;

        if (multiblockTile.isFormed()) {
          if (multiblockTile.getReplaced() != null) {
            world.spawnEntityInWorld(new EntityItem(world, x + .5, y + .5, z + .5, multiblockTile.getReplaced().copy()));
          }

          TileUtils.dropInventory(world, x, y, z);

          multiblockTile.setFormed(false);
          int[] offset = multiblockTile.getOffset();
          for (int dz = -4; dz <= 0; dz++) {
            for (int dx = -3; dx <= 3; dx++) {
              for (int dy = -1; dy <= 2; dy++) {
                int ddz = dz * ForgeDirection.getOrientation(offset[3]).offsetZ;
                int ddx = dx;
                if (ForgeDirection.getOrientation(offset[3]).offsetX != 0) {
                  ddz = dx;
                  ddx = dz * ForgeDirection.getOrientation(offset[3]).offsetX;
                }
                TileEntity tileEntity1 = world.getTileEntity(x + ddx - offset[0], y + dy - offset[1], z + ddz - offset[2]);
                if (tileEntity1 != tileEntity && tileEntity1 instanceof IMultiblockTile) {
                  IMultiblockTile multiblockTileReplace = (IMultiblockTile) tileEntity1;
                  ItemStack replaced = multiblockTileReplace.getReplaced();
                  multiblockTileReplace.setFormed(false);
                  if (replaced != null) {
                    world.setBlock(x + ddx - offset[0], y + dy - offset[1], z + ddz - offset[2], ((ItemBlock) replaced.getItem()).field_150939_a, replaced.getItemDamage(), 3);
                  }
                }
              }
            }
          }
        }
      }
    }
    super.breakBlock(world, x, y, z, block, meta);
  }

  @Override
  public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
    if (!world.isRemote) {
      TileEntity tileEntity = world.getTileEntity(x, y, z);
      if (tileEntity instanceof TileIndustrialCokeOven) {
        TileIndustrialCokeOven cokeOven = (TileIndustrialCokeOven) tileEntity;
        if (cokeOven.formed) {
          int[] off = cokeOven.getOffset();
          FMLNetworkHandler.openGui(player, ImmersiveIntegration.instance, 0, world, x - off[0], y - off[1], z - off[2]);
        }
      }
    }
    return world.getBlockMetadata(x, y, z) > 0;
  }

  @Override
  public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
    return new ArrayList<>();
  }


}
