package unwrittenfun.minecraft.immersiveintegration.special.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class HandlerRenderType implements IMessageHandler<MessageRenderType, IMessage> {
  @Override
  public IMessage onMessage(MessageRenderType message, MessageContext ctx) {
    List players = Minecraft.getMinecraft().theWorld.playerEntities;
    for (Object player : players) {
      if (player instanceof EntityPlayer) {
        EntityPlayer entityPlayer = (EntityPlayer) player;
        if (entityPlayer.getCommandSenderName().equalsIgnoreCase(message.playerName)) {
          entityPlayer.getEntityData().setIntArray("IIRenderTypes", message.renderTypes);
          break;
        }
      }
    }
    return null;
  }
}