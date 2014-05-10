package bludecorations.client;

import java.text.DecimalFormat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiFreePointer extends GuiButton
{
	double valueX;
	double valueY;
	double minValueX;
	double maxValueX;
	double minValueY;
	double maxValueY;
	boolean dragging;
	String name;

	ResourceLocation buttonTextures = new ResourceLocation("bludecorations:textures/gui/customwidgets.png");


	public GuiFreePointer(int id, int x, int y, int width, int height, double startX, double minX, double maxX, double startY, double minY, double maxY, String s)
	{
		super(id, x, y, width, height, "");
		this.valueX = startX;
		this.valueY = startY;
		this.minValueX = minX;
		this.maxValueX = maxX;
		this.minValueY = minY;
		this.maxValueY = maxY;
		this.name = s;
	}

	@Override
	protected int getHoverState(boolean par1)
	{
		return 0;
	}

	public double getValueXScaled()
	{
		double scale = maxValueX - minValueX;
		double valueScaled = minValueX + this.valueX * scale;
		return valueScaled;
	}
	public double getValueYScaled()
	{
		double scale = maxValueY- minValueY;
		double valueScaled = minValueY + this.valueY * scale;
		return valueScaled;
	}

	@Override
	protected void mouseDragged(Minecraft mc, int x, int y)
	{
		if (this.visible)
		{
			if (this.dragging)
			{
				double newValueX = (double)(x - (this.xPosition + 4)) / (double)(this.width - 8);
				double newValueY = (double)(y - (this.yPosition + 4)) / (double)(this.height - 8);

				if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
				{
					double step = (0.0625*10000.0);
					double scaledValueX = minValueX + newValueX * (maxValueX - minValueX);
					scaledValueX = Math.round(scaledValueX*10000 / step ) * step / 10000.0;
					newValueX = (scaledValueX - minValueX) / (maxValueX - minValueX);
					
					double scaledValueY = minValueY + newValueY * (maxValueY - minValueY);
					scaledValueY = Math.round(scaledValueY*10000 / step ) * step / 10000.0;
					newValueY = (scaledValueY - minValueY) / (maxValueY - minValueY);
				}

				this.valueX = newValueX;
				this.valueY = newValueY;



				if (this.valueX < 0.0D)
					this.valueX = 0.0D;
				if (this.valueX > 1.0D)
					this.valueX = 1.0D;

				if (this.valueY < 0.0D)
					this.valueY = 0.0D;
				if (this.valueY > 1.0D)
					this.valueY = 1.0D;
			}

			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.drawTexturedModalRect(this.xPosition + (int)(this.valueX * (float)(this.width - 8)), this.yPosition + (int)(this.valueY * (float)(this.height - 8)), 60, 0, 8, 8);
		}
	}

	public boolean mouseOnButton(Minecraft par1Minecraft, int par2, int par3)
	{
		return par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
	}

	@Override
	public void drawButton(Minecraft par1Minecraft, int par2, int par3)
	{
		if (this.visible)
		{
			FontRenderer fontrenderer = par1Minecraft.fontRenderer;
			par1Minecraft.getTextureManager().bindTexture(buttonTextures);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.field_146123_n = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
			this.drawTexturedModalRect(this.xPosition, this.yPosition, 68, 0, this.width, this.height);
			this.mouseDragged(par1Minecraft, par2, par3);
			int l = 14737632;

			if (!this.enabled)
			{
				l = -6250336;
			}
			else if (this.field_146123_n)
			{
				l = 16777120;
			}
			DecimalFormat df = new DecimalFormat("0.000");

			String name = this.name;
			String sX = (this.getValueXScaled()<0?"":" ")+df.format(this.getValueXScaled());
			String sY = (this.getValueYScaled()<0?"":" ")+df.format(this.getValueYScaled());
			
			name = name.replaceFirst("%i", sX);
			name = name.replaceFirst("%i", sY);
			this.drawCenteredString(fontrenderer, name, this.xPosition + this.width / 2, this.yPosition, l);
		}
	}

	@Override
	public boolean mousePressed(Minecraft par1Minecraft, int x, int y)
	{
		if (super.mousePressed(par1Minecraft, x, y))
		{
			double newValueX = (double)(x - (this.xPosition + 4)) / (double)(this.width - 8);
			double newValueY = (double)(y - (this.yPosition + 4)) / (double)(this.height - 8);

			this.valueX = newValueX;
			this.valueY = newValueY;

			if (this.valueX < 0.0D)
				this.valueX = 0.0D;
			if (this.valueX > 1.0D)
				this.valueX = 1.0D;

			if (this.valueY < 0.0D)
				this.valueY = 0.0D;
			if (this.valueY > 1.0D)
				this.valueY = 1.0D;

			this.dragging = true;
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public void mouseReleased(int par1, int par2)
	{
		this.dragging = false;
	}
}