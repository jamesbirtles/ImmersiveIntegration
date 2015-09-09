package unwrittenfun.minecraft.immersiveintegration.special.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import unwrittenfun.minecraft.immersiveintegration.special.Special;

import java.util.List;

public class HandlerRequestRenderType implements IMessageHandler<MessageRequestRenderType, IMessage> {
  @Override
  public IMessage onMessage(MessageRequestRenderType message, MessageContext ctx) {
    List players = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
    for (Object player : players) {
      if (player instanceof EntityPlayer) {
        EntityPlayer entityPlayer = (EntityPlayer) player;
        if (entityPlayer.getCommandSenderName().equalsIgnoreCase(message.playerName)) {
          int renderTypes[] = entityPlayer.getEntityData().getIntArray("IIRenderTypes");
          Special.network.wrapper.sendTo(new MessageRenderType(renderTypes, entityPlayer.getCommandSenderName()), ctx.getServerHandler().playerEntity);
          break;
        }
      }
    }
    return null;
  }
}