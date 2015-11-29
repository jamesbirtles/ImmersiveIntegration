package unwrittenfun.minecraft.immersiveintegration.utils;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.models.ModelIEObj;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.obj.Face;
import net.minecraftforge.client.model.obj.GroupObject;
import net.minecraftforge.client.model.obj.TextureCoordinate;
import net.minecraftforge.client.model.obj.WavefrontObject;
import net.minecraftforge.common.util.ForgeDirection;

public class RenderUtils {
  public static void renderFaceLightingXNeg(Tessellator tessellator, Block block, int meta, IBlockAccess world, int x, int y, int z) {
    int side = 4;
    RenderBlocks.getInstance().setRenderBounds(0, 0, 0, 1, 1, 1);
    ClientUtils.BlockLightingInfo lightingInfo = ClientUtils.calculateBlockLighting(side, world, block, x, y, z, 1, 1, 1);
    IIcon icon = block.getIcon(side, meta);

    tessellator.setColorOpaque_F(lightingInfo.colorRedTopLeft, lightingInfo.colorGreenTopLeft, lightingInfo.colorBlueTopLeft);
    tessellator.setBrightness(lightingInfo.brightnessTopLeft);
    tessellator.addVertexWithUV((double) x, y + 1, z + 1, icon.getMaxU(), icon.getMinV());
    tessellator.setColorOpaque_F(lightingInfo.colorRedBottomLeft, lightingInfo.colorGreenBottomLeft, lightingInfo.colorBlueBottomLeft);
    tessellator.setBrightness(lightingInfo.brightnessBottomLeft);
    tessellator.addVertexWithUV((double) x, y + 1, (double) z, icon.getMinU(), icon.getMinV());
    tessellator.setColorOpaque_F(lightingInfo.colorRedBottomRight, lightingInfo.colorGreenBottomRight, lightingInfo.colorBlueBottomRight);
    tessellator.setBrightness(lightingInfo.brightnessBottomRight);
    tessellator.addVertexWithUV((double) x, (double) y, (double) z, icon.getMinU(), icon.getMaxV());
    tessellator.setColorOpaque_F(lightingInfo.colorRedTopRight, lightingInfo.colorGreenTopRight, lightingInfo.colorBlueTopRight);
    tessellator.setBrightness(lightingInfo.brightnessTopRight);
    tessellator.addVertexWithUV((double) x, (double) y, z + 1, icon.getMaxU(), icon.getMaxV());
  }

  public static void renderFaceLightingXPos(Tessellator tessellator, Block block, int meta, IBlockAccess world, int x, int y, int z) {
    int side = 5;
    RenderBlocks.getInstance().setRenderBounds(0, 0, 0, 1, 1, 1);
    ClientUtils.BlockLightingInfo lightingInfo = ClientUtils.calculateBlockLighting(side, world, block, x, y, z, 1, 1, 1);
    IIcon icon = block.getIcon(side, meta);

    tessellator.setColorOpaque_F(lightingInfo.colorRedTopLeft, lightingInfo.colorGreenTopLeft, lightingInfo.colorBlueTopLeft);
    tessellator.setBrightness(lightingInfo.brightnessTopLeft);
    tessellator.addVertexWithUV(x + 1, y, z + 1, icon.getMinU(), icon.getMaxV());
    tessellator.setColorOpaque_F(lightingInfo.colorRedBottomLeft, lightingInfo.colorGreenBottomLeft, lightingInfo.colorBlueBottomLeft);
    tessellator.setBrightness(lightingInfo.brightnessBottomLeft);
    tessellator.addVertexWithUV(x + 1, y, z, icon.getMaxU(), icon.getMaxV());
    tessellator.setColorOpaque_F(lightingInfo.colorRedBottomRight, lightingInfo.colorGreenBottomRight, lightingInfo.colorBlueBottomRight);
    tessellator.setBrightness(lightingInfo.brightnessBottomRight);
    tessellator.addVertexWithUV(x + 1, y + 1, z, icon.getMaxU(), icon.getMinV());
    tessellator.setColorOpaque_F(lightingInfo.colorRedTopRight, lightingInfo.colorGreenTopRight, lightingInfo.colorBlueTopRight);
    tessellator.setBrightness(lightingInfo.brightnessTopRight);
    tessellator.addVertexWithUV(x + 1, y + 1, z + 1, icon.getMinU(), icon.getMinV());
  }

