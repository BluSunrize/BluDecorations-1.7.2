package bludecorations.client;

import java.lang.reflect.Field;
import java.util.ArrayList;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import bludecorations.api.RenderElement;
import bludecorations.common.BluDecorations;
import bludecorations.common.TileEntityCustomizeableDecoration;
import bludecorations.common.network.PacketRenderElement;
import bludecorations.common.network.PacketRenderWipe;
import bludecorations.common.network.PacketTileAABBLight;
import bludecorations.common.network.PacketTileRotationScale;

public class GuiModelCustomization extends GuiScreen
{
	protected TileEntityCustomizeableDecoration tileEntity;
	private GuiTextboxAdvanced textField_modelDomain;
	private GuiTextboxAdvanced textField_modelPath;
	private GuiTextboxAdvanced textField_textureDomain;
	private GuiTextboxAdvanced textField_texturePath;
	private GuiTextboxAdvanced textField_renderPart;
	int page;

	int xSize = 0;
	int ySize = 0;

	public GuiModelCustomization(TileEntityCustomizeableDecoration te)
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
		RenderElement[] renderElements = tileEntity.getRenderElements();

		this.buttonList.clear();
		this.buttonList.add(0,new GuiButton(0, x+4, y+4, 64, 20, "Prev Page"));
		this.buttonList.add(1,new GuiButton(1, x+188, y+4, 64, 20, "Next Page"));
		if(renderElements.length<=page)
			((GuiButton)this.buttonList.get(1)).enabled=false;

		this.buttonList.add(2,new GuiButton(2, x+188, y+100, 64, 20, "Add Element"));
		this.buttonList.add(3,new GuiButton(3, x+188, y+123, 64, 20, "Del Element"));

