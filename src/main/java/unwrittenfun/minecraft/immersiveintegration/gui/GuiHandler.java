package unwrittenfun.minecraft.immersiveintegration.gui;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {
  @Override
  public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    TileEntity tileEntity = world.getTileEntity(x, y, z);
    if (tileEntity instanceof IGuiProvider) {
      return ((IGuiProvider) tileEntity).getContainer(ID, player);
    }
    return null;
  }

  @Override
  public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    TileEntity tileEntity = world.getTileEntity(x, y, z);
    if (tileEntity instanceof IGuiProvider) {
      return ((IGuiProvider) tileEntity).getGuiContainer(ID, player);
    }
    return null;
  }
}
