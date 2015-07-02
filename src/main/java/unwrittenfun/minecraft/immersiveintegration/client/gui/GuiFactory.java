package unwrittenfun.minecraft.immersiveintegration.client.gui;

import cpw.mods.fml.client.IModGuiFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import java.util.Set;

public class GuiFactory implements IModGuiFactory {
  @Override
  public void initialize(Minecraft minecraft) {

  }

  @Override
  public Class<? extends GuiScreen> mainConfigGuiClass() {
    return IIGuiConfig.class;
  }

  @Override
  public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
    return null;
  }

  @Override
  public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
    return null;
  }
}
