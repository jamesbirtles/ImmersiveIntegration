package unwrittenfun.minecraft.immersiveintegration.client.renderers;

import blusunrize.immersiveengineering.client.render.TileRenderIE;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import unwrittenfun.minecraft.immersiveintegration.tiles.TileExtendedPost;

public class TileRenderExtendedPost extends TileRenderIE {
  ModelIIObj model = new ModelIIObj("immersiveintegration:models/extendedPost.obj", IEContent.blockWoodenDevice);

  @Override
  public void renderDynamic(TileEntity tile, double x, double y, double z, float f) {

  }

  @Override
  public void renderStatic(TileEntity tile, Tessellator tes, Matrix4 translationMatrix, Matrix4 rotationMatrix) {
    boolean armLeft = false;
    boolean armRight = false;
    boolean rotate = false;
    if (tile.getWorldObj() != null) {
      TileEntity tileX0 = tile.getWorldObj().getTileEntity(tile.xCoord + 1, tile.yCoord + 1, tile.zCoord);
      TileEntity tileX1 = tile.getWorldObj().getTileEntity(tile.xCoord - 1, tile.yCoord + 1, tile.zCoord);
      TileEntity tileZ0 = tile.getWorldObj().getTileEntity(tile.xCoord, tile.yCoord + 1, tile.zCoord + 1);
      TileEntity tileZ1 = tile.getWorldObj().getTileEntity(tile.xCoord, tile.yCoord + 1, tile.zCoord - 1);
      if (tileX0 instanceof TileExtendedPost && tileX0.getBlockMetadata() == 5) armLeft = true;
      if (tileX1 instanceof TileExtendedPost && tileX1.getBlockMetadata() == 4) armRight = true;
      if (tileZ0 instanceof TileExtendedPost && tileZ0.getBlockMetadata() == 3) {
        armLeft = true;
        rotate = true;
      }
      if (tileZ1 instanceof TileExtendedPost && tileZ1.getBlockMetadata() == 2) {
        armRight = true;
        rotate = true;
      }
    } else {
      armRight = true;
      armLeft = true;
    }

    translationMatrix.translate(.5, 0, .5);
    if (rotate) rotationMatrix.rotate(Math.toRadians(-90), 0.0, 1.0, 0.0);

    String[] parts = armRight && armLeft ? new String[] { "Base", "Arm_right", "Arm_left" } : armRight ? new String[] { "Base", "Arm_right" } : armLeft ? new String[] { "Base", "Arm_left" } : new String[] { "Base" };
    model.render(tile, tes, translationMatrix, rotationMatrix, true, false, parts);
  }
}
