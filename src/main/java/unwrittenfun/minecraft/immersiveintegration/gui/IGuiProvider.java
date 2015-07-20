package unwrittenfun.minecraft.immersiveintegration.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public interface IGuiProvider {
  Object getContainer(int id, EntityPlayer player);
  Object getGuiContainer(int id, EntityPlayer player);
}
