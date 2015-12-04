package unwrittenfun.minecraft.immersiveintegration.client.renderers.block;

import blusunrize.immersiveengineering.client.ClientUtils;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import unwrittenfun.minecraft.immersiveintegration.client.IIRenderIDs;

public class BlockRenderIndustrialCokeOven implements ISimpleBlockRenderingHandler {
  @Override
  public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
    renderer.setRenderBoundsFromBlock(block);
    ClientUtils.drawInventoryBlock(block, metadata, renderer);
  }

  @Override
  public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
    int meta = world.getBlockMetadata(x, y, z);
    if (meta == 0) {
      block.setBlockBoundsBasedOnState(world, x, y, z);
      renderer.setRenderBoundsFromBlock(block);
      return renderer.renderStandardBlock(block, x, y, z);
    } else if (meta < 3) {
      ClientUtils.handleStaticTileRenderer(world.getTileEntity(x, y, z));
      return true;
    } else {
      if (renderer.hasOverrideBlockTexture()) {
        block.setBlockBoundsBasedOnState(world, x, y, z);
        renderer.setRenderBoundsFromBlock(block);
        return renderer.renderStandardBlock(block, x, y, z);
      }
    }
    return false;
  }

  @Override
  public boolean shouldRender3DInInventory(int modelId) {
    return true;
  }

  @Override
  public int getRenderId() {
    return IIRenderIDs.COKE_OVEN;
  }
}
