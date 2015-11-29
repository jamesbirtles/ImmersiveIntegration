package unwrittenfun.minecraft.immersiveintegration.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;

public class PlayerUtils {
  public static Item getHeldItem(EntityPlayer player) {
    if (player.getHeldItem() != null) {
      return player.getHeldItem().getItem();
    }
    return null;
  }
}