		int startID = 4;
		if(page == 0)
		{
			((GuiButton)this.buttonList.get(3)).enabled = false;
			if(textField_modelDomain != null)
				this.textField_modelDomain = null;
			if(textField_modelPath != null)
				this.textField_modelPath = null;
			if(textField_textureDomain != null)
				this.textField_textureDomain = null;
			if(textField_texturePath != null)
				this.textField_texturePath = null;
			if(textField_renderPart != null)
				this.textField_renderPart = null;

			this.buttonList.add(startID+0,new GuiSliderAdvanced(startID+0, x+188, y+40, 64, 20, tileEntity.getOrientation()/360.0, 0.0, 360.0, 22.5, "Rotation Y", "#.##"));

			this.buttonList.add(startID+1,new GuiPresetModelList(startID+1, x+4, y+64, 80, 144, 5, this ));

			this.buttonList.add(startID+2,new GuiButton(startID+2, x+4, y+ 40, 80, 20, "Prev Page"));
			this.buttonList.add(startID+3,new GuiButton(startID+3, x+4, y+212, 80, 20, "Next Page"));

			this.buttonList.add(startID+4,new GuiSliderAdvanced(startID+4, x+188, y+64, 64, 20, tileEntity.getScale()/5.0, 0.0, 5.0, 0.1, "Scale", "0.00"));

			this.buttonList.add(startID+5,new GuiSliderAdvanced(startID+5, x+188, y+148, 64, 20, tileEntity.getLightValue()/15.0, 0.0, 15.0, 1, "Light", "0"));

			float[] aabb = tileEntity.getAABBLimits();
			this.buttonList.add(startID+ 6,new GuiSliderAdvanced(startID+ 6, x+106, y+172, 72, 20, aabb[0], 0.0, 1.0, 0.125, "Bounds X-", "0.000"));
			this.buttonList.add(startID+ 7,new GuiSliderAdvanced(startID+ 7, x+180, y+172, 72, 20, aabb[1], 0.0, 1.0, 0.125, "Bounds X+", "0.000"));
			this.buttonList.add(startID+ 8,new GuiSliderAdvanced(startID+ 8, x+106, y+194, 72, 20, aabb[2], 0.0, 1.0, 0.125, "Bounds Y-", "0.000"));
			this.buttonList.add(startID+ 9,new GuiSliderAdvanced(startID+ 9, x+180, y+194, 72, 20, aabb[3], 0.0, 1.0, 0.125, "Bounds Y+", "0.000"));
			this.buttonList.add(startID+10,new GuiSliderAdvanced(startID+10, x+106, y+216, 72, 20, aabb[4], 0.0, 1.0, 0.125, "Bounds Z-", "0.000"));
			this.buttonList.add(startID+11,new GuiSliderAdvanced(startID+11, x+180, y+216, 72, 20, aabb[5], 0.0, 1.0, 0.125, "Bounds Z+", "0.000"));

			//this.buttonList.add(startID+0,new GuiSliderAdvanced(startID+0, x+28, y+ 78, 110, 20, "", re.getRotation()[0]/360.0, 0.0, 360.0, 22.5, "Rotation X"));

		}
		else if(renderElements.length>0)
		{
			RenderElement re = renderElements[this.page-1];
			if(re!=null)
			{
				((GuiButton)this.buttonList.get(3)).enabled = true;

				this.textField_modelDomain = new GuiTextboxAdvanced(this.fontRendererObj, x+7, y+95, 56, 11);
				this.textField_modelDomain.setTextColor(-1);
				this.textField_modelDomain.setDisabledTextColour(-1);
				this.textField_modelDomain.setEnableBackgroundDrawing(false);
				this.textField_modelDomain.setMaxStringLength(2048);
				this.textField_modelDomain.setText(re.getModelDomain());
				this.textField_modelPath = new GuiTextboxAdvanced(this.fontRendererObj, x+66, y+95, 117, 11);
				this.textField_modelPath.setTextColor(-1);
				this.textField_modelPath.setDisabledTextColour(-1);
				this.textField_modelPath.setEnableBackgroundDrawing(false);
				this.textField_modelPath.setMaxStringLength(2048);
				this.textField_modelPath.setText(re.getModelPath());
				
				this.textField_textureDomain = new GuiTextboxAdvanced(this.fontRendererObj, x+7, y+131, 56, 12);
				this.textField_textureDomain.setTextColor(-1);
				this.textField_textureDomain.setDisabledTextColour(-1);
				this.textField_textureDomain.setEnableBackgroundDrawing(false);
				this.textField_textureDomain.setMaxStringLength(2048);
				this.textField_textureDomain.setText(re.getTextureDomain());
				this.textField_texturePath = new GuiTextboxAdvanced(this.fontRendererObj, x+66, y+131, 117, 12);
				this.textField_texturePath.setTextColor(-1);
				this.textField_texturePath.setDisabledTextColour(-1);
				this.textField_texturePath.setEnableBackgroundDrawing(false);
				this.textField_texturePath.setMaxStringLength(2048);
				this.textField_texturePath.setText(re.getTexturePath());
				
				this.textField_renderPart = new GuiTextboxAdvanced(this.fontRendererObj, x+7, y+158, 176, 12);
				this.textField_renderPart.setTextColor(-1);
				this.textField_renderPart.setDisabledTextColour(-1);
				this.textField_renderPart.setEnableBackgroundDrawing(false);
				this.textField_renderPart.setMaxStringLength(2048);
				this.textField_renderPart.setText(re.getPart());

				this.buttonList.add(startID+0,new GuiSliderAdvanced(startID+0, x+112, y+172, 72, 20, re.getRotation()[0]/360.0, 0.0, 360.0, 22.5, "Rot. X", "#"));
				this.buttonList.add(startID+1,new GuiSliderAdvanced(startID+1, x+112, y+194, 72, 20, re.getRotation()[1]/360.0, 0.0, 360.0, 22.5, "Rot. Y", "#"));
				this.buttonList.add(startID+2,new GuiSliderAdvanced(startID+2, x+112, y+216, 72, 20, re.getRotation()[2]/360.0, 0.0, 360.0, 22.5, "Rot. Z", "#"));

				double offsetX = (re.getTranslation()[0]+0.5) / 2;
				double offsetY = (re.getTranslation()[1]*(-1)+1.5) / 3;
				double offsetZ = (re.getTranslation()[2]+0.5) / 2;
				this.buttonList.add(startID+3,new GuiFreePointer(startID+3, x+28, y+172, 80, 80, offsetX, -0.5, 1.5, offsetZ, -0.5, 1.5, "Pos:%i%i"));
				this.buttonList.add(startID+4,new GuiSliderAdvanced_Vertical(startID+4, x+4, y+172, 20, 80, "", offsetY, 1.5, -1.5, 0.0625, "Pos Y", "0.000"));

				this.buttonList.add(startID+5,new GuiSliderAdvanced_Vertical(startID+5, x+188, y+172, 20, 80, "", re.getColour()[0], 0.0, 255.0, 16.0, "Colour R", "#"));
				this.buttonList.add(startID+6,new GuiSliderAdvanced_Vertical(startID+6, x+210, y+172, 20, 80, "", re.getColour()[1], 0.0, 255.0, 16.0, "Colour G", "#"));
				this.buttonList.add(startID+7,new GuiSliderAdvanced_Vertical(startID+7, x+232, y+172, 20, 80, "", re.getColour()[2], 0.0, 255.0, 16.0, "Colour B", "#"));
				this.buttonList.add(startID+8,new GuiSliderAdvanced(startID+8, x+188, y+150, 64, 20, re.getAlpha(), 0.0, 1, 0.1, "Alpha", "0.00"));
			}
		}
	}

	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
		int maxPage = tileEntity.getRenderElements().length;
		for(int p=maxPage;p>=0;p--)
		{
			page = p;
			this.initGui();
			this.updateTile();
		}
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
				if(this.textField_modelDomain!=null && this.textField_modelDomain.textboxKeyTyped(par1, par2))
				{
				}
				if(this.textField_modelPath!=null && this.textField_modelPath.textboxKeyTyped(par1, par2))
				{
				}
				else if(this.textField_textureDomain!=null && this.textField_textureDomain.textboxKeyTyped(par1, par2))
				{
				}
				else if(this.textField_texturePath!=null && this.textField_texturePath.textboxKeyTyped(par1, par2))
				{
				}
				else if(this.textField_renderPart!=null && this.textField_renderPart.textboxKeyTyped(par1, par2))
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
		this.updateTile();
	}

	@Override
	protected void mouseClicked(int par1, int par2, int par3)
	{
		super.mouseClicked(par1, par2, par3);
		if(page > 0)
		{
			if(textField_modelDomain != null)
				this.textField_modelDomain.mouseClicked(par1, par2, par3);
			if(textField_modelPath != null)
				this.textField_modelPath.mouseClicked(par1, par2, par3);
			if(textField_textureDomain != null)
				this.textField_textureDomain.mouseClicked(par1, par2, par3);
			if(textField_texturePath != null)
				this.textField_texturePath.mouseClicked(par1, par2, par3);
			if(textField_renderPart != null)
				this.textField_renderPart.mouseClicked(par1, par2, par3);


		}
		this.updateTile();
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
		this.updateTile();
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
			if(textField_modelDomain != null)
				this.textField_modelDomain.drawTextBox();
			if(textField_modelPath != null)
				this.textField_modelPath.drawTextBox();
			if(textField_textureDomain != null)
				this.textField_textureDomain.drawTextBox();
			if(textField_texturePath != null)
				this.textField_texturePath.drawTextBox();
			if(textField_renderPart != null)
				this.textField_renderPart.drawTextBox();

			this.fontRendererObj.drawStringWithShadow("Model", k+11, l+74, GraphicUtilities.isModelPathValid(textField_modelPath.getText()) ? 14737632 : 9125927);
			this.fontRendererObj.drawStringWithShadow("Domain:", k+8, l+84, GraphicUtilities.isModelPathValid(textField_modelPath.getText()) ? 14737632 : 9125927);
			this.fontRendererObj.drawStringWithShadow("Path:", k+70, l+84, GraphicUtilities.isModelPathValid(textField_modelPath.getText()) ? 14737632 : 9125927);
			
			this.fontRendererObj.drawStringWithShadow("Texture", k+11, l+110, GraphicUtilities.isTexturePathValid(textField_textureDomain.getText(), textField_texturePath.getText()) ? 14737632 : 9125927);
			this.fontRendererObj.drawStringWithShadow("Domain:", k+8, l+120, GraphicUtilities.isTexturePathValid(textField_textureDomain.getText(), textField_texturePath.getText()) ? 14737632 : 9125927);
			this.fontRendererObj.drawStringWithShadow("Path:", k+70, l+120, GraphicUtilities.isTexturePathValid(textField_textureDomain.getText(), textField_texturePath.getText()) ? 14737632 : 9125927);
			boolean validModelPart = false;
			if(GraphicUtilities.isModelPathValid(textField_modelPath.getText()) && GraphicUtilities.bindModel(textField_modelDomain.getText(), textField_modelPath.getText())!=null)
				validModelPart = GraphicUtilities.isModelPartValid(GraphicUtilities.bindModel(textField_modelDomain.getText(), textField_modelPath.getText()), textField_renderPart.getText());
			this.fontRendererObj.drawStringWithShadow("Model Part Name:", k+8, l+146, validModelPart ? 14737632 : 9125927);
		}
	}

	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glEnable(3042);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		String tex = "bludecorations:textures/gui/customizeScreen" + (page==0 ? "0.png" : "1.png");
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
			this.updateTile();
		}
		if(button.id == 1 && page < maxPage)
		{
			page++;
			this.initGui();
			this.updateTile();
		}
		if(button.id == 2)
		{
			try{
				updateTile();
				ArrayList<RenderElement> rList = new ArrayList<RenderElement>();
				for(RenderElement r: tileEntity.getRenderElements())
					rList.add(r);
				RenderElement nre = new RenderElement().update();
				rList.add(nre);
				tileEntity.setRenderElements(rList.toArray(new RenderElement[rList.size()]));
				page = rList.size();
				this.initGui();
				updateTile();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		if(button.id == 3)
		{
			try{
				updateTile();
				ArrayList<RenderElement> rList = new ArrayList<RenderElement>();
				for(RenderElement r: tileEntity.getRenderElements())
					rList.add(r);
				rList.remove(tileEntity.getRenderElements()[page-1]);
				BluDecorations.packetPipeline.sendToServer(new PacketRenderWipe(tileEntity.getWorldObj(), tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord));
				tileEntity.setRenderElements(rList.toArray(new RenderElement[rList.size()]));
				updateTile();
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
			if(button.id == 6)
			{
				((GuiPresetModelList)this.buttonList.get(5)).changePage(false);
			}
			if(button.id == 7)
			{
				((GuiPresetModelList)this.buttonList.get(5)).changePage(true);
			}
		}
	}

	public void updateTile()
	{
		if(page != 0)
		{
			RenderElement[] elements = tileEntity.getRenderElements();
			if(elements==null || this.page-1>=elements.length)
				return;
			RenderElement re = elements[this.page-1];
			if(re!=null)
			{
				if(textField_modelPath != null && textField_modelDomain != null)
					re.setModel(this.textField_modelDomain.getText(), this.textField_modelPath.getText());
				if(textField_texturePath != null && textField_textureDomain != null)
					re.setTexture(this.textField_textureDomain.getText(), this.textField_texturePath.getText());
				if(textField_renderPart != null)
					re.setPart(this.textField_renderPart.getText());

				int startID = 4;
				double rotX = ((GuiSliderAdvanced) this.buttonList.get(startID+0)).getValueScaled();
				double rotY = ((GuiSliderAdvanced) this.buttonList.get(startID+1)).getValueScaled();
				double rotZ = ((GuiSliderAdvanced) this.buttonList.get(startID+2)).getValueScaled();

				double offsetX = ((GuiFreePointer) this.buttonList.get(startID+3)).getValueXScaled();
				double offsetZ = ((GuiFreePointer) this.buttonList.get(startID+3)).getValueYScaled();
				double offsetY = ((GuiSliderAdvanced) this.buttonList.get(startID+4)).getValueScaled();

				//				System.out.println("offsetX "+offsetX);
				//				System.out.println("offsetZ "+offsetZ);

				double colourR = ((GuiSliderAdvanced) this.buttonList.get(startID+5)).getValueScaled()/255.0;
				double colourG = ((GuiSliderAdvanced) this.buttonList.get(startID+6)).getValueScaled()/255.0;
				double colourB = ((GuiSliderAdvanced) this.buttonList.get(startID+7)).getValueScaled()/255.0;
				double alpha = ((GuiSliderAdvanced) this.buttonList.get(startID+8)).getValueScaled();

				re.setRotation(rotX,rotY,rotZ);
				re.setTranslation(offsetX,offsetY,offsetZ);
				re.setColour(colourR,colourG,colourB);
				re.setAlpha(alpha);
				re.update();

					tileEntity.setRenderElements(new RenderElement[]{});
					for(int i=0; i<elements.length; i++)
						BluDecorations.packetPipeline.sendToServer(new PacketRenderElement(tileEntity.getWorldObj(), tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, i, elements[i]));
			}
		}
		else
		{
			int startID = 4;
			double rotY = ((GuiSliderAdvanced) this.buttonList.get(startID+0)).getValueScaled();
			double scale = ((GuiSliderAdvanced) this.buttonList.get(startID+4)).getValueScaled();

			int light = (int)Math.floor( ((GuiSliderAdvanced) this.buttonList.get(startID+5)).getValueScaled() );

			float xMin = (float) ((GuiSliderAdvanced) this.buttonList.get(startID+ 6)).getValueScaled();
			float xMax = (float) ((GuiSliderAdvanced) this.buttonList.get(startID+ 7)).getValueScaled();
			float yMin = (float) ((GuiSliderAdvanced) this.buttonList.get(startID+ 8)).getValueScaled();
			float yMax = (float) ((GuiSliderAdvanced) this.buttonList.get(startID+ 9)).getValueScaled();
			float zMin = (float) ((GuiSliderAdvanced) this.buttonList.get(startID+10)).getValueScaled();
			float zMax = (float) ((GuiSliderAdvanced) this.buttonList.get(startID+11)).getValueScaled();

			BluDecorations.packetPipeline.sendToServer(new PacketTileRotationScale(tileEntity.getWorldObj(), tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, rotY, scale));
			BluDecorations.packetPipeline.sendToServer(new PacketTileAABBLight(tileEntity.getWorldObj(), tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, xMin, xMax, yMin, yMax, zMin, zMax, light));
		}
	}

	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}
}