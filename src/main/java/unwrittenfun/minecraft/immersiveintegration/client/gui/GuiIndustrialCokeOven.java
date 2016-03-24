package unwrittenfun.minecraft.immersiveintegration.client.gui;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.IEContent;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import unwrittenfun.minecraft.immersiveintegration.ModInfo;
import unwrittenfun.minecraft.immersiveintegration.gui.containers.ContainerIndustrialCokeOven;

import java.util.ArrayList;
import java.util.List;

public class GuiIndustrialCokeOven extends GuiContainer {
  public static final ResourceLocation texture = new ResourceLocation(ModInfo.MOD_ID, "textures/gui/industrialCokeOven.png");
  public ContainerIndustrialCokeOven container;

  public GuiIndustrialCokeOven(ContainerIndustrialCokeOven container) {
    super(container);
    this.container = container;
    xSize = 176;
    ySize = 166;
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float f1, int mX, int mY) {
    GL11.glColor4f(1, 1, 1, 1);
    mc.renderEngine.bindTexture(texture);
    drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mX, int mY) {
    if (container.cokeOven.tank.getFluidAmount() > 0) {
      int height = (int) (47 * ((float) container.cokeOven.tank.getFluidAmount() / (float) container.cokeOven.tank.getCapacity()));
      ClientUtils.drawRepeatedFluidIcon(IEContent.fluidCreosote, 124, 66 - height, 16, height);
    }
    mc.renderEngine.bindTexture(texture);
    drawTexturedModalRect(122, 17, xSize, 12, 20, 51);

    for (int i = 0; i < 4; i++) {
      drawTexturedModalRect(17 + 28 * i, 36 + container.cokeOven.clientProgress[i], xSize, container.cokeOven.clientProgress[i], 9, 12 - container.cokeOven.clientProgress[i]);
    }
  }

  @Override
  public void drawScreen(int mx, int my, float f) {
    super.drawScreen(mx, my, f);

    drawTooltips(mx - guiLeft, my - guiTop, mx, my);
  }

  public void drawTooltips(int guiMX, int guiMY, int renderMX, int renderMY) {
    List<String> fluidLines = new ArrayList<>();
    if (container.cokeOven.tank.getFluidAmount() == 0) {
      fluidLines.add("Empty");
    } else {
      fluidLines.add(IEContent.fluidCreosote.getLocalizedName(container.cokeOven.tank.getFluid()));
    }
    fluidLines.add(container.cokeOven.tank.getFluidAmount() + " / " + container.cokeOven.tank.getCapacity() + " mB");

    if (guiMX >= 124 && guiMX < 124 + 16 && guiMY >= 19 && guiMY < 19 + 47) {
      drawHoveringText(fluidLines, renderMX, renderMY, fontRendererObj);
      RenderHelper.enableGUIStandardItemLighting();
    }
  }
}
