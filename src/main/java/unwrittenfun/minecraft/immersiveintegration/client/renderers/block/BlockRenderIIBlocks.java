package unwrittenfun.minecraft.immersiveintegration.client.renderers.block;

import blusunrize.immersiveengineering.client.ClientUtils;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;
import unwrittenfun.minecraft.immersiveintegration.client.IIRenderIDs;
import unwrittenfun.minecraft.immersiveintegration.tiles.IWireConnector;

public class BlockRenderIIBlocks implements ISimpleBlockRenderingHandler {
  @Override
  public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
    GL11.glPushMatrix();
    GL11.glScalef(1.25F, 1.25F, 1.25F);
    Tessellator.instance.startDrawingQuads();
    ClientUtils.handleStaticTileRenderer(block.createTileEntity(null, metadata));
    Tessellator.instance.draw();
    GL11.glPopMatrix();
  }

  @Override
  public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
    TileEntity tile = world.getTileEntity(x, y, z);
    ClientUtils.handleStaticTileRenderer(tile);
    if (tile instanceof IWireConnector) {
      ClientUtils.renderAttachedConnections(tile);
    }
    return true;
  }

  @Override
  public boolean shouldRender3DInInventory(int modelId) {
    return true;
  }

  @Override
  public int getRenderId() {
    return IIRenderIDs.BLOCKS;
  }
}
