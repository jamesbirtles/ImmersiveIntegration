package unwrittenfun.minecraft.immersiveintegration.client;

import blusunrize.immersiveengineering.api.energy.WireType;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Lib;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.oredict.OreDictionary;
import unwrittenfun.minecraft.immersiveintegration.items.IIItems;

public class RenderEventsHandler {
  @SubscribeEvent
  public void postRenderGameOverlay(RenderGameOverlayEvent.Post event) {
    if (ClientUtils.mc().thePlayer != null && ClientUtils.mc().thePlayer.getCurrentEquippedItem() != null && event.type == RenderGameOverlayEvent.ElementType.TEXT) {
      if (OreDictionary.itemMatches(new ItemStack(IIItems.aeWireCoil, 1, OreDictionary.WILDCARD_VALUE), ClientUtils.mc().thePlayer.getCurrentEquippedItem(), false) || OreDictionary.itemMatches(new ItemStack(IIItems.iiWireCoil, 1, OreDictionary.WILDCARD_VALUE), ClientUtils.mc().thePlayer.getCurrentEquippedItem(), false)) {
        if (ItemNBTHelper.hasKey(ClientUtils.mc().thePlayer.getCurrentEquippedItem(), "linkingPos")) {
          int[] link = ItemNBTHelper.getIntArray(ClientUtils.mc().thePlayer.getCurrentEquippedItem(), "linkingPos");
          if (link != null && link.length > 3) {
            String s = StatCollector.translateToLocalFormatted(Lib.DESC_INFO + "attachedTo", link[1], link[2], link[3]);
            ClientUtils.font().drawString(s, event.resolution.getScaledWidth() / 2 - ClientUtils.font().getStringWidth(s) / 2, event.resolution.getScaledHeight() - GuiIngameForge.left_height - 15, WireType.ELECTRUM.getColour(null), true);
          }
        }
      }
    }
  }
}
