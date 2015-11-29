package unwrittenfun.minecraft.immersiveintegration.client.renderers.tile;

import blusunrize.immersiveengineering.client.render.TileRenderImmersiveConnectable;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;
import unwrittenfun.minecraft.immersiveintegration.ModInfo;
import unwrittenfun.minecraft.immersiveintegration.blocks.IIBlocks;
import unwrittenfun.minecraft.immersiveintegration.client.renderers.ModelIIObj;
import unwrittenfun.minecraft.immersiveintegration.tiles.TileWallConnector;
import unwrittenfun.minecraft.immersiveintegration.utils.MathUtils;
import unwrittenfun.minecraft.immersiveintegration.utils.RenderUtils;

public class TileRenderWallConnector extends TileRenderImmersiveConnectable {
  public ModelIIObj model = new ModelIIObj(ModInfo.MOD_ID + ":models/wallConnector.obj", IIBlocks.wallConnector);
  public ModelIIObj modelCover = new ModelIIObj(ModInfo.MOD_ID + ":models/wallConnector.obj", IEContent.blockWoodenDecoration);

  @Override
  public void renderStatic(TileEntity tile, Tessellator tes, Matrix4 translationMatrix, Matrix4 rotationMatrix) {
    if (!(tile instanceof TileWallConnector)) {
      return;
    }

    translationMatrix.translate(.5, .5, .5);

    if (!tile.hasWorldObj()) {
      GL11.glScalef(0.78f, 0.78f, 0.78f);
      translationMatrix.translate(0, -0.07, 0);
    }

    rotationMatrix.rotate(Math.PI, 0, 1, 0);
    for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
      Matrix4 rotation = new Matrix4(rotationMatrix);
      MathUtils.applyRotation(direction, rotation);
//      RenderBlocks.getInstance().setRenderBounds(0, 0, 0, 1, 1, 1);
      model.render(tile, tes, translationMatrix, rotation, 0, false, "Connector");

//      if (tile.getSideStack(direction.ordinal()) != null) {
//
//      }
      modelCover.rebindModel();
      RenderUtils.rebindUVsToIcon(modelCover, IEContent.blockWoodenDecoration.getIcon(direction.ordinal(),0));

//      RenderBlocks.getInstance().setRenderBounds(0, 0, 0, 1, 1, 1);
      modelCover.render(tile, tes, translationMatrix, rotation, 0, false, "Cover");

//      if (tile.hasWorldObj()) {
//        RenderUtils.renderFaceLightingDirecttion(direction, tes, IEContent.blockWoodenDecoration, 0, tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord);
//      }
    }
  }
}
