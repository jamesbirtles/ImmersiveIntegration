package unwrittenfun.minecraft.immersiveintegration.blocks;

import appeng.api.AEApi;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import unwrittenfun.minecraft.immersiveintegration.tiles.TileMEWireConnector;
import unwrittenfun.minecraft.immersiveintegration.utils.TileUtils;

public class BlockMEWireConnector extends BlockWireConnector {
  public BlockMEWireConnector(String key) {
    super(key);
  }

  @Override
  public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
    return new TileMEWireConnector();
  }

  @Override
  public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
    TileMEWireConnector wireConnector = TileUtils.getTileEntity(world, x, y, z, TileMEWireConnector.class);
    if (!world.isRemote && entity instanceof EntityPlayer && wireConnector != null) {
      wireConnector.getGridNode(ForgeDirection.UNKNOWN).setPlayerID(AEApi.instance().registries().players().getID((EntityPlayer) entity));
    }
  }
}
