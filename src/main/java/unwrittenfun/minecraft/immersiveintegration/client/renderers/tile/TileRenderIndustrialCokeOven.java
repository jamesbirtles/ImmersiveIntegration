package unwrittenfun.minecraft.immersiveintegration.client.renderers.tile;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.model.obj.WavefrontObject;
import org.lwjgl.opengl.GL11;
import unwrittenfun.minecraft.immersiveintegration.ModInfo;

public class TileRenderIndustrialCokeOven extends TileEntitySpecialRenderer {
  private static final WavefrontObject model = ClientUtils.getModel(ModInfo.MOD_ID + ":models/cokeOven.obj");

  @Override
  public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f) {
    if (tile.getBlockMetadata() == 1 || tile.getBlockMetadata() == 2) {
      GL11.glPushMatrix();

      GL11.glTranslated(x, y, z);

      if (tile.getBlockMetadata() == 1) {
        GL11.glRotatef(90f, 0, 1, 0);
        GL11.glTranslated(-1, 0, 0);
      }

      int l = tile.getWorldObj().getLightBrightnessForSkyBlocks(tile.xCoord, tile.yCoord + 3000, tile.zCoord, 0);
      int l1 = l % 65536;
      int l2 = l / 65536;
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) l1, (float) l2);

      Matrix4 translationMatrix = new Matrix4();
      Matrix4 rotationMatrix = new Matrix4();
      //      translationMatrix.translate(x + 0.5, y + 1, z + 0.5);
      //
      //      if (tile.getBlockMetadata() == 1) {
      //        rotationMatrix.rotate(Math.PI/2.0, 0, 1, 0);
      //      }

      GL11.glColor4f(1, 1, 1, 1);
      ClientUtils.bindTexture(ModInfo.MOD_ID + ":textures/blocks/cokeOven.png");
      Tessellator.instance.startDrawingQuads();
      ClientUtils.renderStaticWavefrontModel(tile, model, Tessellator.instance, translationMatrix, rotationMatrix, 0, false);
      Tessellator.instance.draw();

      IIcon lava = Blocks.lava.getIcon(0, 0);
      Tessellator tess = Tessellator.instance;

      Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
      float dz = 0;
      float dx = -1.8f;
      float dy = -1;

      GL11.glDisable(GL11.GL_LIGHTING);
      tess.startDrawingQuads();

      double lavaWidth = (lava.getIconWidth() / 16) * ((lava.getMaxU() - lava.getMinU()) / lava.getIconWidth());
      for (int i = 0; i < 4; i++) {
        tess.addVertexWithUV(dx, dy + i, dz + 5 / 16f, lava.getMinU() + (5.0 * lavaWidth), lava.getMaxV());
        tess.addVertexWithUV(dx, dy + i, dz + 11 / 16f, lava.getMaxU() - (5.0 * lavaWidth), lava.getMaxV());
        tess.addVertexWithUV(dx, dy + i + 1, dz + 11 / 16f, lava.getMaxU() - (5.0 * lavaWidth), lava.getMinV());
        tess.addVertexWithUV(dx, dy + i + 1, dz + 5 / 16f, lava.getMinU() + (5.0 * lavaWidth), lava.getMinV());

        tess.addVertexWithUV(dx + 4.6f, dy + i + 1, dz + 5 / 16f, lava.getMaxU() - (5.0 * lavaWidth), lava.getMinV());
        tess.addVertexWithUV(dx + 4.6f, dy + i + 1, dz + 11 / 16f, lava.getMinU() + (5.0 * lavaWidth), lava.getMinV());
        tess.addVertexWithUV(dx + 4.6f, dy + i, dz + 11 / 16f, lava.getMinU() + (5.0 * lavaWidth), lava.getMaxV());
        tess.addVertexWithUV(dx + 4.6f, dy + i, dz + 5 / 16f, lava.getMaxU() - (5.0 * lavaWidth), lava.getMaxV());

        tess.addVertexWithUV(dx, dy + i, dz + 2 + 5 / 16f, lava.getMinU() + (5.0 * lavaWidth), lava.getMaxV());
        tess.addVertexWithUV(dx, dy + i, dz + 2 + 11 / 16f, lava.getMaxU() - (5.0 * lavaWidth), lava.getMaxV());
        tess.addVertexWithUV(dx, dy + i + 1, dz + 2 + 11 / 16f, lava.getMaxU() - (5.0 * lavaWidth), lava.getMinV());
        tess.addVertexWithUV(dx, dy + i + 1, dz + 2 + 5 / 16f, lava.getMinU() + (5.0 * lavaWidth), lava.getMinV());

        tess.addVertexWithUV(dx + 4.6f, dy + i + 1, dz + 2 + 5 / 16f, lava.getMaxU() - (5.0 * lavaWidth), lava.getMinV());
        tess.addVertexWithUV(dx + 4.6f, dy + i + 1, dz + 2 + 11 / 16f, lava.getMinU() + (5.0 * lavaWidth), lava.getMinV());
        tess.addVertexWithUV(dx + 4.6f, dy + i, dz + 2 + 11 / 16f, lava.getMinU() + (5.0 * lavaWidth), lava.getMaxV());
        tess.addVertexWithUV(dx + 4.6f, dy + i, dz + 2 + 5 / 16f, lava.getMaxU() - (5.0 * lavaWidth), lava.getMaxV());

        tess.addVertexWithUV(dx, dy + i, dz - 2 + 5 / 16f, lava.getMinU() + (5.0 * lavaWidth), lava.getMaxV());
        tess.addVertexWithUV(dx, dy + i, dz - 2 + 11 / 16f, lava.getMaxU() - (5.0 * lavaWidth), lava.getMaxV());
        tess.addVertexWithUV(dx, dy + i + 1, dz - 2 + 11 / 16f, lava.getMaxU() - (5.0 * lavaWidth), lava.getMinV());
        tess.addVertexWithUV(dx, dy + i + 1, dz - 2 + 5 / 16f, lava.getMinU() + (5.0 * lavaWidth), lava.getMinV());

        tess.addVertexWithUV(dx + 4.6f, dy + i + 1, dz - 2 + 5 / 16f, lava.getMaxU() - (5.0 * lavaWidth), lava.getMinV());
        tess.addVertexWithUV(dx + 4.6f, dy + i + 1, dz - 2 + 11 / 16f, lava.getMinU() + (5.0 * lavaWidth), lava.getMinV());
        tess.addVertexWithUV(dx + 4.6f, dy + i, dz - 2 + 11 / 16f, lava.getMinU() + (5.0 * lavaWidth), lava.getMaxV());
        tess.addVertexWithUV(dx + 4.6f, dy + i, dz - 2 + 5 / 16f, lava.getMaxU() - (5.0 * lavaWidth), lava.getMaxV());
      }
      tess.draw();

      GL11.glEnable(GL11.GL_LIGHTING);

      GL11.glPopMatrix();
    }
  }
}
