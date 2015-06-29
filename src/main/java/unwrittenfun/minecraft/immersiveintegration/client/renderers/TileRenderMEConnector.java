package unwrittenfun.minecraft.immersiveintegration.client.renderers;

import blusunrize.immersiveengineering.client.models.ModelIEObj;
import blusunrize.immersiveengineering.client.render.TileRenderImmersiveConnectable;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.metal.BlockMetalDevices;
import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import unwrittenfun.minecraft.immersiveintegration.ModInfo;
import unwrittenfun.minecraft.immersiveintegration.blocks.IIBlocks;
import unwrittenfun.minecraft.immersiveintegration.tiles.TileMEWireConnector;

public class TileRenderMEConnector extends TileRenderImmersiveConnectable {
  ModelIEObj model = new ModelIEObj("immersiveengineering:models/connectorMV.obj") {
  @Override
    public IIcon getBlockIcon() {
      return IIBlocks.meWireConnector.getIcon(0, 0);
    }
  };

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

    model.render(tile, tes, translationMatrix, rotationMatrix, true, false);
  }
}
