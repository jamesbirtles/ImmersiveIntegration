package unwrittenfun.minecraft.immersiveintegration.tiles;

import blusunrize.immersiveengineering.common.Config;

public class TileInductionChargerMV extends TileInductionChargerLV {
  @Override
  public int getCapacity() {
    return Config.getInt("capacitorMV_storage") / 2;
  }

  @Override
  public int getMaxInOut() {
    return Config.getInt("capacitorMV_input");
  }
}
