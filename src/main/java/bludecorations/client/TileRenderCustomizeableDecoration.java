package bludecorations.client;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import bludecorations.api.RenderElement;
import bludecorations.common.TileEntityCustomizeableDecoration;

public class TileRenderCustomizeableDecoration extends TileEntitySpecialRenderer {


	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float tick)
	{
		TileEntityCustomizeableDecoration tile = (TileEntityCustomizeableDecoration) tileEntity;
		RenderElement[] renderElements = tile.getRenderElements();

		GL11.glPushMatrix();
		GL11.glColor4d(1,1,1,1);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770,771);
		GL11.glTranslated(x, y, z);
		for(RenderElement element: renderElements)
		{
			if(element!=null)
			{
				try
				{
//					if(!GraphicUtilities.isModelPathValid(element.getModel())
//						/*||*/if(	!GraphicUtilities.isTexturePathValid(element.getTexture()) )
//						break;
					GL11.glPushMatrix();

					GraphicUtilities.bindTexture(element.getTextureDomain(),element.getTexturePath());
					GL11.glColor4d(element.getColour()[0], element.getColour()[1], element.getColour()[2], element.getAlpha());
					GL11.glTranslated(0.5,0.5,0.5);
					GL11.glRotated(tile.getOrientation(), 0, 1, 0);
					GL11.glTranslated(-0.5,-0.5,-0.5);
					GL11.glTranslated(element.getTranslation()[0], element.getTranslation()[1], element.getTranslation()[2]);

					
					if(element.getRotation()[2] != 0)
						GL11.glRotated(element.getRotation()[2], 0, 0, 1);
					if(element.getRotation()[1] != 0)
						GL11.glRotated(element.getRotation()[1], 0, 1, 0);
					if(element.getRotation()[0] != 0)
						GL11.glRotated(element.getRotation()[0], 1, 0, 0);

					GL11.glScaled(tile.getScale(),tile.getScale(),tile.getScale());
					if(GraphicUtilities.isModelPathValid(element.getModelDomain()+":"+element.getModelPath()))
					{
						IModelCustom tempMod = GraphicUtilities.bindModel(element.getModelDomain(),element.getModelPath());
						if(tempMod != null && GraphicUtilities.isModelPartValid(tempMod, element.getPart()))
							tempMod.renderPart(element.getPart());
					}
					GL11.glPopMatrix();
				}
				catch(Exception except)
				{
					//except.printStackTrace();
					GL11.glPopMatrix();
				}
			}
		}

		GL11.glDisable(3042);
		GL11.glColor4d(1,1,1,1);
		GL11.glPopMatrix();
	}


}