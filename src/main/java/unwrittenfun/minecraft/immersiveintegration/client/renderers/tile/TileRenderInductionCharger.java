package unwrittenfun.minecraft.immersiveintegration.client.renderers.tile;

import blusunrize.immersiveengineering.client.render.TileRenderIE;
import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.model.obj.Vertex;
import org.lwjgl.opengl.GL11;
import unwrittenfun.minecraft.immersiveintegration.blocks.IIBlocks;
import unwrittenfun.minecraft.immersiveintegration.client.renderers.ModelIIObj;
import unwrittenfun.minecraft.immersiveintegration.tiles.TileInductionChargerLV;

public class TileRenderInductionCharger extends TileRenderIE {
  protected ModelIIObj model;

  public TileRenderInductionCharger(int meta) {
    this.model = new ModelIIObj("immersiveintegration:models/inductionCharger.obj", IIBlocks.inductionCharger, meta);
  }

  @Override
  public void renderDynamic(TileEntity tile, double x, double y, double z, float f) {
    if (tile.hasWorldObj()) {
      if (tile instanceof TileInductionChargerLV) {
        TileInductionChargerLV charger = (TileInductionChargerLV) tile;
        if (charger.chargingStackEntity != null) {
          GL11.glPushMatrix();
          GL11.glTranslatef((float) x + 0.5f, (float) (y + 0.6f), (float) z + 0.109f);
          GL11.glRotatef(90f, 1, 0, 0);
          GL11.glRotatef(180f, 0, 1, 0);
          GL11.glScalef(1.94f, 1.94f, 1.94f);
          RenderItem.renderInFrame = true;
          RenderManager.instance.renderEntityWithPosYaw(charger.chargingStackEntity, 0D, 0D, 0D, 0F, 0f);
          RenderItem.renderInFrame = false;
          GL11.glPopMatrix();
        }
      }
    }
  }

  @Override
  public void renderStatic(TileEntity tile, Tessellator tes, Matrix4 translationMatrix, Matrix4 rotationMatrix) {
    translationMatrix.translate(.5, .5, .5);

    if (!tile.hasWorldObj()) {
      translationMatrix.scale(new Vertex(0.80f, 0.8f, 0.8f));
    }

    model.render(tile, tes, translationMatrix, rotationMatrix, 0, false);
  }
}
