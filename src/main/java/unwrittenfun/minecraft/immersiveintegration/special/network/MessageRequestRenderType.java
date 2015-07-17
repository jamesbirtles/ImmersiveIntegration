package unwrittenfun.minecraft.immersiveintegration.special.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class MessageRequestRenderType implements IMessage {
  String playerName;

  public MessageRequestRenderType() {

  }

  public MessageRequestRenderType(String playerName) {
    this.playerName = playerName;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    playerName = ByteBufUtils.readUTF8String(buf);
  }

  @Override
  public void toBytes(ByteBuf buf) {
    ByteBufUtils.writeUTF8String(buf, playerName);
  }
}
