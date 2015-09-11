package unwrittenfun.minecraft.immersiveintegration.gui;

import net.minecraft.entity.player.EntityPlayer;

public interface IGuiProvider {
  Object getContainer(int id, EntityPlayer player);

  Object getGuiContainer(int id, EntityPlayer player);
}
