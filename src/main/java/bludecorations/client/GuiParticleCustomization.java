package bludecorations.client;

import java.lang.reflect.Field;
import java.util.ArrayList;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import bludecorations.api.ParticleElement;
import bludecorations.common.BluDecorations;
import bludecorations.common.TileEntityCustomizeableDecoration;
import bludecorations.common.network.PacketParticleElement;
import bludecorations.common.network.PacketParticleWipe;

public class GuiParticleCustomization extends GuiScreen
{
	protected TileEntityCustomizeableDecoration tileEntity;
	private GuiTextboxAdvanced textField_particlePath;
	int page;

	int xSize = 0;
	int ySize = 0;

	public GuiParticleCustomization(TileEntityCustomizeableDecoration te)
	{
		this.tileEntity = te;
		this.xSize = 256;
		this.ySize = 256;
		this.page = 0;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		Keyboard.enableRepeatEvents(true);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		ParticleElement[] particleElements = tileEntity.getParticleElements();

		this.buttonList.clear();
		this.buttonList.add(0,new GuiButton(0, x+4, y+4, 64, 20, "Prev Page"));
		this.buttonList.add(1,new GuiButton(1, x+188, y+4, 64, 20, "Next Page"));
		if(particleElements.length<=page)
			((GuiButton)this.buttonList.get(1)).enabled=false;
		this.buttonList.add(2,new GuiButton(2, x+188, y+100, 64, 20, "Add Element"));
		this.buttonList.add(3,new GuiButton(3, x+188, y+123, 64, 20, "Del Element"));

		int startID = 4;
		if(page == 0)
		{
			((GuiButton)this.buttonList.get(3)).enabled = false;
			if(textField_particlePath != null)
				this.textField_particlePath = null;

			this.buttonList.add(startID+0,new GuiPresetParticleList(startID+0, x+4, y+64, 80, 144, 5, this ));

			this.buttonList.add(startID+1,new GuiButton(startID+1, x+4, y+ 40, 80, 20, "Prev Page"));
			this.buttonList.add(startID+2,new GuiButton(startID+2, x+4, y+212, 80, 20, "Next Page"));

			//this.buttonList.add(startID+0,new GuiSliderAdvanced(startID+0, x+28, y+ 78, 110, 20, "", re.getRotation()[0]/360.0, 0.0, 360.0, 22.5, "Rotation X"));

		}
		else if(particleElements.length>0)
		{
			ParticleElement pe = particleElements[this.page-1];
			if(pe!=null)
			{
				((GuiButton)this.buttonList.get(3)).enabled = true;

				this.textField_particlePath = new GuiTextboxAdvanced(this.fontRendererObj, x+7, y+130, 176, 11);
				this.textField_particlePath.setTextColor(-1);
				this.textField_particlePath.setDisabledTextColour(-1);
				this.textField_particlePath.setEnableBackgroundDrawing(false);
				this.textField_particlePath.setMaxStringLength(2048);
				this.textField_particlePath.setText(pe.getParticleClass());

				this.buttonList.add(startID+0,new GuiSliderAdvanced(startID+0, x+112, y+172, 72, 20, pe.getScale(), 0.0, 1, 0.1, "Scale", "0.00"));
				double offsetX = (pe.getTranslation()[0]+0.5) / 2;
				double offsetY = (pe.getTranslation()[1]*(-1)+1.5) / 3;
				double offsetZ = (pe.getTranslation()[2]+0.5) / 2;
				this.buttonList.add(startID+1,new GuiFreePointer(startID+1, x+28, y+172, 80, 80, offsetX, -0.5, 1.5, offsetZ, -0.5, 1.5, "Pos:%i%i"));
				this.buttonList.add(startID+2,new GuiSliderAdvanced_Vertical(startID+2, x+4, y+172, 20, 80, "", offsetY, 1.5, -1.5, 0.0625, "Pos Y", "0.000"));

				this.buttonList.add(startID+3,new GuiSliderAdvanced_Vertical(startID+3, x+188, y+172, 20, 80, "", pe.getColour()[0], 0.0, 255.0, 16.0, "Colour R", "#"));
				this.buttonList.add(startID+4,new GuiSliderAdvanced_Vertical(startID+4, x+210, y+172, 20, 80, "", pe.getColour()[1], 0.0, 255.0, 16.0, "Colour G", "#"));
				this.buttonList.add(startID+5,new GuiSliderAdvanced_Vertical(startID+5, x+232, y+172, 20, 80, "", pe.getColour()[2], 0.0, 255.0, 16.0, "Colour B", "#"));
				this.buttonList.add(startID+6,new GuiSliderAdvanced(startID+6, x+188, y+150, 64, 20, pe.getAlpha(), 0.0, 1, 0.1, "Alpha", "0.00"));
			}
		}
	}

	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();
		this.updateTile(true);
		Keyboard.enableRepeatEvents(false);
	}
	@Override
	protected void keyTyped(char par1, int par2)
	{
		int mX = Mouse.getEventX() * this.width / this.mc.displayWidth;
		int mY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
		if(page > 0)
		{
			boolean cancelFurtherAction = false;
			for (int l = 0; l < this.buttonList.size(); ++l)
			{
				GuiButton guibutton = (GuiButton)this.buttonList.get(l);
				if (guibutton instanceof GuiSliderAdvanced)
					if(((GuiSliderAdvanced)guibutton).mouseOnButton(this.mc, mX, mY))
						cancelFurtherAction = ((GuiSliderAdvanced)guibutton).keyEntered(par1, par2);
			}

			if(!cancelFurtherAction)
			{
				if(this.textField_particlePath!=null && this.textField_particlePath.textboxKeyTyped(par1, par2))
				{
				}
				else
				{
					super.keyTyped(par1, par2);
				}
			}
		}
		else
			super.keyTyped(par1, par2);
		this.updateTile(false);
	}

	@Override
	protected void mouseClicked(int par1, int par2, int par3)
	{
		super.mouseClicked(par1, par2, par3);
		if(page > 0)
		{
			if(textField_particlePath != null)
				this.textField_particlePath.mouseClicked(par1, par2, par3);


		}
		this.updateTile(false);
	}
	@Override
	protected void mouseMovedOrUp(int par1, int par2, int par3)
	{
		super.mouseMovedOrUp(par1, par2, par3);


		Field selBut;
		try {
			selBut = GuiScreen.class.getDeclaredFields()[ClientProxy.GUISCREENACTIVEBUTTON];
			selBut.setAccessible(true);
			//System.out.println(selBut.get(this) != null);
			if( selBut.get(this) != null && par3 == 0)
			{
				((GuiButton)selBut.get(this)).mouseReleased(par1, par2);
				selBut.set(this, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.updateTile(false);
		//        if( this.selectedButton != null && par3 == 0)
		//        {
		//            this.selectedButton.mouseReleased(par1, par2);
		//            this.selectedButton = null;
		//        }

	}

	@Override
	public void drawScreen(int par1, int par2, float par3)
	{
		drawGuiContainerBackgroundLayer(par3, par1, par2);
		super.drawScreen(par1, par2, par3);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		GL11.glDisable(GL11.GL_LIGHTING);
		if(page > 0)
		{
			if(textField_particlePath != null)
				this.textField_particlePath.drawTextBox();

			this.fontRendererObj.drawStringWithShadow("Class Path:", k+8, l+120, GraphicUtilities.isParticleClassValid(textField_particlePath.getText()) ? 14737632 : 9125927);
		}
	}

	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glEnable(3042);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		String tex = "bludecorations:textures/gui/particleCustomizeScreen" + (page==0 ? "0.png" : "1.png");
		this.mc.getTextureManager().bindTexture(new ResourceLocation(tex));
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l,  0,  0, xSize, ySize);
	}

	@Override
	protected void actionPerformed(GuiButton button)
	{
		super.actionPerformed(button);
		int maxPage = tileEntity.getRenderElements().length;
		if(button.id == 0 && page > 0)
		{
			page--;
			this.initGui();
		}
		if(button.id == 1 && page < maxPage)
		{
			page++;
			this.initGui();
		}
		if(button.id == 2)
		{
			try{
				updateTile(true);
				ArrayList<ParticleElement> rList = new ArrayList<ParticleElement>();
				for(ParticleElement r: tileEntity.getParticleElements())
					rList.add(r);
				ParticleElement nre = new ParticleElement().update();
				rList.add(nre);
				tileEntity.setParticleElements(rList.toArray(new ParticleElement[rList.size()]));
				page = rList.size();
				this.initGui();
				updateTile(true);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		if(button.id == 3)
		{
			try{
				updateTile(true);
				ArrayList<ParticleElement> rList = new ArrayList<ParticleElement>();
				for(ParticleElement r: tileEntity.getParticleElements())
					rList.add(r);
				rList.remove(tileEntity.getRenderElements()[page-1]);
				BluDecorations.packetPipeline.sendToServer(new PacketParticleWipe(tileEntity.getWorldObj(), tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord));
				tileEntity.setParticleElements(rList.toArray(new ParticleElement[rList.size()]));
				updateTile(true);
				page = 0;
				this.initGui();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		if(page == 0)
		{
			if(button.id == 5)
			{
				((GuiPresetParticleList)this.buttonList.get(4)).changePage(false);
			}
			if(button.id == 6)
			{
				((GuiPresetParticleList)this.buttonList.get(4)).changePage(true);
			}
		}
	}

	public void updateTile(boolean sendPacket)
	{
		if(page != 0)
		{
			ParticleElement[] elements = tileEntity.getParticleElements();
			if(elements==null || this.page-1>=elements.length)
				return;
			ParticleElement pe = elements[this.page-1];
			if(pe!=null)
			{
				if(textField_particlePath != null)
					pe.setParticleClass(this.textField_particlePath.getText());

				int startID = 4;

				float scale = (float) ((GuiSliderAdvanced) this.buttonList.get(startID+0)).getValueScaled();

				double offsetX = ((GuiFreePointer) this.buttonList.get(startID+1)).getValueXScaled();
				double offsetZ = ((GuiFreePointer) this.buttonList.get(startID+1)).getValueYScaled();
				double offsetY = ((GuiSliderAdvanced) this.buttonList.get(startID+2)).getValueScaled();

				float colourR = (float) (((GuiSliderAdvanced) this.buttonList.get(startID+3)).getValueScaled()/255.0);
				float colourG = (float) (((GuiSliderAdvanced) this.buttonList.get(startID+4)).getValueScaled()/255.0);
				float colourB = (float) (((GuiSliderAdvanced) this.buttonList.get(startID+5)).getValueScaled()/255.0);
				float alpha = (float) ((GuiSliderAdvanced) this.buttonList.get(startID+6)).getValueScaled();

				pe.setTranslation(offsetX,offsetY,offsetZ);
				pe.setColour(colourR,colourG,colourB);
				pe.setAlpha(alpha);
				pe.setScale(scale);
				pe.update();

				if(sendPacket)
				{
					tileEntity.setParticleElements(new ParticleElement[]{});
					for(int i=0; i<elements.length; i++)
						BluDecorations.packetPipeline.sendToServer(new PacketParticleElement(tileEntity.getWorldObj(), tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, i, elements[i]));
				}
				else
				{
					elements[this.page-1] = pe;
					tileEntity.setParticleElements(elements);
				}
			}
		}
		else
		{
		}
	}

	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}
}