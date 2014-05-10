package bludecorations.client;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import bludecorations.common.ContainerInventoryDecoration;
import bludecorations.common.TileEntityCustomizeableDecoration;

public class GuiInventoryDecoration extends GuiContainer {

	ResourceLocation gui = new ResourceLocation("bludecorations:textures/gui/inventoryDecoration.png");
	
	public GuiInventoryDecoration(InventoryPlayer inventoryPlayer, TileEntityCustomizeableDecoration tileEntity)
	{
		super(new ContainerInventoryDecoration(inventoryPlayer,tileEntity));
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
	{
		this.mc.getTextureManager().bindTexture(gui);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	}

}
