package unwrittenfun.minecraft.immersiveintegration.client.renderers.tile;

import blusunrize.immersiveengineering.client.render.TileRenderIE;
import blusunrize.immersiveengineering.client.render.TileRenderPost;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import unwrittenfun.minecraft.immersiveintegration.client.renderers.ModelIIObj;
import unwrittenfun.minecraft.immersiveintegration.tiles.TileExtendedPost;

import java.util.ArrayList;

public class TileRenderExtendedPost extends TileRenderIE {
  ModelIIObj model = new ModelIIObj("immersiveintegration:models/extendedPost.obj", IEContent.blockWoodenDevice);

  @Override
  public void renderDynamic(TileEntity tile, double x, double y, double z, float f) {

  }

  @Override
  public void renderStatic(TileEntity tile, Tessellator tes, Matrix4 translationMatrix, Matrix4 rotationMatrix) {
    ArrayList<String> parts = new ArrayList<String>();
    parts.add("Base");

    boolean rotate = false;
    float fr = 0;
    if (tile.getWorldObj() != null) {
      for (int i = 0; i < 4; i++) {
        rotate |= handleArms(tile.getWorldObj().getTileEntity(tile.xCoord + (i == 2 ? -1 : i == 3 ? 1 : 0), tile.yCoord + 1, tile.zCoord + (i == 0 ? -1 : i == 1 ? 1 : 0)), 2 + i, fr, parts);
      }
    } else {
      parts.add("Arm_right_u");
      parts.add("Arm_left_u");
    }

    translationMatrix.translate(.5, 0, .5);
    if (rotate) rotationMatrix.rotate(Math.toRadians(-90), 0.0, 1.0, 0.0);

    model.render(tile, tes, translationMatrix, rotationMatrix, 0, false, parts.toArray(new String[parts.size()]));
  }

  public boolean handleArms(TileEntity arm, int checkType, float rotate, ArrayList<String> parts) {
    if (arm instanceof TileExtendedPost && arm.getBlockMetadata() == checkType) {
      String dir = checkType % 2 == 1 ? "left" : "right";
      if (TileRenderPost.canArmConnectToBlock(arm.getWorldObj(), arm.xCoord, arm.yCoord - 1, arm.zCoord, true)) {
        parts.add("Arm_" + dir + "_d");
        if (TileRenderPost.canArmConnectToBlock(arm.getWorldObj(), arm.xCoord, arm.yCoord + 1, arm.zCoord, false))
          parts.add("Arm_" + dir + "_u");
      } else
        parts.add("Arm_" + dir + "_u");
      return checkType < 4;
    }
    return false;
  }
}
