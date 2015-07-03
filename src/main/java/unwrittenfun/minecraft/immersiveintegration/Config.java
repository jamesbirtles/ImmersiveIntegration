package unwrittenfun.minecraft.immersiveintegration;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.Configuration;

public class Config {
  public boolean enableAE;
  public int fluixWireRange;
  public int denseWireRange;

  public Configuration config;

  public Config(Configuration config) {
    this.config = config;
    config.load();
    readConfig();
  }

  private void readConfig() {
    enableAE = config.getBoolean("enableAE", Configuration.CATEGORY_GENERAL, true, StatCollector.translateToLocal("immersiveintegration.config.desc.enableAE"), "immersiveintegration.config.enableAE");
    enableAE = enableAE && Loader.isModLoaded("appliedenergistics2");
    fluixWireRange = config.getInt("fluixWireRange", Configuration.CATEGORY_GENERAL, 16, 1, 64, StatCollector.translateToLocal("immersiveintegration.config.desc.fluixWireRange"), "immersiveintegration.config.fluixWireRange");
    denseWireRange = config.getInt("denseWireRange", Configuration.CATEGORY_GENERAL, 8, 1, 64, StatCollector.translateToLocal("immersiveintegration.config.desc.denseWireRange"), "immersiveintegration.config.denseWireRange");
    if (config.hasChanged()) {
      config.save();
    }
  }

  @SubscribeEvent
  public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
    if (event.modID.equalsIgnoreCase(ModInfo.MOD_ID)) {
      readConfig();
    }
  }
}
