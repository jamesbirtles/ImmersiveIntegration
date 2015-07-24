package unwrittenfun.minecraft.immersiveintegration.client.renderers;

import blusunrize.immersiveengineering.client.ClientEventHandler;
import blusunrize.immersiveengineering.client.render.TileRenderIE;
import blusunrize.immersiveengineering.client.render.TileRenderImmersiveConnectable;
import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import unwrittenfun.minecraft.immersiveintegration.blocks.IIBlocks;

public class TileRenderMETransformer extends TileRenderIE {
  private final ModelIIObj model = new ModelIIObj("immersiveengineering:models/transformerHV.obj", IIBlocks.meTransformer);

  @Override
  public void renderDynamic(TileEntity tile, double x, double y, double z, float f) {
    int meta = tile.hasWorldObj() ? tile.getBlockMetadata() : 8;
    if ((meta & 8) == 8) {
      ClientEventHandler.renderAllIEConnections(f);
    }
  }

  @Override
  public void renderStatic(TileEntity tile, Tessellator tes, Matrix4 translationMatrix, Matrix4 rotationMatrix) {
    int meta = tile.hasWorldObj() ? tile.getBlockMetadata() : 8;
    if ((meta & 8) == 8) {
      switch (meta & 7) {
        case 2:
          rotationMatrix.rotate(Math.toRadians(180), 0, 1, 0);
          break;
        case 3:
          break;
        case 4:
          rotationMatrix.rotate(Math.toRadians(-90), 0, 1, 0);
          break;
        case 5:
          rotationMatrix.rotate(Math.toRadians(90), 0, 1, 0);
          break;
      }

      translationMatrix.translate(.5, .5, .5);
      if (!tile.hasWorldObj()) {
        translationMatrix.translate(0, .4, 0);
        GL11.glScaled(0.5f, 0.5f, 0.5f);
      }
      model.render(tile, tes, translationMatrix, rotationMatrix, true, false, "Base", "Connector_Right");
    }
  }
}
