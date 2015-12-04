package unwrittenfun.minecraft.immersiveintegration.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import unwrittenfun.minecraft.immersiveintegration.ImmersiveIntegration;
import unwrittenfun.minecraft.immersiveintegration.client.IIRenderIDs;
import unwrittenfun.minecraft.immersiveintegration.client.renderers.block.BlockRenderIIBlocks;
import unwrittenfun.minecraft.immersiveintegration.tiles.TileWallConnector;
import unwrittenfun.minecraft.immersiveintegration.utils.PlayerUtils;
import unwrittenfun.minecraft.immersiveintegration.utils.TileUtils;

public class BlockWallConnector extends BlockContainer {
  public BlockWallConnector(String key) {
    super(Material.wood);
    setBlockName(key);
    setBlockTextureName(key);
    setCreativeTab(ImmersiveIntegration.iiCreativeTab);
    setHardness(2f);
  }

  @Override
  public TileEntity createNewTileEntity(World world, int meta) {
    return new TileWallConnector();
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
    return IIRenderIDs.BLOCKS;
  }

  @Override
  public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
    Item heldItem = PlayerUtils.getHeldItem(player);
    if (heldItem instanceof ItemBlock) {
      Block block = ((ItemBlock) heldItem).field_150939_a;
      TileWallConnector tile = TileUtils.getTileEntity(world, x, y, z, TileWallConnector.class);
      if (tile != null) {
//        tile.setSideStack(player.getHeldItem());
      }
    }

    return false;
  }
}
