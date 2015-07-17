package unwrittenfun.minecraft.immersiveintegration.special;

import net.minecraft.entity.player.EntityPlayer;
import org.apache.commons.lang3.ArrayUtils;
import unwrittenfun.minecraft.immersiveintegration.special.network.MessageRequestRenderType;

public enum SpecialRenderType {
  CONNECTOR_HEAD("Connector Head"), LOVEBUTT("LoveButt"), REDSTONE_PIN("Redstone Pin");

  public final String friendlyName;

  SpecialRenderType(String friendlyName) {
    this.friendlyName = friendlyName;
  }

  public boolean shouldRenderForPlayer(EntityPlayer player) {
    if (!player.getEntityData().hasKey("IIRenderTypes")) {
      player.getEntityData().setIntArray("IIRenderTypes", new int[0]);
      Special.network.wrapper.sendToServer(new MessageRequestRenderType(player.getCommandSenderName()));
      return false;
    }
    return ArrayUtils.contains(player.getEntityData().getIntArray("IIRenderTypes"), ordinal());
  }

  public void toggle(EntityPlayer player) {
    int[] renderTypes = player.getEntityData().getIntArray("IIRenderTypes");
    if (ArrayUtils.contains(renderTypes, ordinal())) {
      renderTypes = ArrayUtils.removeElement(renderTypes, ordinal());
    } else {
      renderTypes = ArrayUtils.add(renderTypes, ordinal());
    }
    player.getEntityData().setIntArray("IIRenderTypes", renderTypes);
  }
}
