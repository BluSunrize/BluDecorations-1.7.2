package bludecorations.client;

import java.text.DecimalFormat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiSliderAdvanced extends GuiButton
{
	double value;
	double minValue;
	double maxValue;
	double steppedMove;
	boolean dragging;
	String name;
	String displayFormat;

	public GuiSliderAdvanced(int id, int x, int y, int width, int height, double startValue, double min, double max, double step, String s, String df)
	{
		super(id, x, y, width, height, "");
		this.value = startValue;
		this.minValue = min;
		this.maxValue = max;
		this.steppedMove = step;
		this.name = s;
		this.displayFormat = df;
	}

	@Override
	protected int getHoverState(boolean par1)
	{
		return 0;
	}

	public double getValueScaled()
	{
		double scale = maxValue - minValue;
		double valueScaled = minValue + this.value * scale;
		return valueScaled;
	}

	public boolean keyEntered(char c, int keyboardEvent)
	{
		try{
			DecimalFormat df = new DecimalFormat("###.##");
			String s = df.format(this.getValueScaled());
			if(!s.isEmpty())
			{
				double newValue = this.value;
				if(keyboardEvent == 14)
					s = s.substring(0, s.length()-1);
				else if(Character.isDigit(c))
						s += c;
				if(s.isEmpty() || s.equalsIgnoreCase("-"))
					s = "0";
				double parsed = df.parse(s).doubleValue();
				double scale = maxValue - minValue;
				newValue = (parsed - minValue) / scale;
				this.value = newValue;
				if (this.value < 0.0D)
					this.value = 0.0D;
				if (this.value > 1.0D)
					this.value = 1.0D;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	@Override
	protected void mouseDragged(Minecraft mc, int x, int y)
	{
		if (this.visible)
		{
			if (this.dragging)
			{
				double newValue = (double)(x - (this.xPosition + 4)) / (double)(this.width - 8);

				if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
				{
					double step = (steppedMove*1000.0);
					double scaledValue = minValue + newValue * (maxValue - minValue);
//					System.out.println("newValueX "+scaledValue);
//					System.out.println("multipliedValueX "+ Math.round(scaledValue*1000));
//					System.out.println("dividedValueX "+ Math.round(scaledValue*1000 / step) );
//					System.out.println("scaledValueX "+ Math.round(scaledValue*1000 / step) * step);
//					System.out.println("roundedValueX "+ Math.round(scaledValue*1000 / step) * step / 1000.0);
					scaledValue = Math.round(scaledValue*1000 / step ) * step / 1000.0;
					newValue = (scaledValue - minValue) / (maxValue - minValue);
				}
				
				this.value = newValue;

				if (this.value < 0.0D)
					this.value = 0.0D;

				if (this.value > 1.0D)
					this.value = 1.0D;

				//this.displayString = par1Minecraft.gameSettings.getKeyBinding(this.idFloat);
			}

			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.drawTexturedModalRect(this.xPosition + (int)(this.value * (float)(this.width - 8)), this.yPosition, 0, 66, 4, this.height);
			this.drawTexturedModalRect(this.xPosition + (int)(this.value * (float)(this.width - 8)) + 4, this.yPosition, 196, 66, 4, this.height);
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
			int k = this.getHoverState(this.field_146123_n);
			this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + k * 20, this.width / 2, this.height);
			this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + k * 20, this.width / 2, this.height);
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

			String toWrite = this.name +": "+df.format(invert && this.getValueScaled()!=0 ? this.getValueScaled()*(-1) : this.getValueScaled());
			
			int charPerLine = this.width/6;
			int iSMax = Math.max(1, toWrite.length()/charPerLine + (toWrite.length()%charPerLine != 0? 1 : 0));
			if(iSMax>1)
			{
				this.drawCenteredString(fontrenderer, toWrite.substring(0, toWrite.indexOf(":")), this.xPosition + this.width / 2, this.yPosition + (this.height - 8)/2  - ((iSMax-1)*4), l);
				this.drawCenteredString(fontrenderer, toWrite.substring(toWrite.indexOf(":")+1, toWrite.length()), this.xPosition + this.width / 2, this.yPosition + (this.height - 8)/2  - ((iSMax-1)*4) + 8, l);
			}
			else
				this.drawCenteredString(fontrenderer, toWrite, this.xPosition + this.width / 2, this.yPosition + (this.height - 8)/2 , l);
			//this.drawCenteredString(fontrenderer, toWrite, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, l);
		}
	}

	@Override
	public boolean mousePressed(Minecraft par1Minecraft, int x, int y)
	{
		if (super.mousePressed(par1Minecraft, x, y))
		{
			this.value = (double)(x - (this.xPosition + 4)) / (double)(this.width - 8);

			if (this.value < 0.0D)
				this.value = 0.0D;

			if (this.value > 1.0D)
				this.value = 1.0D;

			//par1Minecraft.gameSettings.setOptionFloatValue(this.idFloat, this.sliderValue);
			//this.displayString = par1Minecraft.gameSettings.getKeyBinding(this.idFloat);
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