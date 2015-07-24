package unwrittenfun.minecraft.immersiveintegration.utils;

public class MathUtils {
  public static int increment(int value, int max) {
    return value + 1 > max ? 0 : value + 1;
  }
}
