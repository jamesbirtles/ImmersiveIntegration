package unwrittenfun.minecraft.immersiveintegration.special.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class SpecialNetwork {
  public SimpleNetworkWrapper wrapper;
  protected int descriminator = 0;

  public SpecialNetwork() {
    this.wrapper = NetworkRegistry.INSTANCE.newSimpleChannel("iiSpecial");

    registerMessageClient(HandlerRenderType.class, MessageRenderType.class);
    registerMessageServer(HandlerRequestRenderType.class, MessageRequestRenderType.class);
  }

  protected <REQ extends IMessage, REPLY extends IMessage> void registerMessage(Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> messageType, Side side) {
    wrapper.registerMessage(messageHandler, messageType, descriminator, side);
    descriminator++;
  }

  protected <REQ extends IMessage, REPLY extends IMessage> void registerMessageClient(Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> messageType) {
    registerMessage(messageHandler, messageType, Side.CLIENT);
  }

  protected <REQ extends IMessage, REPLY extends IMessage> void registerMessageServer(Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> messageType) {
    registerMessage(messageHandler, messageType, Side.SERVER);
  }

  protected <REQ extends IMessage, REPLY extends IMessage> void registerMessageCommon(Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> messageType) {
    registerMessageClient(messageHandler, messageType);
    registerMessageServer(messageHandler, messageType);
  }
}
