package unwrittenfun.minecraft.immersiveintegration;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.Configuration;

public class Config {
  public boolean enableAE;
  public int fluixWireRange, denseWireRange, redstoneWireRange, meTransformerPowerDrain;
  public int cokeOvenDoubleChance;
  public float cokeOvenCreosoteMultiplier, cokeOvenTimeMultiplier;

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
    meTransformerPowerDrain = config.getInt("meTransformerPowerDrain", Configuration.CATEGORY_GENERAL, 24, 0, Integer.MAX_VALUE, StatCollector.translateToLocal("immersiveintegration.config.desc.meTransformerPowerDrain"), "immersiveintegration.config.meTransformerPowerDrain");


    redstoneWireRange = config.getInt("redstoneWireRange", Configuration.CATEGORY_GENERAL, 32, 1, 64, StatCollector.translateToLocal("immersiveintegration.config.desc.redstoneWireRange"), "immersiveintegration.config.redstoneWireRange");

    cokeOvenDoubleChance = config.getInt("cokeOvenDoubleChance", Configuration.CATEGORY_GENERAL, 10, 1, Integer.MAX_VALUE, StatCollector.translateToLocal("immersiveintegration.config.desc.cokeOvenDoubleChance"), "immersiveintegration.config.cokeOvenDoubleChance");
    cokeOvenCreosoteMultiplier = config.getFloat("cokeOvenCreosoteMultiplier", Configuration.CATEGORY_GENERAL, 1.5f, 1f, Float.MAX_VALUE, StatCollector.translateToLocal("immersiveintegration.config.desc.cokeOvenCreosoteMultiplier"), "immersiveintegration.config.cokeOvenCreosoteMultiplier");
    cokeOvenTimeMultiplier = config.getFloat("cokeOvenTimeMultiplier", Configuration.CATEGORY_GENERAL, 0.5f, 0.01f, Float.MAX_VALUE, StatCollector.translateToLocal("immersiveintegration.config.desc.cokeOvenTimeMultiplier"), "immersiveintegration.config.cokeOvenTimeMultiplier");

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
