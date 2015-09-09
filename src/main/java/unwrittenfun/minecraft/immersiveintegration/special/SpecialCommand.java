package unwrittenfun.minecraft.immersiveintegration.special;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import unwrittenfun.minecraft.immersiveintegration.special.network.MessageRenderType;

import java.util.ArrayList;
import java.util.List;

public class SpecialCommand implements ICommand {
  List<String> typesList;

  public SpecialCommand() {
    typesList = new ArrayList<>();
    for (SpecialRenderType renderType : SpecialRenderType.values()) {
      typesList.add(renderType.toString().toLowerCase());
    }
  }

  @Override
  public String getCommandName() {
    return "iis";
  }

  @Override
  public String getCommandUsage(ICommandSender p_71518_1_) {
    return "iis <special>";
  }

  @Override
  public List getCommandAliases() {
    return new ArrayList();
  }

  @Override
  public void processCommand(ICommandSender sender, String[] args) {
    if (sender instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) sender;
      if (typesList.contains(args[0])) {
        SpecialRenderType renderType = SpecialRenderType.valueOf(args[0].toUpperCase());
        renderType.toggle(player);
        if (!player.worldObj.isRemote)
          Special.network.wrapper.sendToAll(new MessageRenderType(player.getEntityData().getIntArray("IIRenderTypes"), player.getCommandSenderName()));
        sender.addChatMessage(new ChatComponentText("Toggled " + renderType.friendlyName));
      } else {
        sender.addChatMessage(new ChatComponentText(args[0] + " is not a valid special type."));
      }
    } else {
      sender.addChatMessage(new ChatComponentText("This command can only be used by players."));
    }
  }

  @Override
  public boolean canCommandSenderUseCommand(ICommandSender sender) {
    return true;
  }

  @Override
  public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_) {
    return typesList;
  }

  @Override
  public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
    return false;
  }

  @Override
  public int compareTo(Object o) {
    return 0;
  }
}
