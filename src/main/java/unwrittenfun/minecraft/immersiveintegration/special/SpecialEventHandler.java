package unwrittenfun.minecraft.immersiveintegration.special;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;
import unwrittenfun.minecraft.immersiveintegration.ModInfo;

public class SpecialEventHandler {
  public ResourceLocation loveButt = new ResourceLocation(ModInfo.MOD_ID, "textures/specials/LoveButt.png");
  public ResourceLocation connectorTexture = new ResourceLocation(ModInfo.MOD_ID, "textures/blocks/meDenseWireConnector.png");
  public IModelCustom connectorModel = AdvancedModelLoader.loadModel(new ResourceLocation("immersiveintegration", "models/meDenseWireConnector.obj"));
  public ResourceLocation redstonePin = new ResourceLocation(ModInfo.MOD_ID, "textures/items/iiWireCoilRedstone.png");

  @SubscribeEvent
  public void renderPlayerSpecial(RenderPlayerEvent.Specials.Post event) {
    if (SpecialRenderType.CONNECTOR_HEAD.shouldRenderForPlayer(event.entityPlayer)) {
      GL11.glPushMatrix();
      event.renderer.modelBipedMain.bipedHead.postRender(0.0625F);
      GL11.glRotatef(180, 1f, 0, 0);
      GL11.glTranslatef(0, 1f, 0f);

      Minecraft.getMinecraft().renderEngine.bindTexture(connectorTexture);
      connectorModel.renderAll();
      GL11.glPopMatrix();
    }

    if (SpecialRenderType.LOVEBUTT.shouldRenderForPlayer(event.entityPlayer)) {
      GL11.glPushMatrix();

      event.renderer.modelBipedMain.bipedBody.postRender(0.0625F);
      GL11.glRotatef(180, 0, 0, 1f);
      GL11.glScalef(0.5f, 0.5f, 0.5f);
      GL11.glTranslatef(-0.5f, -1.5f, 0.26f);

      Tessellator tess = Tessellator.instance;
      tess.startDrawingQuads();

      Minecraft.getMinecraft().renderEngine.bindTexture(loveButt);
      tess.addVertexWithUV(0, 0, 0, 0, 1);
      tess.addVertexWithUV(1, 0, 0, 1, 1);
      tess.addVertexWithUV(1, 1, 0, 1, 0);
      tess.addVertexWithUV(0, 1, 0, 0, 0);

      tess.draw();

      GL11.glPopMatrix();
    }

    if (SpecialRenderType.REDSTONE_PIN.shouldRenderForPlayer(event.entityPlayer)) {
      GL11.glPushMatrix();

      event.renderer.modelBipedMain.bipedBody.postRender(0.0625F);
      GL11.glRotatef(180, 0, 0, 1f);
      GL11.glScalef(0.15f, 0.15f, 0.15f);
      GL11.glTranslatef(-1.4f, -1.4f, -0.84f);

      Tessellator tess = Tessellator.instance;
      tess.startDrawingQuads();

      Minecraft.getMinecraft().renderEngine.bindTexture(redstonePin);
      tess.addVertexWithUV(0, 0, 0, 1, 0);
      tess.addVertexWithUV(1, 0, 0, 0, 0);
      tess.addVertexWithUV(1, 1, 0, 0, 1);
      tess.addVertexWithUV(0, 1, 0, 1, 1);

      tess.draw();

      GL11.glPopMatrix();
    }
  }
}
