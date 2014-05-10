package bludecorations.client;

import java.text.DecimalFormat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiSliderAdvanced_Vertical extends GuiSliderAdvanced
{

	ResourceLocation buttonTextures = new ResourceLocation("bludecorations:textures/gui/customwidgets.png");

	public GuiSliderAdvanced_Vertical(int id, int x, int y, int width, int height, String par5Str, double startValue, double min, double max, double step, String s, String df)
	{
		super(id, x, y, width, height, startValue, min, max, step, s, df);
	}
	
	@Override
	protected void mouseDragged(Minecraft mc, int x, int y)
	{
		if (this.visible)
		{
			if (this.dragging)
			{
				double newValue = (double)(y - (this.yPosition + 4)) / (double)(this.height - 8);

				if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
				{
					double step = (steppedMove*1000.0);
					double scaledValue = minValue + newValue * (maxValue - minValue);
//					System.out.println("newValueY "+scaledValue);
//					System.out.println("multipliedValueY "+ Math.round(scaledValue*1000));
//					System.out.println("dividedValueY "+ Math.round(scaledValue*1000 / step) );
//					System.out.println("scaledValueY "+ Math.round(scaledValue*1000 / step) * step);
//					System.out.println("roundedValueY "+ Math.round(scaledValue*1000 / step) * step / 1000.0);
					scaledValue = Math.round(scaledValue*1000 / step ) * step / 1000.0;
					newValue = (scaledValue - minValue) / (maxValue - minValue);
				}
				
				this.value = newValue;

				if (this.value < 0.0D)
				{
					this.value = 0.0D;
				}

				if (this.value > 1.0D)
				{
					this.value = 1.0D;
				}

				//this.displayString = par1Minecraft.gameSettings.getKeyBinding(this.idFloat);
			}

			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.drawTexturedModalRect(this.xPosition, this.yPosition + (int)(this.value * (float)(this.height - 8)), 20, 0, this.width, 4);
			this.drawTexturedModalRect(this.xPosition, this.yPosition + (int)(this.value * (float)(this.height - 8)) + 4, 20, 196, this.width, 4);
		}
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
			this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 0, this.width, this.height/2);
			this.drawTexturedModalRect(this.xPosition, this.yPosition + this.height/2, 0, 200 - this.height/2, this.width, this.height/2);
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
			String format = this.displayFormat;
			boolean invert = false;
			if(format.contains("-"))
			{
				invert = true;
				format = format.replaceAll("-", "");
			}
			DecimalFormat df = new DecimalFormat(format);

			String sN = this.name+" ";
			int stringYMinPos = yPosition+height/2 - ((sN.length()+2)/2)*8 -4;
			for(int iS=0;iS < sN.length(); iS++)
				this.drawCenteredString(fontrenderer, String.valueOf(sN.charAt(iS)), this.xPosition + this.width / 2, stringYMinPos + iS*8 + 4, l);

			this.drawCenteredString(fontrenderer, df.format(invert && this.getValueScaled()!=0 ? this.getValueScaled()*(-1) : this.getValueScaled()), this.xPosition + this.width / 2, stringYMinPos + sN.length()*8 + 4, l);
		}
	}

	@Override
	public boolean mousePressed(Minecraft par1Minecraft, int x, int y)
	{
		if (super.mousePressed(par1Minecraft, x, y))
		{
			this.value = (double)(y - (this.yPosition + 4)) / (double)(this.height - 8);

			if (this.value < 0.0D)
			{
				this.value = 0.0D;
			}

			if (this.value > 1.0D)
			{
				this.value = 1.0D;
			}

			this.dragging = true;
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public void drawTexturedModalRect(int par1, int par2, int par3, int par4, int par5, int par6)
	{
		float f = 0.00390625F;
		float f1 = 0.00390625F;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV((double)(par1 +    0), (double)(par2 + par6), (double)this.zLevel, (double)((float)(par3 +    0) * f), (double)((float)(par4 + par6) * f1));
		tessellator.addVertexWithUV((double)(par1 + par5), (double)(par2 + par6), (double)this.zLevel, (double)((float)(par3 + par5) * f), (double)((float)(par4 + par6) * f1));
		tessellator.addVertexWithUV((double)(par1 + par5), (double)(par2 +    0), (double)this.zLevel, (double)((float)(par3 + par5) * f), (double)((float)(par4 +    0) * f1));
		tessellator.addVertexWithUV((double)(par1 +    0), (double)(par2 +    0), (double)this.zLevel, (double)((float)(par3 +    0) * f), (double)((float)(par4 +    0) * f1));
		tessellator.draw();
	}
}