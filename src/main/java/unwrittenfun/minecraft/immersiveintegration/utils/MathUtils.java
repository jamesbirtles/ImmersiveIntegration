package unwrittenfun.minecraft.immersiveintegration.utils;

import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

public class MathUtils {
  public static int increment(int value, int max) {
    return value + 1 > max ? 0 : value + 1;
  }

  public static double degToRad(int angle) {
    return (double) angle * (Math.PI / 180d);
  }

  public static void applyRotation(ForgeDirection direction, Matrix4 matrix4) {
    switch (direction) {
      case DOWN:
        matrix4.rotate(degToRad(90), 1, 0, 0);
        break;
      case UP:
        matrix4.rotate(degToRad(-90), 1, 0, 0);
        matrix4.rotate(degToRad(180), 0, 0, 1);
        break;
      case SOUTH:
        matrix4.rotate(degToRad(180), 0, 1, 0);
        break;
      case WEST:
        matrix4.rotate(degToRad(90), 0, 1, 0);
        break;
      case EAST:
        matrix4.rotate(degToRad(270), 0, 1, 0);
        break;
    }
  }
}
