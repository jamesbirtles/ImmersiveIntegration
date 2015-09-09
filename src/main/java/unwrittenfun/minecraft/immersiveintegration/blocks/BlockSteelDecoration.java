package unwrittenfun.minecraft.immersiveintegration.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import unwrittenfun.minecraft.immersiveintegration.ImmersiveIntegration;

import java.util.List;

public class BlockSteelDecoration extends Block {
  private final String[] keys;
  public IIcon[] icons;

  public BlockSteelDecoration(String key, String[] keys) {
    super(Material.iron);
    setBlockName(key);
    setBlockTextureName(key);
    setCreativeTab(ImmersiveIntegration.iiCreativeTab);
    setHardness(2.5f);
    setStepSound(Block.soundTypeMetal);

    this.keys = keys;
  }

  @Override
  public int damageDropped(int meta) {
    return meta;
  }

  @Override
  public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
    for (int i = 0; i < keys.length; i++) {
      list.add(new ItemStack(item, 1, i));
    }
  }

  @Override
  public void registerBlockIcons(IIconRegister register) {
    icons = new IIcon[keys.length * 2];
    for (int i = 0; i < keys.length; i++) {
      icons[i * 2] = register.registerIcon(getTextureName() + keys[i]);
      icons[(i * 2) + 1] = register.registerIcon(getTextureName() + keys[i] + "Top");
    }
  }

  @Override
  public IIcon getIcon(int side, int meta) {
    return (side == 0 || side == 1) ? icons[meta * 2 + 1] : icons[meta * 2];
  }
}