  public static void renderFaceLightingYNeg(Tessellator tessellator, Block block, int meta, IBlockAccess world, int x, int y, int z) {
    int side = 0;
    RenderBlocks.getInstance().setRenderBounds(0, 0, 0, 1, 1, 1);
    ClientUtils.BlockLightingInfo lightingInfo = ClientUtils.calculateBlockLighting(side, world, block, x, y, z, 1, 1, 1);
    IIcon icon = block.getIcon(side, meta);

    tessellator.setColorOpaque_F(lightingInfo.colorRedTopLeft, lightingInfo.colorGreenTopLeft, lightingInfo.colorBlueTopLeft);
    tessellator.setBrightness(lightingInfo.brightnessTopLeft);
    tessellator.addVertexWithUV(x, y, z + 1, icon.getMinU(), icon.getMaxV());
    tessellator.setColorOpaque_F(lightingInfo.colorRedBottomLeft, lightingInfo.colorGreenBottomLeft, lightingInfo.colorBlueBottomLeft);
    tessellator.setBrightness(lightingInfo.brightnessBottomLeft);
    tessellator.addVertexWithUV(x, y, z, icon.getMinU(), icon.getMinV());
    tessellator.setColorOpaque_F(lightingInfo.colorRedBottomRight, lightingInfo.colorGreenBottomRight, lightingInfo.colorBlueBottomRight);
    tessellator.setBrightness(lightingInfo.brightnessBottomRight);
    tessellator.addVertexWithUV(x + 1, y, z, icon.getMaxU(), icon.getMinV());
    tessellator.setColorOpaque_F(lightingInfo.colorRedTopRight, lightingInfo.colorGreenTopRight, lightingInfo.colorBlueTopRight);
    tessellator.setBrightness(lightingInfo.brightnessTopRight);
    tessellator.addVertexWithUV(x + 1, y, z + 1, icon.getMaxU(), icon.getMaxV());
  }

  public static void renderFaceLightingYPos(Tessellator tessellator, Block block, int meta, IBlockAccess world, int x, int y, int z) {
    int side = 1;
    RenderBlocks.getInstance().setRenderBounds(0, 0, 0, 1, 1, 1);
    ClientUtils.BlockLightingInfo lightingInfo = ClientUtils.calculateBlockLighting(side, world, block, x, y, z, 1, 1, 1);
    IIcon icon = block.getIcon(side, meta);

    tessellator.setColorOpaque_F(lightingInfo.colorRedTopLeft, lightingInfo.colorGreenTopLeft, lightingInfo.colorBlueTopLeft);
    tessellator.setBrightness(lightingInfo.brightnessTopLeft);
    tessellator.addVertexWithUV(x + 1, y + 1, z + 1, icon.getMaxU(), icon.getMaxV());
    tessellator.setColorOpaque_F(lightingInfo.colorRedBottomLeft, lightingInfo.colorGreenBottomLeft, lightingInfo.colorBlueBottomLeft);
    tessellator.setBrightness(lightingInfo.brightnessBottomLeft);
    tessellator.addVertexWithUV(x + 1, y + 1, z, icon.getMaxU(), icon.getMinV());
    tessellator.setColorOpaque_F(lightingInfo.colorRedBottomRight, lightingInfo.colorGreenBottomRight, lightingInfo.colorBlueBottomRight);
    tessellator.setBrightness(lightingInfo.brightnessBottomRight);
    tessellator.addVertexWithUV(x, y + 1, z, icon.getMinU(), icon.getMinV());
    tessellator.setColorOpaque_F(lightingInfo.colorRedTopRight, lightingInfo.colorGreenTopRight, lightingInfo.colorBlueTopRight);
    tessellator.setBrightness(lightingInfo.brightnessTopRight);
    tessellator.addVertexWithUV(x, y + 1, z + 1, icon.getMinU(), icon.getMaxV());
  }

  public static void renderFaceLightingZNeg(Tessellator tessellator, Block block, int meta, IBlockAccess world, int x, int y, int z) {
    int side = 2;
    RenderBlocks.getInstance().setRenderBounds(0, 0, 0, 1, 1, 1);
    ClientUtils.BlockLightingInfo lightingInfo = ClientUtils.calculateBlockLighting(side, world, block, x, y, z, 1, 1, 1);
    IIcon icon = block.getIcon(side, meta);

    tessellator.setColorOpaque_F(lightingInfo.colorRedTopLeft, lightingInfo.colorGreenTopLeft, lightingInfo.colorBlueTopLeft);
    tessellator.setBrightness(lightingInfo.brightnessTopLeft);
    tessellator.addVertexWithUV(x, y + 1, z, icon.getMaxU(), icon.getMinV());
    tessellator.setColorOpaque_F(lightingInfo.colorRedBottomLeft, lightingInfo.colorGreenBottomLeft, lightingInfo.colorBlueBottomLeft);
    tessellator.setBrightness(lightingInfo.brightnessBottomLeft);
    tessellator.addVertexWithUV(x + 1, y + 1, z, icon.getMinU(), icon.getMinV());
    tessellator.setColorOpaque_F(lightingInfo.colorRedBottomRight, lightingInfo.colorGreenBottomRight, lightingInfo.colorBlueBottomRight);
    tessellator.setBrightness(lightingInfo.brightnessBottomRight);
    tessellator.addVertexWithUV(x + 1, y, z, icon.getMinU(), icon.getMaxV());
    tessellator.setColorOpaque_F(lightingInfo.colorRedTopRight, lightingInfo.colorGreenTopRight, lightingInfo.colorBlueTopRight);
    tessellator.setBrightness(lightingInfo.brightnessTopRight);
    tessellator.addVertexWithUV(x, y, z, icon.getMaxU(), icon.getMaxV());
  }

