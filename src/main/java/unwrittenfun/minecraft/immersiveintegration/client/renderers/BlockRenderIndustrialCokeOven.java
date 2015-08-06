package unwrittenfun.minecraft.immersiveintegration.client.renderers;

import blusunrize.immersiveengineering.client.ClientUtils;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;
import unwrittenfun.minecraft.immersiveintegration.blocks.IIBlocks;

public class BlockRenderIndustrialCokeOven implements ISimpleBlockRenderingHandler {
  public static int RENDER_ID = RenderingRegistry.getNextAvailableRenderId();

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
      if (meta == 1) {
        Tessellator.instance.draw();
        Tessellator.instance.startDrawingQuads();
        GL11.glPushMatrix();
        GL11.glTranslated(x - (Math.floor(x / 16f) * 16), 0, z - (Math.floor(z / 16f) * 16));
        GL11.glRotatef(90, 0, 1, 0);
        GL11.glTranslated(-(x - (Math.floor(x / 16f) * 16)) - 1, 0, -(z - (Math.floor(z / 16f) * 16)));
        renderCokeOven(world, x, y, z, true);
        Tessellator.instance.draw();
        Tessellator.instance.startDrawingQuads();
        GL11.glPopMatrix();
      } else {
        renderCokeOven(world, x, y, z, false);
      }
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

  private void renderCokeOven(IBlockAccess world, int x, int y, int z, boolean rotate) {
    Tessellator tess = Tessellator.instance;


    Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);

    IIcon lava = Blocks.lava.getIcon(0, 0);

    IIcon icon = IIBlocks.industrialCokeOven.mapIcon;


    // Side Short
    int dz = z + 4;
    int dy = y - 1;
    int dx = x - 2;
    float faceOffU = 0f;
    float faceOffV = 64f * (icon.getIconHeight() / 256f);
    float faceWidth = 80f * (icon.getIconWidth() / 256f);
    float faceHeight = 64f * (icon.getIconHeight() / 256f);
    double minU = icon.getMinU() + (faceOffU / icon.getIconWidth()) * (icon.getMaxU() - icon.getMinU());
    double maxU = minU + (faceWidth / icon.getIconWidth()) * (icon.getMaxU() - icon.getMinU());
    double minV = icon.getMinV() + (faceOffV / icon.getIconHeight()) * (icon.getMaxV() - icon.getMinV());
    double maxV = minV + (faceHeight / icon.getIconHeight()) * (icon.getMaxV() - icon.getMinV());

    ClientUtils.BlockLightingInfo info = ClientUtils.calculateBlockLighting(rotate ? 5 : 2, world, IIBlocks.industrialCokeOven, x + (rotate ? 4 : 0), y, z + (rotate ? 0 : 4), 1, 1, 1);
    tess.setColorOpaque_F(info.colorRedTopLeft, info.colorGreenTopLeft, info.colorBlueTopLeft);
    tess.setBrightness(info.brightnessTopLeft);

    tess.addVertexWithUV(dx, dy, dz, minU, maxV);
    tess.addVertexWithUV(dx + 5, dy, dz, maxU, maxV);
    tess.addVertexWithUV(dx + 5, dy + 4, dz, maxU, minV);
    tess.addVertexWithUV(dx, dy + 4, dz, minU, minV);

    // Lava
    dx = x;
    for (int i = 0; i < 4; i++) {
      tess.addVertexWithUV(dx, dy + i, dz - 0.01f, lava.getMinU(), lava.getMaxV());
      tess.addVertexWithUV(dx + 1, dy + i, dz - 0.01f, lava.getMaxU(), lava.getMaxV());
      tess.addVertexWithUV(dx + 1, dy + i + 1, dz - 0.01f, lava.getMaxU(), lava.getMinV());
      tess.addVertexWithUV(dx, dy + i + 1, dz - 0.01f, lava.getMinU(), lava.getMinV());

      tess.addVertexWithUV(dx, dy + i + 1, dz - 6.99f, lava.getMaxU(), lava.getMinV());
      tess.addVertexWithUV(dx + 1, dy + i + 1, dz - 6.99f, lava.getMinU(), lava.getMinV());
      tess.addVertexWithUV(dx + 1, dy + i, dz - 6.99f, lava.getMinU(), lava.getMaxV());
      tess.addVertexWithUV(dx, dy + i, dz - 6.99f, lava.getMaxU(), lava.getMaxV());
    }

    dx = x - 2;
    dz = z - 3;
    tess.addVertexWithUV(dx, dy + 4, dz, maxU, minV);
    tess.addVertexWithUV(dx + 5, dy + 4, dz, minU, minV);
    tess.addVertexWithUV(dx + 5, dy, dz, minU, maxV);
    tess.addVertexWithUV(dx, dy, dz, maxU, maxV);

    // Side long

