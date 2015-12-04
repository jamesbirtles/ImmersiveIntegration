package unwrittenfun.minecraft.immersiveintegration.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import net.minecraft.item.ItemStack;
import unwrittenfun.minecraft.immersiveintegration.ModInfo;
import unwrittenfun.minecraft.immersiveintegration.blocks.IIBlocks;

/**
 * Author: James Birtles
 */
public class NEIImmersiveIntegrationConfig implements IConfigureNEI {
  @Override
  public void loadConfig() {
    API.hideItem(new ItemStack(IIBlocks.industrialCokeOven));
    API.hideItem(new ItemStack(IIBlocks.extendedPost));
  }

  @Override
  public String getName() {
    return ModInfo.NAME + " Plugin";
  }

  @Override
  public String getVersion() {
    return ModInfo.VERSION;
  }
}
