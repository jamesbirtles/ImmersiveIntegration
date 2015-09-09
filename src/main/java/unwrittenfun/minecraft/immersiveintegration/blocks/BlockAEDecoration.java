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

public class BlockAEDecoration extends Block {
  public IIcon[] icons;

  public BlockAEDecoration(String key) {
    super(Material.iron);
    setBlockName(key);
    setBlockTextureName(key);
    setCreativeTab(ImmersiveIntegration.iiCreativeTab);
    setHardness(2.5f);
    setStepSound(Block.soundTypeMetal);
  }

  @Override
  public int damageDropped(int meta) {
    return meta;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
    for (int i = 0; i < IIBlocks.AE_DECORATION_KEYS.length; i++) {
      list.add(new ItemStack(item, 1, i));
    }
  }

  @Override
  public void registerBlockIcons(IIconRegister register) {
    icons = new IIcon[IIBlocks.AE_DECORATION_KEYS.length * 2];
    for (int i = 0; i < IIBlocks.AE_DECORATION_KEYS.length; i++) {
      icons[i * 2] = register.registerIcon(getTextureName() + IIBlocks.AE_DECORATION_KEYS[i]);
      icons[(i * 2) + 1] = register.registerIcon(getTextureName() + IIBlocks.AE_DECORATION_KEYS[i] + "Top");
    }
  }

  @Override
  public IIcon getIcon(int side, int meta) {
    return (side == 0 || side == 1) ? icons[meta * 2 + 1] : icons[meta * 2];
  }
}
