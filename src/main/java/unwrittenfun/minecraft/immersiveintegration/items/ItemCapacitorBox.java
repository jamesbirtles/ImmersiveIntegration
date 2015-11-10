package unwrittenfun.minecraft.immersiveintegration.items;

import blusunrize.immersiveengineering.common.Config;
import cofh.api.energy.IEnergyContainerItem;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import unwrittenfun.minecraft.immersiveintegration.ImmersiveIntegration;

import java.util.List;

public class ItemCapacitorBox extends Item implements IEnergyContainerItem {
  private static final String[] KEYS = new String[]{
     "LV", "MV", "HV", "Creative"
  };
  private static final int[] capacity = new int[]{
     Config.getInt("capacitorLV_storage"),
     Config.getInt("capacitorMV_storage"),
     Config.getInt("capacitorHV_storage"),
     Integer.MAX_VALUE
  };
  private static final int[] maxReceive = new int[]{
     Config.getInt("capacitorLV_input"),
     Config.getInt("capacitorMV_input"),
     Config.getInt("capacitorHV_input"),
     100000
  };
  private static final int[] maxExtract = new int[]{
     Config.getInt("capacitorLV_output"),
     Config.getInt("capacitorMV_output"),
     Config.getInt("capacitorHV_output"),
     100000
  };

  protected IIcon[] icons;
  protected String key;

  public ItemCapacitorBox(String key) {
    this.key = key;
    setTextureName(key);
    setUnlocalizedName(key);
    setHasSubtypes(true);
    setCreativeTab(ImmersiveIntegration.iiCreativeTab);
    setMaxStackSize(1);
  }

  @Override
  public void registerIcons(IIconRegister register) {
    icons = new IIcon[KEYS.length];
    for (int i = 0; i < KEYS.length; i++) {
      icons[i] = register.registerIcon(key + KEYS[i]);
    }
  }

  @Override
  public IIcon getIconFromDamage(int meta) {
    return icons[Math.min(meta, icons.length - 1)];
  }

  @Override
  public String getUnlocalizedName(ItemStack stack) {
    int meta = Math.min(stack.getItemDamage(), KEYS.length - 1);
    return getUnlocalizedName() + KEYS[meta];
  }

  @SuppressWarnings("unchecked")
  @Override
  public void getSubItems(Item item, CreativeTabs tab, List items) {
    for (int i = 0; i < KEYS.length; i++) {
      items.add(new ItemStack(item, 1, i));

      if (i != 3) {
        ItemStack fullStack = new ItemStack(item, 1, i);
        fullStack.stackTagCompound = new NBTTagCompound();
        fullStack.stackTagCompound.setInteger("Energy", capacity[i]);
        items.add(fullStack);
      }
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List lines, boolean idc) {
    if (stack.getItemDamage() == 3) {
      lines.add(StatCollector.translateToLocal("immersiveintegration.tooltip.capacitor.infinite"));
    } else {
      lines.add(getEnergyStored(stack) + " / " + getMaxEnergyStored(stack) + " RF");
    }

    if (isActive(stack)) {
      lines.add(StatCollector.translateToLocal("immersiveintegration.tooltip.capacitor.active0"));
      lines.add(StatCollector.translateToLocal("immersiveintegration.tooltip.capacitor.active1"));
    } else {
      lines.add(StatCollector.translateToLocal("immersiveintegration.tooltip.capacitor.rightClickActive0"));
      lines.add(StatCollector.translateToLocal("immersiveintegration.tooltip.capacitor.rightClickActive1"));
    }
  }

  public boolean isActive(ItemStack stack) {
    if (stack.stackTagCompound == null) {
      stack.stackTagCompound = new NBTTagCompound();
      stack.stackTagCompound.setBoolean("Active", false);
      return false;
    }
    return stack.stackTagCompound.getBoolean("Active");
  }

  @Override
  public boolean showDurabilityBar(ItemStack stack) {
    return getEnergyStored(stack) != getMaxEnergyStored(stack);
  }

  @Override
  public double getDurabilityForDisplay(ItemStack stack) {
    return 1 - (double) getEnergyStored(stack) / (double) getMaxEnergyStored(stack);
  }

  @Override
  public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
    if (!world.isRemote) {
      stack.stackTagCompound.setBoolean("Active", !isActive(stack));
    }
    return stack;
  }

  @Override
  public boolean hasEffect(ItemStack stack, int pass) {
    return isActive(stack);
  }

  @Override
  public void onUpdate(ItemStack stack, World world, Entity entity, int meta, boolean p_77663_5_) {
    if (!world.isRemote && isActive(stack) && entity instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) entity;
      ItemStack held = player.getHeldItem();
      if (held != stack && held != null && held.getItem() instanceof IEnergyContainerItem) {
        IEnergyContainerItem energyContainer = (IEnergyContainerItem) held.getItem();
        int energyToInsert = extractEnergy(stack, maxExtract[stack.getItemDamage()], true);
        int energyExtracted = energyContainer.receiveEnergy(held, energyToInsert, false);
        extractEnergy(stack, energyExtracted, false);
      }
    }
  }

  /// IEnergyContainerItem
  @Override
  public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
    int meta = Math.min(container.getItemDamage(), KEYS.length - 1);

    if (meta == 3) return maxReceive;

    if (container.stackTagCompound == null) {
      container.stackTagCompound = new NBTTagCompound();
    }
    int energy = container.stackTagCompound.getInteger("Energy");
    int energyReceived = Math.min(capacity[meta] - energy, Math.min(ItemCapacitorBox.maxReceive[meta], maxReceive));

    if (!simulate) {
      energy += energyReceived;
      container.stackTagCompound.setInteger("Energy", energy);
    }
    return energyReceived;
  }

  @Override
  public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
    int meta = Math.min(container.getItemDamage(), KEYS.length - 1);

    if (meta == 3) return maxExtract;

    if (container.stackTagCompound == null || !container.stackTagCompound.hasKey("Energy")) {
      return 0;
    }
    int energy = container.stackTagCompound.getInteger("Energy");
    int energyExtracted = Math.min(energy, Math.min(ItemCapacitorBox.maxExtract[meta], maxExtract));

    if (!simulate) {
      energy -= energyExtracted;
      container.stackTagCompound.setInteger("Energy", energy);
    }
    return energyExtracted;
  }

  @Override
  public int getEnergyStored(ItemStack container) {
    int meta = Math.min(container.getItemDamage(), KEYS.length - 1);
    if (meta == 3) return getMaxEnergyStored(container);

    if (container.stackTagCompound == null || !container.stackTagCompound.hasKey("Energy")) {
      return 0;
    }
    return container.stackTagCompound.getInteger("Energy");
  }

  @Override
  public int getMaxEnergyStored(ItemStack container) {
    int meta = Math.min(container.getItemDamage(), KEYS.length - 1);

    return capacity[meta];
  }
}
