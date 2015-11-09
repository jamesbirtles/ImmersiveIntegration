package unwrittenfun.minecraft.immersiveintegration.recipe;

import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.Arrays;

public class CapacitorBoxRecipe extends ShapelessRecipes {
  protected ItemStack energyContainer, result;

  public CapacitorBoxRecipe(ItemStack energyContainer, ItemStack result) {
    super(result, Arrays.asList(energyContainer, new ItemStack(IEContent.blockWoodenDevice, 1, 4)));
    this.energyContainer = energyContainer;
    this.result = result;
  }

  @Override
  public boolean matches(InventoryCrafting crafting, World world) {
    boolean foundCrate = false, foundCap = false;
    for (int i = 0; i < crafting.getSizeInventory(); i++) {
      ItemStack stack = crafting.getStackInSlot(i);
      if (stack != null) {
        if (!foundCap && stack.isItemEqual(energyContainer)) {
          foundCap = true;
        } else if (!foundCrate && stack.getItem() == Item.getItemFromBlock(IEContent.blockWoodenDevice) && stack.getItemDamage() == 4) {
          foundCrate = true;
        } else {
          return false;
        }
      }
    }

    return foundCrate && foundCap;
  }

  @Override
  public ItemStack getCraftingResult(InventoryCrafting crafting) {
    ItemStack foundCrate = null, foundCap = null;
    for (int i = 0; i < crafting.getSizeInventory(); i++) {
      ItemStack stack = crafting.getStackInSlot(i);
      if (stack != null) {
        if (foundCap == null && stack.isItemEqual(energyContainer)) {
          foundCap = stack;
        } else if (foundCrate == null && stack.getItem() == Item.getItemFromBlock(IEContent.blockWoodenDevice) && stack.getItemDamage() == 4) {
          foundCrate = stack;
        } else {
          return null;
        }
      }
    }

    if (foundCap == null || foundCrate == null) return null;

    ItemStack boxedContainer = result.copy();
    boxedContainer.stackTagCompound = new NBTTagCompound();
    boxedContainer.stackTagCompound.setInteger("Energy", ItemNBTHelper.getInt(foundCap, "energyStorage"));
    return boxedContainer;
  }

  @Override
  public int getRecipeSize() {
    return 2;
  }

  @Override
  public ItemStack getRecipeOutput() {
    return result.copy();
  }
}