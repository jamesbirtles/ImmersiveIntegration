package unwrittenfun.minecraft.immersiveintegration.blocks;

import appeng.core.WorldSettings;
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
import unwrittenfun.minecraft.immersiveintegration.client.renderers.block.BlockRenderIIBlocks;
import unwrittenfun.minecraft.immersiveintegration.tiles.TileMETransformer;

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
    return BlockRenderIIBlocks.RENDER_ID;
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
    TileEntity tileEntity = world.getTileEntity(x, y, z);
    if (!world.isRemote && entity instanceof EntityPlayer && tileEntity instanceof TileMETransformer) {
      TileMETransformer transformer = (TileMETransformer) tileEntity;
      if (transformer.theGridNode == null) transformer.createAELink();
      transformer.theGridNode.setPlayerID(WorldSettings.getInstance().getPlayerID(((EntityPlayer) entity).getGameProfile()));
    }

    TileEntity tileEntity1 = world.getTileEntity(x, y + 1, z);
    if (!world.isRemote && entity instanceof EntityPlayer && tileEntity1 instanceof TileMETransformer) {
      TileMETransformer transformer = (TileMETransformer) tileEntity1;
      if (transformer.theGridNode == null) transformer.createAELink();
      transformer.theGridNode.setPlayerID(WorldSettings.getInstance().getPlayerID(((EntityPlayer) entity).getGameProfile()));
    }
  }
}
