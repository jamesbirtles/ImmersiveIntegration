package unwrittenfun.minecraft.immersiveintegration.compat;

import cpw.mods.fml.common.Loader;
import unwrittenfun.minecraft.immersiveintegration.ImmersiveIntegration;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;

public abstract class CompatModule {
  private static HashSet<CompatModule> modules = new HashSet<>();

  static {
    register(CCCompat.class);
    register(WailaCompat.class);
    register(FMPCompat.class);
  }

  public static void eventPreInit() {
    for (CompatModule module : modules) {
      if (Loader.isModLoaded(module.getModId())) module.preInit();
    }
  }

  public static void eventInit() {
    for (CompatModule module : modules) {
      if (Loader.isModLoaded(module.getModId())) module.init();
    }
  }

  public static void eventPostInit() {
    for (CompatModule module : modules) {
      if (Loader.isModLoaded(module.getModId())) module.postInit();
    }
  }

  public static void register(Class<? extends CompatModule> compatClass) {
    try {
      modules.add(compatClass.getConstructor().newInstance());
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      ImmersiveIntegration.log.error("Something went wrong loading a compat module.");
      e.printStackTrace();
    }
  }

  public abstract String getModId();

  public void preInit() {

  }

  public void init() {

  }

  public void postInit() {

  }
}