    faceOffU = 0f;
    faceOffV = 0f;
    faceWidth = 112f * (icon.getIconWidth() / 256f);
    faceHeight = 64f * (icon.getIconHeight() / 256f);
    minU = icon.getMinU() + (faceOffU / icon.getIconWidth()) * (icon.getMaxU() - icon.getMinU());
    maxU = minU + (faceWidth / icon.getIconWidth()) * (icon.getMaxU() - icon.getMinU());
    minV = icon.getMinV() + (faceOffV / icon.getIconHeight()) * (icon.getMaxV() - icon.getMinV());
    maxV = minV + (faceHeight / icon.getIconHeight()) * (icon.getMaxV() - icon.getMinV());

    info = ClientUtils.calculateBlockLighting(rotate ? 3 : 5, world, IIBlocks.industrialCokeOven, x + (rotate ? 0 : + 3), y, z + (rotate ? 3 : 0), 1, 1, 1);
    tess.setColorOpaque_F(info.colorRedTopLeft, info.colorGreenTopLeft, info.colorBlueTopLeft);
    tess.setBrightness(info.brightnessTopLeft);

    dz = z - 3;
    dx = x - 2;
    tess.addVertexWithUV(dx, dy, dz, minU, maxV);
    tess.addVertexWithUV(dx, dy, dz + 7, maxU, maxV);
    tess.addVertexWithUV(dx, dy + 4, dz + 7, maxU, minV);
    tess.addVertexWithUV(dx, dy + 4, dz, minU, minV);

    dx = x + 3;
    tess.addVertexWithUV(dx, dy + 4, dz, maxU, minV);
    tess.addVertexWithUV(dx, dy + 4, dz + 7, minU, minV);
    tess.addVertexWithUV(dx, dy, dz + 7, minU, maxV);
    tess.addVertexWithUV(dx, dy, dz, maxU, maxV);

    // Lava
    dz = z;
    dx = x - 2;
    for (int i = 0; i < 4; i++) {
      tess.addVertexWithUV(dx + 0.01f, dy + i, dz, lava.getMinU(), lava.getMaxV());
      tess.addVertexWithUV(dx + 0.01f, dy + i, dz + 1, lava.getMaxU(), lava.getMaxV());
      tess.addVertexWithUV(dx + 0.01f, dy + i + 1, dz + 1, lava.getMaxU(), lava.getMinV());
      tess.addVertexWithUV(dx + 0.01f, dy + i + 1, dz, lava.getMinU(), lava.getMinV());

      tess.addVertexWithUV(dx + 4.99f, dy + i + 1, dz, lava.getMaxU(), lava.getMinV());
      tess.addVertexWithUV(dx + 4.99f, dy + i + 1, dz + 1, lava.getMinU(), lava.getMinV());
      tess.addVertexWithUV(dx + 4.99f, dy + i, dz + 1, lava.getMinU(), lava.getMaxV());
      tess.addVertexWithUV(dx + 4.99f, dy + i, dz, lava.getMaxU(), lava.getMaxV());
    }

    // Top and bottom

    info = ClientUtils.calculateBlockLighting(0, world, IIBlocks.industrialCokeOven, x, y+3, z, 1, 1, 1);
    tess.setColorOpaque_F(255, 255, 255);
    tess.setBrightness(info.brightnessTopLeft);

    faceOffU = 112f * (icon.getIconWidth() / 256f);
    faceOffV = 0f;
    faceWidth = 112f * (icon.getIconWidth() / 256f);
    faceHeight = 80f * (icon.getIconHeight() / 256f);
    minU = icon.getMinU() + (faceOffU / icon.getIconWidth()) * (icon.getMaxU() - icon.getMinU());
    maxU = minU + (faceWidth / icon.getIconWidth()) * (icon.getMaxU() - icon.getMinU());
    minV = icon.getMinV() + (faceOffV / icon.getIconHeight()) * (icon.getMaxV() - icon.getMinV());
    maxV = minV + (faceHeight / icon.getIconHeight()) * (icon.getMaxV() - icon.getMinV());

    dy = y + 3;
    dx = x - 2;
    dz = z - 3;
    tess.addVertexWithUV(dx, dy, dz, minU, maxV);
    tess.addVertexWithUV(dx, dy, dz + 7, maxU, maxV);
    tess.addVertexWithUV(dx + 5, dy, dz + 7, maxU, minV);
    tess.addVertexWithUV(dx + 5, dy, dz, minU, minV);



    info = ClientUtils.calculateBlockLighting(0, world, IIBlocks.industrialCokeOven, x, y-2, z, 1, 1, 1);
    tess.setColorOpaque_F(info.colorRedTopLeft, info.colorGreenTopLeft, info.colorBlueTopLeft);
    tess.setBrightness(info.brightnessTopLeft);
    dy = y - 1;
    tess.addVertexWithUV(dx + 5, dy, dz, maxU, minV);
    tess.addVertexWithUV(dx + 5, dy, dz + 7, minU, minV);
    tess.addVertexWithUV(dx, dy, dz + 7, minU, maxV);
    tess.addVertexWithUV(dx, dy, dz, maxU, maxV);
  }

  @Override
  public boolean shouldRender3DInInventory(int modelId) {
    return true;
  }

  @Override
  public int getRenderId() {
    return RENDER_ID;
  }
}
