package unwrittenfun.minecraft.immersiveintegration.client.renderers.tile;

import blusunrize.immersiveengineering.client.models.ModelIEObj;
import blusunrize.immersiveengineering.client.render.TileRenderImmersiveConnectable;
import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemDye;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;
import unwrittenfun.minecraft.immersiveintegration.ModInfo;
import unwrittenfun.minecraft.immersiveintegration.blocks.BlockRedstoneWireConnector;
import unwrittenfun.minecraft.immersiveintegration.blocks.IIBlocks;
import unwrittenfun.minecraft.immersiveintegration.client.renderers.ModelIIObj;
import unwrittenfun.minecraft.immersiveintegration.tiles.TileRedstoneWireConnector;

public class TileRenderRedstoneWireConnector extends TileRenderImmersiveConnectable {
  private static final IModelCustom modelChannel = AdvancedModelLoader.loadModel(new ResourceLocation(ModInfo.MOD_ID, "models/redstoneWireConnectorChannel.obj"));
  private static final ResourceLocation channelTexture = new ResourceLocation(ModInfo.MOD_ID, "textures/blocks/redstoneWireConnectorChannel.png");
  private final ModelIIObj modelOutput = new ModelIIObj("immersiveintegration:models/redstoneWireConnector.obj", IIBlocks.redstoneWireConnector);
  private final ModelIEObj modelInput = new ModelIEObj("immersiveintegration:models/redstoneWireConnector.obj") {
    @Override
    public IIcon getBlockIcon(String groupName) {
      return BlockRedstoneWireConnector.inputIcon;
    }
  };

  @Override
  public void renderDynamic(TileEntity tile, double x, double y, double z, float f) {
    super.renderDynamic(tile, x, y, z, f);

    if (tile instanceof TileRedstoneWireConnector) {
      GL11.glPushMatrix();

      int hex = ItemDye.field_150922_c[15 - ((TileRedstoneWireConnector) tile).redstoneChannel];
      int r = (hex & 0xFF0000) >> 16;
      int g = (hex & 0xFF00) >> 8;
      int b = (hex & 0xFF);

      GL11.glColor3f(r / 255f, g / 255f, b / 255f);
      GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);

      if (tile.hasWorldObj()) {
        switch (tile.getBlockMetadata()) {
          case 0:
            break;
          case 1:
            GL11.glRotatef(180, 0, 0, 1);
            break;
          case 2:
            GL11.glRotatef(90, 1, 0, 0);
            break;
          case 3:
            GL11.glRotatef(-90, 1, 0, 0);
            break;
          case 4:
            GL11.glRotatef(-90, 0, 0, 1);
            break;
          case 5:
            GL11.glRotatef(90, 0, 0, 1);
            break;
        }
      }

      Minecraft.getMinecraft().renderEngine.bindTexture(channelTexture);
      modelChannel.renderAll();
      GL11.glPopMatrix();
    }
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

    if (tile.hasWorldObj()) {
      if (tile instanceof TileRedstoneWireConnector && ((TileRedstoneWireConnector) tile).isInput()) {
        modelInput.render(tile, tes, translationMatrix, rotationMatrix, 0, false, "connectorMV");
      } else {
        modelOutput.render(tile, tes, translationMatrix, rotationMatrix, 0, false, "connectorMV");
      }
    } else {
      modelOutput.render(tile, tes, translationMatrix, rotationMatrix, 0, false);
    }
  }
}
