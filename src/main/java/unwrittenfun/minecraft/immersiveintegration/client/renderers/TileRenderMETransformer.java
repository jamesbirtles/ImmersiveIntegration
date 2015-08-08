package unwrittenfun.minecraft.immersiveintegration.client.renderers;

import blusunrize.immersiveengineering.client.ClientEventHandler;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.render.TileRenderIE;
import blusunrize.immersiveengineering.client.render.TileRenderImmersiveConnectable;
import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;
import unwrittenfun.minecraft.immersiveintegration.ImmersiveIntegration;
import unwrittenfun.minecraft.immersiveintegration.ModInfo;
import unwrittenfun.minecraft.immersiveintegration.blocks.IIBlocks;

public class TileRenderMETransformer extends TileRenderIE {
  private final ModelIIObj modelItem = new ModelIIObj("immersiveengineering:models/transformerHV.obj", IIBlocks.meTransformer);
  private final IModelCustom model = ClientUtils.getModel("immersiveengineering:models/transformerHV.obj");

  @Override
  public void renderDynamic(TileEntity tile, double x, double y, double z, float f) {
    int meta = tile.hasWorldObj() ? tile.getBlockMetadata() : 8;
    if ((meta & 8) == 8) {
      ClientEventHandler.renderAllIEConnections(f);

      GL11.glPushMatrix();

      GL11.glTranslated(x, y, z);
      GL11.glTranslated(0.5, 0.5, 0.5);
      switch (meta & 7) {
        case 2:
          GL11.glRotatef(180f, 0, 1, 0);
          break;
        case 3:
          break;
        case 4:
          GL11.glRotatef(-90f, 0, 1, 0);
          break;
        case 5:
          GL11.glRotatef(90f, 0, 1, 0);
          break;
      }

      int l = tile.getWorldObj().getLightBrightnessForSkyBlocks(tile.xCoord, tile.yCoord + 1, tile.zCoord, 0);
      int l1 = l % 65536;
      int l2 = l / 65536;
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) l1, (float) l2);

      ClientUtils.bindTexture(ModInfo.MOD_ID + ":textures/blocks/meTransformer.png");
      model.renderOnly("Base", "Connector_Right");

      GL11.glPopMatrix();
    }
  }

  @Override
  public void renderStatic(TileEntity tile, Tessellator tes, Matrix4 translationMatrix, Matrix4 rotationMatrix) {
    if (!tile.hasWorldObj()) {

      translationMatrix.translate(.5, .5, .5);
      translationMatrix.translate(0, .4, 0);
      GL11.glScaled(0.5f, 0.5f, 0.5f);
      modelItem.render(tile, tes, translationMatrix, rotationMatrix, true, false, "Base", "Connector_Right");
    }
  }
}
