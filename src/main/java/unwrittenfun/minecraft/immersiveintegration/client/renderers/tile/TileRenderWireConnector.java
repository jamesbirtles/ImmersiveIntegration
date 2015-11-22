package unwrittenfun.minecraft.immersiveintegration.client.renderers.tile;

import blusunrize.immersiveengineering.client.render.TileRenderImmersiveConnectable;
import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import unwrittenfun.minecraft.immersiveintegration.client.renderers.ModelIIObj;

public class TileRenderWireConnector extends TileRenderImmersiveConnectable {
  private final ModelIIObj model;

  public TileRenderWireConnector(ModelIIObj model) {
    this.model = model;
  }

  @Override
  public void renderStatic(TileEntity tile, Tessellator tes, Matrix4 translationMatrix, Matrix4 rotationMatrix) {
    translationMatrix.translate(.5, .5, .5);

    if (tile.hasWorldObj()) {
      switch (tile.getBlockMetadata()) {
        case 0:
          break;
        case 1:
          rotationMatrix.rotate(Math.toRadians(180), 0, 0, 1);
          break;
        case 2:
          rotationMatrix.rotate(Math.toRadians(90), 1, 0, 0);
          break;
        case 3:
          rotationMatrix.rotate(Math.toRadians(-90), 1, 0, 0);
          break;
        case 4:
          rotationMatrix.rotate(Math.toRadians(-90), 0, 0, 1);
          break;
        case 5:
          rotationMatrix.rotate(Math.toRadians(90), 0, 0, 1);
          break;
      }
    }

    model.render(tile, tes, translationMatrix, rotationMatrix, 0, false);
  }
}
