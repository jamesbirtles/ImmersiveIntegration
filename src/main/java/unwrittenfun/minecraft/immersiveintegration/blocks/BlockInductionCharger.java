package unwrittenfun.minecraft.immersiveintegration.blocks;

import cofh.api.energy.IEnergyContainerItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import unwrittenfun.minecraft.immersiveintegration.ImmersiveIntegration;
import unwrittenfun.minecraft.immersiveintegration.client.IIRenderIDs;
import unwrittenfun.minecraft.immersiveintegration.client.renderers.block.BlockRenderIIBlocks;
import unwrittenfun.minecraft.immersiveintegration.tiles.TileInductionChargerHV;
import unwrittenfun.minecraft.immersiveintegration.tiles.TileInductionChargerLV;
import unwrittenfun.minecraft.immersiveintegration.tiles.TileInductionChargerMV;

import java.util.List;

public class BlockInductionCharger extends BlockContainer {
  public static final String[] KEYS = new String[]{
     "LV", "MV", "HV"
  };
  protected IIcon[] icons;
  protected String key;

  public BlockInductionCharger(String key) {
    super(Material.wood);
    this.key = key;
    setBlockTextureName(key);
    setBlockName(key);
    setCreativeTab(ImmersiveIntegration.iiCreativeTab);
    setBlockBounds(0, 0, 0, 1, 0.5625f, 1);
    setHardness(2f);
    setStepSound(Block.soundTypeWood);
  }

  @Override
  public TileEntity createNewTileEntity(World world, int meta) {
    switch (meta) {
      case 0:
        return new TileInductionChargerLV();
      case 1:
        return new TileInductionChargerMV();
      case 2:
        return new TileInductionChargerHV();
    }
    return null;
  }

  @Override
  public void registerBlockIcons(IIconRegister register) {
    icons = new IIcon[KEYS.length];
    for (int i = 0; i < icons.length; i++) {
      icons[i] = register.registerIcon(key + KEYS[i]);
    }
  }

  @Override
  public IIcon getIcon(int side, int meta) {
    return icons[Math.min(meta, icons.length - 1)];
  }

  @Override
  public int damageDropped(int meta) {
    return meta;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
    for (int i = 0; i < KEYS.length; i++) {
      list.add(new ItemStack(item, 1, i));
    }
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
    return IIRenderIDs.BLOCKS;
  }

  @Override
  public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
    if (!world.isRemote) {
      TileEntity tileEntity = world.getTileEntity(x, y, z);
      if (tileEntity instanceof TileInductionChargerLV) {
        TileInductionChargerLV charger = (TileInductionChargerLV) tileEntity;
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
    if (!world.isRemote && !entity.isDead) {
      TileEntity tileEntity = world.getTileEntity(x, y, z);
      if (tileEntity instanceof TileInductionChargerLV) {
        TileInductionChargerLV charger = (TileInductionChargerLV) tileEntity;
        if (entity instanceof EntityItem) {
          EntityItem itemEntity = (EntityItem) entity;
          if (charger.chargingStack == null) {
            if (itemEntity.getEntityItem().getItem() instanceof IEnergyContainerItem && !(itemEntity.getEntityItem().getItem() instanceof ItemBlock)) {
              charger.setChargingStack(itemEntity.getEntityItem());
              itemEntity.getEntityItem().stackSize--;
              if (itemEntity.getEntityItem().stackSize < 1) {
                itemEntity.setDead();
              }
            }
          }
        } else if (entity instanceof EntityPlayer) {
          EntityPlayer player = (EntityPlayer) entity;
          charger.chargePlayer(player);
        }
      }
    }
  }
}
