package unwrittenfun.minecraft.immersiveintegration.client.renderers.tile;

import blusunrize.immersiveengineering.client.render.TileRenderIE;
import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import unwrittenfun.minecraft.immersiveintegration.ModInfo;
import unwrittenfun.minecraft.immersiveintegration.blocks.IIBlocks;
import unwrittenfun.minecraft.immersiveintegration.client.renderers.ModelIIObj;
import unwrittenfun.minecraft.immersiveintegration.tiles.TileMEDenseTransformer;

public class TileRenderMETransformer extends TileRenderIE {
  private final ModelIIObj modelItem = new ModelIIObj(ModInfo.MOD_ID + ":models/meTransformer.obj", IIBlocks.meTransformer);
  private final ModelIIObj modelItemDense = new ModelIIObj(ModInfo.MOD_ID + ":models/meTransformer.obj", IIBlocks.meDenseTransformer);
  private final ModelIIObj modelItemConnector = new ModelIIObj("immersiveengineering:models/connectorMV.obj", IIBlocks.meWireConnector);
  private final ModelIIObj modelItemDenseConnector = new ModelIIObj(ModInfo.MOD_ID + ":models/meDenseWireConnector.obj", IIBlocks.meDenseWireConnector);

  @Override
  public void renderDynamic(TileEntity tile, double x, double y, double z, float f) {
  }

  @Override
  public void renderStatic(TileEntity tile, Tessellator tes, Matrix4 translationMatrix, Matrix4 rotationMatrix) {
    int meta = tile.hasWorldObj() ? tile.getBlockMetadata() : 8;
    if ((meta & 8) == 8) {
      translationMatrix.translate(.5, .5, .5);
      if (!tile.hasWorldObj()) {
        translationMatrix.translate(0, .35, 0);
        GL11.glScaled(0.48f, 0.48f, 0.48f);
      } else {
        switch (meta & 7) {
          case 2:
            rotationMatrix.rotate(Math.PI, 0, 1, 0);
            break;
          case 3:
            break;
          case 4:
            rotationMatrix.rotate(-Math.PI / 2, 0, 1, 0);
            break;
          case 5:
            rotationMatrix.rotate(Math.PI / 2, 0, 1, 0);
            break;
        }
      }

      if (tile instanceof TileMEDenseTransformer) {
        modelItemDense.render(tile, tes, translationMatrix, rotationMatrix, 0, false);
      } else {
        modelItem.render(tile, tes, translationMatrix, rotationMatrix, 0, false);
      }

      translationMatrix.translate(0, 0.875, 0);
      if (tile instanceof TileMEDenseTransformer) {
        modelItemDenseConnector.render(tile, tes, translationMatrix, rotationMatrix, 0, false);
      } else {
        modelItemConnector.render(tile, tes, translationMatrix, rotationMatrix, 0, false);
      }
    }
  }
}
