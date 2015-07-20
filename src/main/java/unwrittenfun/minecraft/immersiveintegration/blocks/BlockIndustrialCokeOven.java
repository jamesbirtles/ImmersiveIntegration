package unwrittenfun.minecraft.immersiveintegration.blocks;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import unwrittenfun.minecraft.immersiveintegration.ImmersiveIntegration;
import unwrittenfun.minecraft.immersiveintegration.client.renderers.BlockRenderIndustrialCokeOven;
import unwrittenfun.minecraft.immersiveintegration.tiles.IMultiblockTile;
import unwrittenfun.minecraft.immersiveintegration.tiles.TileIndustrialCokeOven;

public class BlockIndustrialCokeOven extends BlockContainer {
  public IIcon topIcon;

  protected BlockIndustrialCokeOven(String key) {
    super(Material.rock);
    setBlockName(key);
    setBlockTextureName(key);
    setCreativeTab(ImmersiveIntegration.iiCreativeTab);
    setHardness(3f);
  }

  @Override
  public TileEntity createNewTileEntity(World world, int meta) {
    return new TileIndustrialCokeOven();
  }

  @Override
  public void registerBlockIcons(IIconRegister iconRegister) {
    super.registerBlockIcons(iconRegister);
    topIcon = iconRegister.registerIcon(getTextureName() + "Top");
  }

  @Override
  public IIcon getIcon(int side, int meta) {
    return (side == 0 || side == 1) ? topIcon : blockIcon;
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
    return BlockRenderIndustrialCokeOven.RENDER_ID;
  }

  @Override
  public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
    if (meta != 0) {
      TileEntity tileEntity = world.getTileEntity(x, y, z);
      if (tileEntity instanceof IMultiblockTile) {
        IMultiblockTile multiblockTile = (IMultiblockTile) tileEntity;
        if (multiblockTile.isFormed()) {
          multiblockTile.setFormed(false);
          int[] offset = multiblockTile.getOffset();
          for (int dz = -4; dz <= 0; dz++) {
            for (int dx = -3; dx <= 3; dx++) {
              for (int dy = -1; dy <= 2; dy++) {
                int ddz = dz * offset[4];
                int ddx = dx;
                if (offset[3] != 0) {
                  ddz = dx;
                  ddx = dz * offset[3];
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
}
