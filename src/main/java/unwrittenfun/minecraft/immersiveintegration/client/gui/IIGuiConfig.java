package unwrittenfun.minecraft.immersiveintegration.client.gui;

import cpw.mods.fml.client.config.GuiConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import unwrittenfun.minecraft.immersiveintegration.ImmersiveIntegration;
import unwrittenfun.minecraft.immersiveintegration.ModInfo;

public class IIGuiConfig extends GuiConfig {
  @SuppressWarnings("unchecked")
  public IIGuiConfig(GuiScreen parentScreen) {
    super(parentScreen, new ConfigElement(ImmersiveIntegration.cfg.config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(), ModInfo.MOD_ID, false, false, GuiConfig.getAbridgedConfigPath(ImmersiveIntegration.cfg.config.toString()));
  }
}
