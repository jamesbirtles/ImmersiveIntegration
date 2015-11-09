package unwrittenfun.minecraft.immersiveintegration.client.gui;

import cpw.mods.fml.client.config.DummyConfigElement;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import unwrittenfun.minecraft.immersiveintegration.Config;
import unwrittenfun.minecraft.immersiveintegration.ImmersiveIntegration;
import unwrittenfun.minecraft.immersiveintegration.ModInfo;

import java.util.ArrayList;
import java.util.List;

public class IIGuiConfig extends GuiConfig {
  @SuppressWarnings("unchecked")
  public IIGuiConfig(GuiScreen parentScreen) {
    super(parentScreen, getConfigElements(), ModInfo.MOD_ID, false, false, ModInfo.NAME);
  }

  private static List<IConfigElement> getConfigElements() {
    // Set up our list to store the elements in.
    List<IConfigElement> list = new ArrayList<>();
    list.add(configElement(Config.CATEGORY_AE, "immersiveintegration.config.category.ae", "immersiveintegration.config.category.ae"));
    list.add(configElement(Config.CATEGORY_IE, "immersiveintegration.config.category.ie", "immersiveintegration.config.category.ie"));
    list.add(configElement(Config.CATEGORY_MINECRAFT, "immersiveintegration.config.category.minecraft", "immersiveintegration.config.category.minecraft"));
    list.add(configElement(Config.CATEGORY_OTHER, "immersiveintegration.config.category.other", "immersiveintegration.config.category.other"));
    return list;
  }

  @SuppressWarnings("unchecked")
  private static IConfigElement configElement(String category, String name, String tooltip_key) {
    return new DummyConfigElement.DummyCategoryElement(name, tooltip_key,
        new ConfigElement(ImmersiveIntegration.cfg.config.getCategory(category)).getChildElements());
  }
}
