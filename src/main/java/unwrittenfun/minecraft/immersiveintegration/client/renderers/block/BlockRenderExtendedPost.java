package unwrittenfun.minecraft.immersiveintegration.client.renderers.block;

import blusunrize.immersiveengineering.client.ClientUtils;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;
import unwrittenfun.minecraft.immersiveintegration.client.IIRenderIDs;
import unwrittenfun.minecraft.immersiveintegration.tiles.TileExtendedPost;

public class BlockRenderExtendedPost implements ISimpleBlockRenderingHandler {
  @Override
  public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
    GL11.glPushMatrix();
    GL11.glScalef(0.7f, 0.7f, 0.7f);
    GL11.glTranslatef(0f, -0.85f, 0f);
    Tessellator.instance.startDrawingQuads();
    ClientUtils.handleStaticTileRenderer(new TileExtendedPost());
    Tessellator.instance.draw();
    GL11.glPopMatrix();
  }

  @Override
  public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
    if (world.getBlockMetadata(x, y, z) == 0) ClientUtils.handleStaticTileRenderer(world.getTileEntity(x, y, z));
    return true;
  }

  @Override
  public boolean shouldRender3DInInventory(int modelId) {
    return true;
  }

  @Override
  public int getRenderId() {
    return IIRenderIDs.EXTENDED_POST;
  }
}
