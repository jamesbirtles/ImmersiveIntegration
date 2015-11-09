package unwrittenfun.minecraft.immersiveintegration.tiles;

import blusunrize.immersiveengineering.common.Config;

public class TileInductionChargerHV extends TileInductionChargerLV {
  @Override
  public int getCapacity() {
    return Config.getInt("capacitorHV_storage") / 2;
  }

  @Override
  public int getMaxInOut() {
    return Config.getInt("capacitorHV_input");
  }
}
