package unwrittenfun.minecraft.immersiveintegration.special.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class MessageRenderType implements IMessage {
  int[] renderTypes;
  String playerName;

  public MessageRenderType() {

  }

  public MessageRenderType(int[] renderTypes, String playerName) {
    this.renderTypes = renderTypes;
    this.playerName = playerName;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    renderTypes = new int[buf.readInt()];
    for (int i = 0; i < renderTypes.length; i++) {
      renderTypes[i] = buf.readInt();
    }
    playerName = ByteBufUtils.readUTF8String(buf);
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeInt(renderTypes.length);
    for (int i = 0; i < renderTypes.length; i++) {
      buf.writeInt(renderTypes[i]);
    }
    ByteBufUtils.writeUTF8String(buf, playerName);
  }
}