  public static void renderFaceLightingZPos(Tessellator tessellator, Block block, int meta, IBlockAccess world, int x, int y, int z) {
    int side = 3;
    RenderBlocks.getInstance().setRenderBounds(0, 0, 0, 1, 1, 1);
    ClientUtils.BlockLightingInfo lightingInfo = ClientUtils.calculateBlockLighting(side, world, block, x, y, z, 1, 1, 1);
    IIcon icon = block.getIcon(side, meta);

    tessellator.setColorOpaque_F(lightingInfo.colorRedTopLeft, lightingInfo.colorGreenTopLeft, lightingInfo.colorBlueTopLeft);
    tessellator.setBrightness(lightingInfo.brightnessTopLeft);
    tessellator.addVertexWithUV(x, y + 1, z + 1, icon.getMinU(), icon.getMinV());
    tessellator.setColorOpaque_F(lightingInfo.colorRedBottomLeft, lightingInfo.colorGreenBottomLeft, lightingInfo.colorBlueBottomLeft);
    tessellator.setBrightness(lightingInfo.brightnessBottomLeft);
    tessellator.addVertexWithUV(x, y, z + 1, icon.getMinU(), icon.getMaxV());
    tessellator.setColorOpaque_F(lightingInfo.colorRedBottomRight, lightingInfo.colorGreenBottomRight, lightingInfo.colorBlueBottomRight);
    tessellator.setBrightness(lightingInfo.brightnessBottomRight);
    tessellator.addVertexWithUV(x + 1, y, z + 1, icon.getMaxU(), icon.getMaxV());
    tessellator.setColorOpaque_F(lightingInfo.colorRedTopRight, lightingInfo.colorGreenTopRight, lightingInfo.colorBlueTopRight);
    tessellator.setBrightness(lightingInfo.brightnessTopRight);
    tessellator.addVertexWithUV(x + 1, y + 1, z + 1, icon.getMaxU(), icon.getMinV());
  }

  public static void renderFaceLightingDirecttion(ForgeDirection direction, Tessellator tessellator, Block block, int meta, IBlockAccess world, int x, int y, int z) {
    switch (direction) {
      case DOWN:
        renderFaceLightingYNeg(tessellator, block, meta, world, x, y, z);
        break;
      case UP:
        renderFaceLightingYPos(tessellator, block, meta, world, x, y, z);
        break;
      case NORTH:
        renderFaceLightingZNeg(tessellator, block, meta, world, x, y, z);
        break;
      case SOUTH:
        renderFaceLightingZPos(tessellator, block, meta, world, x, y, z);
        break;
      case WEST:
        renderFaceLightingXNeg(tessellator, block, meta, world, x, y, z);
        break;
      case EAST:
        renderFaceLightingXPos(tessellator, block, meta, world, x, y, z);
        break;
    }
  }

  public static void rebindUVsToIcon(ModelIEObj modelIEObj, IIcon icon) {
    for (GroupObject groupObject : modelIEObj.model.groupObjects) {
      float minU = icon.getInterpolatedU(0);
      float sizeU = icon.getInterpolatedU(16) - minU;
      float minV = icon.getInterpolatedV(0);
      float sizeV = icon.getInterpolatedV(16) - minV;
      float baseOffsetU = (16f / icon.getIconWidth()) * .0005F;
      float baseOffsetV = (16f / icon.getIconHeight()) * .0005F;
      for (Face face : groupObject.faces) {
        float averageU = 0F;
        float averageV = 0F;
        if (face.textureCoordinates != null && face.textureCoordinates.length > 0) {
          for (int i = 0; i < face.textureCoordinates.length; ++i) {
            averageU += face.textureCoordinates[i].u;
            averageV += face.textureCoordinates[i].v;
          }
          averageU = averageU / face.textureCoordinates.length;
          averageV = averageV / face.textureCoordinates.length;
        }

        for (int i = 0; i < face.vertices.length; ++i) {
          float offsetU, offsetV;
          TextureCoordinate textureCoordinate = face.textureCoordinates[i];
          offsetU = baseOffsetU;
          offsetV = baseOffsetV;
          if (face.textureCoordinates[i].u > averageU)
            offsetU = -offsetU;
          if (face.textureCoordinates[i].v > averageV)
            offsetV = -offsetV;

          face.textureCoordinates[i] = new TextureCoordinate(
             minU + sizeU * (textureCoordinate.u + offsetU),
             minV + sizeV * (textureCoordinate.v + offsetV)
          );
        }
      }
    }
  }
}
