package bludecorations.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;

import org.lwjgl.input.Keyboard;

import bludecorations.api.BluDecorationsApi;
import bludecorations.api.RenderElement;
import bludecorations.common.BluDecorations;
import bludecorations.common.network.PacketRenderElement;
import bludecorations.common.network.PacketRenderWipe;
import bludecorations.common.network.PacketTileAABBLight;
import bludecorations.common.network.PacketTileRotationScale;

public class GuiPresetModelList extends GuiButton
{
	static List<String> tooltip = new ArrayList();
	static{
		tooltip.add("gui.text.presetListInfo0");
		tooltip.add("gui.text.presetListInfo1");
	}
	int elementsPerPage;
	int page = 0;
	GuiModelCustomization gui;

	public GuiPresetModelList(int id, int x, int y, int width, int height, int perPage, GuiModelCustomization g)
	{
		super(id, x, y, width, height, "");
		this.elementsPerPage = perPage;
		gui = g;
	}


	@Override
	public void drawButton(Minecraft par1Minecraft, int mX, int mY)
	{
		//System.out.println(page);
		if (this.visible)
		{
			FontRenderer fontrenderer = par1Minecraft.fontRenderer;
			int heightofElement = this.height / this.elementsPerPage;
			int startOffset = (this.height - (heightofElement * this.elementsPerPage))/2;
			this.drawCenteredString(fontrenderer, "Preset Models", this.xPosition + this.width / 2, this.yPosition, 16777120);


			for(int i=0;i<elementsPerPage;i++)
			{
				int iElement = this.page*elementsPerPage +i;
				if(iElement >= BluDecorationsApi.presetModels.size())
					break;
				Entry<String,RenderElement[]> element = (Entry<String, RenderElement[]>) BluDecorationsApi.presetModels.get(iElement);

				int elemYMin = this.yPosition + startOffset + heightofElement*i;
				int elemYMax = this.yPosition + startOffset + heightofElement*(i+1);

				String s0 = null;
				String s1 = null;
				if(element.getKey().length() > 13)
				{
					s0 = element.getKey().substring(0, 13);
					s1 = element.getKey().substring(13, element.getKey().length() );
				}
				else
					s0 = element.getKey();

				int textCol = (mY>=elemYMin && mY<elemYMax && mX>=xPosition && mX<xPosition+width) ? 16777120 : 14737632;

				this.drawCenteredString(fontrenderer, s0, this.xPosition + this.width / 2, this.yPosition + startOffset + (heightofElement*i) +heightofElement/2-4, textCol);
				this.drawCenteredString(fontrenderer, s1, this.xPosition + this.width / 2, this.yPosition + startOffset + (heightofElement*i) +heightofElement/2+4, textCol);

				if(mY>=elemYMin && mY<elemYMax && mX>=xPosition && mX<xPosition+width)
				{
					GraphicUtilities.drawTooltip(gui, mX, mY, tooltip, Minecraft.getMinecraft().fontRenderer);
				}
			}
		}
	}

	@Override
	public boolean mousePressed(Minecraft par1Minecraft, int mX, int mY)
	{
		if (super.mousePressed(par1Minecraft, mX, mY))
		{
			int heightofElement = this.height / this.elementsPerPage;
			int startOffset = (this.height - (heightofElement * this.elementsPerPage))/2;

			for(int i=0;i<elementsPerPage;i++)
			{
				int iElement = this.page*elementsPerPage +i;
				if(iElement >= BluDecorationsApi.presetModels.size())
					break;
				
				Entry<String,RenderElement[]> element = (Entry<String, RenderElement[]>) BluDecorationsApi.presetModels.get(iElement);
				List<RenderElement> newList = new ArrayList(Arrays.asList(element.getValue()));
				if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL))
					newList.addAll(0,new ArrayList(Arrays.asList(gui.tileEntity.getRenderElements())));

				int elemYMin = this.yPosition + startOffset + heightofElement*i;
				int elemYMax = this.yPosition + startOffset + heightofElement*(i+1);

				if(mY >= elemYMin && mY < elemYMax)
				{
					BluDecorations.packetPipeline.sendToServer(new PacketRenderWipe(gui.tileEntity.getWorldObj(), gui.tileEntity.xCoord, gui.tileEntity.yCoord, gui.tileEntity.zCoord));
					for(int iRE=0; iRE<newList.size(); iRE++)
						BluDecorations.packetPipeline.sendToServer(new PacketRenderElement(gui.tileEntity.getWorldObj(), gui.tileEntity.xCoord, gui.tileEntity.yCoord, gui.tileEntity.zCoord, iRE, newList.get(iRE)));
					BluDecorations.packetPipeline.sendToServer(new PacketTileRotationScale(gui.tileEntity.getWorldObj(), gui.tileEntity.xCoord, gui.tileEntity.yCoord, gui.tileEntity.zCoord, gui.tileEntity.getOrientation(), gui.tileEntity.getScale()));
					BluDecorations.packetPipeline.sendToServer(new PacketTileAABBLight(gui.tileEntity.getWorldObj(), gui.tileEntity.xCoord, gui.tileEntity.yCoord, gui.tileEntity.zCoord, gui.tileEntity.getAABBLimits()[0], gui.tileEntity.getAABBLimits()[1], gui.tileEntity.getAABBLimits()[2], gui.tileEntity.getAABBLimits()[3], gui.tileEntity.getAABBLimits()[4], gui.tileEntity.getAABBLimits()[5], gui.tileEntity.getLightValue()));
					gui.initGui();
					//					gui.tileEntity.setRenderElements(element.getValue());
					//					gui.initGui();
					//					gui.updateTile(true);
				}
			}

			//par1Minecraft.gameSettings.setOptionFloatValue(this.idFloat, this.sliderValue);
			//this.displayString = par1Minecraft.gameSettings.getKeyBinding(this.idFloat);
			return true;
		}
		else
		{
			return false;
		}
	}

	public void changePage(boolean increase)
	{
		int maxPage = BluDecorationsApi.presetModels.size() /  this.elementsPerPage;
		//		if(maxPage * this.elementsPerPage != BluDecorationsApi.presetModels.size())
		//			maxPage++;

		if(increase)
			page++;
		else
			page--;
		if(page > maxPage)
		{
			page = 0;
		}
		if(page < 0)
		{
			page = maxPage;
		}
	}
}
