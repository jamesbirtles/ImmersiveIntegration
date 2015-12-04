package unwrittenfun.minecraft.immersiveintegration.blocks;

import appeng.api.AEApi;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import unwrittenfun.minecraft.immersiveintegration.ImmersiveIntegration;
import unwrittenfun.minecraft.immersiveintegration.client.IIRenderIDs;
import unwrittenfun.minecraft.immersiveintegration.client.renderers.block.BlockRenderIIBlocks;
import unwrittenfun.minecraft.immersiveintegration.tiles.TileMETransformer;
import unwrittenfun.minecraft.immersiveintegration.utils.TileUtils;

public class BlockMETransformer extends BlockContainer {
  public BlockMETransformer(String key) {
    super(Material.iron);
    setBlockName(key);
    setBlockTextureName(key);
    setCreativeTab(ImmersiveIntegration.iiCreativeTab);
    setHardness(2.5f);
  }

  @Override
  public TileEntity createNewTileEntity(World world, int meta) {
    return new TileMETransformer();
  }

  @Override
  public int getRenderType() {
    return IIRenderIDs.BLOCKS;
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
    return true;
  }

  @Override
  public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
    if ((meta & 8) == 8) {
      world.setBlockToAir(x, y - 1, z);
    } else {
      world.setBlockToAir(x, y + 1, z);
    }
  }

  @Override
  public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
    if (!world.isRemote && entity instanceof EntityPlayer) {
      TileMETransformer transformer = TileUtils.getTileEntity(world, x, y, z, TileMETransformer.class);
      if (transformer != null) {
        transformer.getGridNode(ForgeDirection.UNKNOWN).setPlayerID(AEApi.instance().registries().players().getID((EntityPlayer) entity));
      }

      transformer = TileUtils.getTileEntity(world, x, y + 1, z, TileMETransformer.class);
      if (transformer != null) {
        transformer.getGridNode(ForgeDirection.UNKNOWN).setPlayerID(AEApi.instance().registries().players().getID((EntityPlayer) entity));
      }
    }
  }
}
