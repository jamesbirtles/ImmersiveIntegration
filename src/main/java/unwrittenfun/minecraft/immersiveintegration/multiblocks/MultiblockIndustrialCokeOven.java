package unwrittenfun.minecraft.immersiveintegration.multiblocks;

import blusunrize.immersiveengineering.api.MultiblockHandler.IMultiblock;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityArcFurnace;
import blusunrize.immersiveengineering.common.blocks.stone.TileEntityCokeOven;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import unwrittenfun.minecraft.immersiveintegration.ImmersiveIntegration;
import unwrittenfun.minecraft.immersiveintegration.ModInfo;
import unwrittenfun.minecraft.immersiveintegration.blocks.IIBlocks;
import unwrittenfun.minecraft.immersiveintegration.tiles.IMultiblockTile;

public class MultiblockIndustrialCokeOven implements IMultiblock {
  public static ItemStack[][][] blockStructure = new ItemStack[4][5][7];
  public static ItemStack[][][][] targetStructure = new ItemStack[4][5][7][2];

  static {
    for (int h = 0; h < 4; h++) {
      for (int l = 0; l < 5; l++) {
        for (int w = 0; w < 7; w++) {
          if ((h == 0 && (w == 0 || w == 2 || w == 4 || w == 6) && (l == 0 || l == 4)) || (h == 1 && (w == 0 || w == 6) && l == 2)) {
            blockStructure[h][l][w] = new ItemStack(IIBlocks.steelDecoration, 1, 2);
          } else if (w == 1 || w == 3 || w == 5) {
            blockStructure[h][l][w] = new ItemStack(IIBlocks.steelDecoration, 1, 1);
          } else {
            blockStructure[h][l][w] = new ItemStack(IIBlocks.steelDecoration, 1, 0);
          }

          if (h == 1 && l == 2 && w == 3) {
            targetStructure[h][l][w][0] = new ItemStack(IIBlocks.industrialCokeOven, 1, 1);
            targetStructure[h][l][w][1] = new ItemStack(IIBlocks.industrialCokeOven, 1, 2);
          } else {
            targetStructure[h][l][w][0] = new ItemStack(IIBlocks.industrialCokeOven, 1, 3);
            targetStructure[h][l][w][1] = new ItemStack(IIBlocks.industrialCokeOven, 1, 3);
          }
        }
      }
    }
  }

  @Override
  public boolean isBlockTrigger(Block b, int meta) {
    return b == IIBlocks.steelDecoration && meta == 1;
  }

  @Override
  public boolean createStructure(World world, int x, int y, int z, int side, EntityPlayer player) {
//    ImmersiveIntegration.log.info("Attempt multiblock creation");

    ForgeDirection direction = ForgeDirection.getOrientation(side);
    for (int dz = -4; dz <= 0; dz++) {
      for (int dx = -3; dx <= 3; dx++) {
        for (int dy = -1; dy <= 2; dy++) {
          int ddz = dz * direction.offsetZ;
          int ddx = dx;
          if (direction.offsetX != 0) {
            ddz = dx;
            ddx = dz * direction.offsetX;
          }
          ItemStack targetItem = blockStructure[dy  + 1][dz + 4][dx + 3];
          if (world.getBlock(x + ddx, y + dy, z + ddz) != ((ItemBlock) targetItem.getItem()).field_150939_a || world.getBlockMetadata(x + ddx, y + dy, z + ddz) != targetItem.getItemDamage()) {
//            ImmersiveIntegration.log.info(world.getBlock(x + ddx, y + dy, z + ddz).getLocalizedName() + ":" + world.getBlockMetadata(x + ddx, y + dy, z + ddz) + " - " + ((ItemBlock) targetItem.getItem()).field_150939_a.getLocalizedName() + ":" + targetItem.getItemDamage());
//            ImmersiveIntegration.log.info("Multiblock failed");
            return false;
          }
        }
      }
    }

    for (int dz = -4; dz <= 0; dz++) {
      for (int dx = -3; dx <= 3; dx++) {
        for (int dy = -1; dy <= 2; dy++) {
          int ddz = dz * direction.offsetZ;
          int ddx = dx;
          if (direction.offsetX != 0) {
            ddz = dx;
            ddx = dz * direction.offsetX;
          }
          int axis = 0;
          if (direction.offsetX != 0) axis = 1;
          ItemStack targetItem = targetStructure[dy + 1][dz + 4][dx + 3][axis];
          Block targetBlock = ((ItemBlock) targetItem.getItem()).field_150939_a;
          if (world.getBlock(x + ddx, y + dy, z + ddz) == targetBlock) {
            world.setBlockMetadataWithNotify(x + ddx, y + dy, z + ddz, targetItem.getItemDamage(), 3);
          } else {
            world.setBlock(x + ddx, y + dy, z + ddz, targetBlock, targetItem.getItemDamage(), 3);
          }
          TileEntity tileEntity = world.getTileEntity(x + ddx, y + dy, z + ddz);
          if (tileEntity instanceof IMultiblockTile) {
            IMultiblockTile multiblock = (IMultiblockTile) tileEntity;
            multiblock.setOffset(new int[] { ddx, dy, ddz, direction.ordinal() });
            multiblock.setReplaced(blockStructure[dy + 1][dz + 4][dx + 3].copy());
            multiblock.setFormed(true);
          }
        }
      }
    }

//    ImmersiveIntegration.log.info("Multiblock valid");

    return true;
  }

  @Override
  public ItemStack[][][] getStructureManual() {
    return blockStructure;
  }

  @Override
  public ItemStack[] getTotalMaterials() {
    return new ItemStack[] {
        new ItemStack(IIBlocks.steelDecoration, 70, 0),
        new ItemStack(IIBlocks.steelDecoration, 60, 1),
        new ItemStack(IIBlocks.steelDecoration, 10, 2)
    };
  }


  ////
  // TODO
  ////

  @Override
  public float getManualScale() {
    return 10.5f;
  }

  @Override
  public boolean canRenderFormedStructure() {
    return false;
  }

  @Override
  public void renderFormedStructure() {
  }

  @Override
  public String getUniqueName() {
    return ModInfo.MOD_ID + ":industrialCokeOven";
  }

  @Override
  public boolean overwriteBlockRender(ItemStack stack, int iterator) {
    return false;
  }
}
