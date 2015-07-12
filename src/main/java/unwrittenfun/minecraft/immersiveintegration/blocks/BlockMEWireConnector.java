package unwrittenfun.minecraft.immersiveintegration.blocks;

import appeng.core.WorldSettings;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import unwrittenfun.minecraft.immersiveintegration.tiles.TileMEWireConnector;

public class BlockMEWireConnector extends BlockWireConnector {
  protected BlockMEWireConnector(String key) {
    super(key);
  }

  @Override
  public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
    return new TileMEWireConnector();
  }

  @Override
  public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
    TileEntity tileEntity = world.getTileEntity(x, y, z);
    if (!world.isRemote && entity instanceof EntityPlayer && tileEntity instanceof TileMEWireConnector) {
      TileMEWireConnector wireConnector = (TileMEWireConnector) tileEntity;
      if (wireConnector.theGridNode == null) wireConnector.createAELink();
      wireConnector.theGridNode.setPlayerID(WorldSettings.getInstance().getPlayerID(((EntityPlayer) entity).getGameProfile()));
    }
  }
}
