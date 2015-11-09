package unwrittenfun.minecraft.immersiveintegration.blocks;

import cofh.api.energy.IEnergyContainerItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import unwrittenfun.minecraft.immersiveintegration.ImmersiveIntegration;
import unwrittenfun.minecraft.immersiveintegration.client.renderers.BlockRenderIIBlocks;
import unwrittenfun.minecraft.immersiveintegration.tiles.TileInductionCharger;

public class BlockInductionCharger extends BlockContainer {
  protected BlockInductionCharger(String key) {
    super(Material.wood);
    setBlockTextureName(key);
    setBlockName(key);
    setCreativeTab(ImmersiveIntegration.iiCreativeTab);
    setBlockBounds(0, 0, 0, 1, 0.5625f, 1);
    setHardness(2f);
  }

  @Override
  public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
    return new TileInductionCharger();
  }

  @Override
  public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
    return ForgeDirection.UP != side;
  }

  @Override
  public boolean renderAsNormalBlock() {
    return false;
  }

  @Override
  public boolean isOpaqueCube() {
    return false;
  }

  @Override
  public int getRenderType() {
    return BlockRenderIIBlocks.RENDER_ID;
  }

  @Override
  public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
    if (!world.isRemote) {
      TileEntity tileEntity = world.getTileEntity(x, y, z);
      if (tileEntity instanceof TileInductionCharger) {
        TileInductionCharger charger = (TileInductionCharger) tileEntity;
        if (charger.chargingStack == null) {
          if (player.getHeldItem() != null && player.getHeldItem().getItem() instanceof IEnergyContainerItem &&
             !(player.getHeldItem().getItem() instanceof ItemBlock) && ((IEnergyContainerItem) player.getHeldItem().getItem()).getMaxEnergyStored(player.getHeldItem()) > 0) {
            charger.setChargingStack(player.getHeldItem());
            player.inventory.decrStackSize(player.inventory.currentItem, 1);
          }
        } else {
          EntityItem dropItem = new EntityItem(world, player.posX, player.posY + 0.5f, player.posZ, charger.chargingStack.copy());
          dropItem.delayBeforeCanPickup = 0;
          dropItem.motionX = 0;
          dropItem.motionY = 0;
          dropItem.motionZ = 0;
          world.spawnEntityInWorld(dropItem);
          charger.setChargingStack(null);
        }
      }

      return true;
    }
    return true;
  }

  @Override
  public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
    if (!world.isRemote && entity instanceof EntityItem) {
      EntityItem itemEntity = (EntityItem) entity;
      TileEntity tileEntity = world.getTileEntity(x, y, z);
      if (tileEntity instanceof TileInductionCharger) {
        TileInductionCharger charger = (TileInductionCharger) tileEntity;
        if (charger.chargingStack == null) {
          if (itemEntity.getEntityItem().getItem() instanceof IEnergyContainerItem && !(itemEntity.getEntityItem().getItem() instanceof ItemBlock)) {
            charger.setChargingStack(itemEntity.getEntityItem());
            itemEntity.getEntityItem().stackSize--;
            if (itemEntity.getEntityItem().stackSize < 1) {
              itemEntity.setDead();
            }
          }
        }
      }
    }
  }
}
