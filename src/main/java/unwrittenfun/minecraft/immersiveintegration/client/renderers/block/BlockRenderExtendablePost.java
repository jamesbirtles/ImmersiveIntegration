package unwrittenfun.minecraft.immersiveintegration.client.renderers.block;

import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;
import unwrittenfun.minecraft.immersiveintegration.ModInfo;
import unwrittenfun.minecraft.immersiveintegration.blocks.BlockExtendablePost;
import unwrittenfun.minecraft.immersiveintegration.blocks.IIBlocks;
import unwrittenfun.minecraft.immersiveintegration.client.IIRenderIDs;
import unwrittenfun.minecraft.immersiveintegration.client.renderers.ModelIIObj;

import java.util.ArrayList;

public class BlockRenderExtendablePost implements ISimpleBlockRenderingHandler {
  public static final ModelIIObj model = new ModelIIObj(ModInfo.MOD_ID + ":models/extendablePost.obj", IIBlocks.extendablePost);
  public static final TileEntity fakeTile = new TileEntity();

  @Override
  public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
    GL11.glPushMatrix();
    GL11.glTranslatef(0f, -0.5f, 0f);
    Tessellator.instance.startDrawingQuads();

    model.render(new TileEntity(), Tessellator.instance, new Matrix4(), new Matrix4(), 0, false, "Base");

    Tessellator.instance.draw();
    GL11.glPopMatrix();
  }

  @Override
  public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
    Matrix4 translationMatrix = new Matrix4();
    Matrix4 rotationMatrix = new Matrix4();
    translationMatrix.translate(x, y, z);
    translationMatrix.translate(0.5, 0, 0.5);

    int meta = world.getBlockMetadata(x, y, z);

    fakeTile.setWorldObj(Minecraft.getMinecraft().theWorld);
    fakeTile.xCoord = x;
    fakeTile.yCoord = y;
    fakeTile.zCoord = z;

    ArrayList<String> parts = new ArrayList<>();

    if (meta > 2) {
//      parts.add("Arm_Up");
      if (BlockExtendablePost.canArmConnectToBlock(Minecraft.getMinecraft().theWorld, x, y + 1, z, false)) {
        parts.add("Arm_Up");
      }
      if (BlockExtendablePost.canArmConnectToBlock(Minecraft.getMinecraft().theWorld, x, y - 1, z, true)) {
        parts.add("Arm_Down");
      }
      if (parts.size() == 0) {
        parts.add("Arm_Up");
      }
    }

    switch (meta) {
      case 0:
        parts.add("Base");
        break;
      case 1:
        parts.add("Post");
        break;
      case 2:
        parts.add("Topper");
        break;
      case 3:
        translationMatrix.translate(0, 0, 0.5);
        rotationMatrix.rotate(-Math.PI / 2f, 0, 1, 0);
        break;
      case 4:
        translationMatrix.translate(0, 0, -0.5);
        rotationMatrix.rotate(Math.PI / 2f, 0, 1, 0);
        break;
      case 5:
        translationMatrix.translate(0.5, 0, 0);
        break;
      case 6:
        translationMatrix.translate(-0.5, 0, 0);
        rotationMatrix.rotate(Math.PI, 0, 1, 0);
        break;
    }

    if (parts.size() > 0) {
      model.render(fakeTile, Tessellator.instance, translationMatrix, rotationMatrix, 0, false, parts.toArray(new String[parts.size()]));
    }

    return true;
  }

  @Override
  public boolean shouldRender3DInInventory(int modelId) {
    return true;
  }

  @Override
  public int getRenderId() {
    return IIRenderIDs.EXTENDABLE_POST;
  }
}
