package unwrittenfun.minecraft.immersiveintegration;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.Configuration;

public class Config {
  public static String CATEGORY_AE = "appliedenergistics2";
  public static String CATEGORY_IE = "immersiveengineering";
  public static String CATEGORY_MINECRAFT = "minecraft";
  public static String CATEGORY_OTHER = "other";

  public boolean enableAE, enableTinkers, verboseLogging;
  public int fluixWireRange, denseWireRange, redstoneWireRange, meTransformerPowerDrain, meDenseTransformerPowerDrain,
      meWireConnectorDrain, meDenseWireConnectorDrain;
  public int cokeOvenDoubleChance;
  public float cokeOvenCreosoteMultiplier, cokeOvenTimeMultiplier;

  public Configuration config;

  public Config(Configuration config) {
    this.config = config;
    config.load();
    readConfig();
  }

  private void readConfig() {
    enableAE = config.getBoolean("enableAE", CATEGORY_AE, true, StatCollector.translateToLocal("immersiveintegration.config.desc.enableAE"), "immersiveintegration.config.enableAE");
    enableAE = enableAE && Loader.isModLoaded("appliedenergistics2");

    fluixWireRange = config.getInt("fluixWireRange", CATEGORY_AE, 24, 1, 64, StatCollector.translateToLocal("immersiveintegration.config.desc.fluixWireRange"), "immersiveintegration.config.fluixWireRange");
    denseWireRange = config.getInt("denseWireRange", CATEGORY_AE, 16, 1, 64, StatCollector.translateToLocal("immersiveintegration.config.desc.denseWireRange"), "immersiveintegration.config.denseWireRange");
    meTransformerPowerDrain = config.getInt("meTransformerPowerDrain", CATEGORY_AE, 16, 0, Integer.MAX_VALUE, StatCollector.translateToLocal("immersiveintegration.config.desc.meTransformerPowerDrain"), "immersiveintegration.config.meTransformerPowerDrain");
    meDenseTransformerPowerDrain = config.getInt("meDenseTransformerPowerDrain", CATEGORY_AE, 32, 0, Integer.MAX_VALUE, StatCollector.translateToLocal("immersiveintegration.config.desc.meDenseTransformerPowerDrain"), "immersiveintegration.config.meDenseTransformerPowerDrain");
    meWireConnectorDrain = config.getInt("meWireConnectorDrain", CATEGORY_AE, 2, 0, Integer.MAX_VALUE, StatCollector.translateToLocal("immersiveintegration.config.desc.meWireConnectorDrain"), "immersiveintegration.config.meWireConnectorDrain");
    meDenseWireConnectorDrain = config.getInt("meDenseWireConnectorDrain", CATEGORY_AE, 4, 0, Integer.MAX_VALUE, StatCollector.translateToLocal("immersiveintegration.config.desc.meDenseWireConnectorDrain"), "immersiveintegration.config.meDenseWireConnectorDrain");

    redstoneWireRange = config.getInt("redstoneWireRange", CATEGORY_MINECRAFT, 32, 1, 64, StatCollector.translateToLocal("immersiveintegration.config.desc.redstoneWireRange"), "immersiveintegration.config.redstoneWireRange");

    cokeOvenDoubleChance = config.getInt("cokeOvenDoubleChance", CATEGORY_IE, 10, 1, Integer.MAX_VALUE, StatCollector.translateToLocal("immersiveintegration.config.desc.cokeOvenDoubleChance"), "immersiveintegration.config.cokeOvenDoubleChance");
    cokeOvenCreosoteMultiplier = config.getFloat("cokeOvenCreosoteMultiplier", CATEGORY_IE, 1.5f, 1f, Float.MAX_VALUE, StatCollector.translateToLocal("immersiveintegration.config.desc.cokeOvenCreosoteMultiplier"), "immersiveintegration.config.cokeOvenCreosoteMultiplier");
    cokeOvenTimeMultiplier = config.getFloat("cokeOvenTimeMultiplier", CATEGORY_IE, 0.5f, 0.01f, Float.MAX_VALUE, StatCollector.translateToLocal("immersiveintegration.config.desc.cokeOvenTimeMultiplier"), "immersiveintegration.config.cokeOvenTimeMultiplier");

    enableTinkers = config.getBoolean("enableTinkers", CATEGORY_OTHER, true, StatCollector.translateToLocal("immersiveintegration.config.desc.enableTinkers"), "immersiveintegration.config.enableTinkers");
    enableTinkers = enableTinkers && Loader.isModLoaded("TConstruct");

    verboseLogging = config.getBoolean("verboseLogging", CATEGORY_OTHER, false, StatCollector.translateToLocal("immersiveintegration.config.desc.verboseLogging"), "immersiveintegration.config.verboseLogging");

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
