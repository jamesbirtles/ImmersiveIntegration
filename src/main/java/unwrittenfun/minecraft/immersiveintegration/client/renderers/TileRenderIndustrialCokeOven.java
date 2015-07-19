package unwrittenfun.minecraft.immersiveintegration.client.renderers;

import blusunrize.immersiveengineering.client.render.TileRenderIE;
import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;
import unwrittenfun.minecraft.immersiveintegration.ModInfo;

public class TileRenderIndustrialCokeOven extends TileRenderIE {
  private static final IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation(ModInfo.MOD_ID, "models/industrialCokeOven.obj"));
  private static final ResourceLocation texture = new ResourceLocation(ModInfo.MOD_ID, "textures/blocks/industrialCokeOvenFormed.png");

  @Override
  public void renderDynamic(TileEntity tile, double x, double y, double z, float f) {
    if (tile.getBlockMetadata() == 1 || tile.getBlockMetadata() == 2) {
      GL11.glPushMatrix();
      GL11.glColor4f(1, 1, 1, 1);

      GL11.glTranslated(x + 0.5, y - 1, z + 0.5);

      if (tile.getBlockMetadata() == 1) {
        GL11.glRotatef(90f, 0, 1, 0);
      }

      Minecraft.getMinecraft().renderEngine.bindTexture(texture);
      model.renderAll();

      GL11.glPopMatrix();
    }
  }

  @Override
  public void renderStatic(TileEntity tile, Tessellator tes, Matrix4 translationMatrix, Matrix4 rotationMatrix) {
  }
}
