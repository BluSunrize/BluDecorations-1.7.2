package bludecorations.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.client.model.obj.GroupObject;
import net.minecraftforge.client.model.obj.WavefrontObject;

import org.lwjgl.opengl.GL11;

public class GraphicUtilities
{
	static HashMap<String,IModelCustom> modelMap = new HashMap<String,IModelCustom>();
	static HashMap<String,ResourceLocation> textureMap = new HashMap<String,ResourceLocation>();
	static Minecraft mc = Minecraft.getMinecraft();

	public static void bindTexture(String domain, String path)
	{
		String savePath = domain+":"+path;
		ResourceLocation rl = textureMap.containsKey(savePath) ? textureMap.get(savePath) : new ResourceLocation(domain,path);
		if(!textureMap.containsKey(savePath))
			textureMap.put(savePath, rl);
		mc.getTextureManager().bindTexture(rl);
	}

	public static IModelCustom bindModel(String domain, String path)
	{
		String savePath = domain+":"+path;
		if(modelMap.containsKey(savePath))
			return modelMap.get(savePath);
		else
		{
			try
			{
				IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation(domain,path));
				modelMap.put(savePath, model);
				return model;
			}
			catch(Exception e)
			{
				//System.out.println("[BluDecorations] Error on attempt to load model:");
				//e.printStackTrace();
				return null;
			}
		}
	}

	public static boolean isModelPathValid(String path)
	{
		int i = path.lastIndexOf('.');
		if (i == -1)
			return false;
		String suffix = path.substring(i+1);
		if(!AdvancedModelLoader.getSupportedSuffixes().contains(suffix))
			return false;
		//URL resource = AdvancedModelLoader.class.getResource(path);
		//if (resource == null)
		//	return false;
		return true;
	}

	public static boolean isTexturePathValid(String domain, String path)
	{
		boolean test = true;
		if(test)
			return true;
		String savePath = domain+":"+path;
		ResourceLocation rl = textureMap.containsKey(savePath) ? textureMap.get(savePath) : new ResourceLocation(path);
		try{	
			mc.getResourceManager().getResource(rl);
		}
		catch(Exception exc)
		{
			return false;
		}
		if(!textureMap.containsKey(savePath))
			textureMap.put(savePath, rl);
		return true;
	}

	public static boolean isModelPartValid(IModelCustom model, String part)
	{
		if(model instanceof WavefrontObject)
		{
			for (GroupObject groupObject : ((WavefrontObject)model).groupObjects)
			{
				if (part.equalsIgnoreCase(groupObject.name))
				{
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isParticleClassValid(String path)
	{
		try
		{
			Class<EntityFX> c = (Class<EntityFX>) Class.forName(path);
			c.getName();
		}
		catch(Exception e)
		{
			return false;
		}
		return true;
	}
	
	public static void drawTooltip(GuiScreen gui, int mX, int mY, List<String> tooltip, FontRenderer font)
	{
		if ((tooltip == null) || (tooltip.isEmpty())) {
			return;
		}
		GL11.glDisable(32826);
		GL11.glDisable(2896);
		GL11.glDisable(2929);
		int k = 0;
		Iterator iterator = tooltip.iterator();
		while (iterator.hasNext())
		{
			String s = StatCollector.translateToLocal((String)iterator.next());
			int l = font.getStringWidth(s);
			if (l > k) {
				k = l;
			}
		}
		int i1 = mX + 12;
		int j1 = mY - 12;
		int k1 = 8;
		if (tooltip.size() > 1) {
			k1 += 2 + (tooltip.size() - 1) * 10;
		}
		if (i1 + k > gui.width) {
			i1 -= 28 + k;
		}
		if (j1 + k1 + 6 > gui.height) {
			j1 = gui.height - k1 - 6;
		}
		//gui.zLevel = 300.0F;
		//GuiContainer.itemRenderer.zLevel = 300.0F;
		int l1 = -267386864;
		drawGradientRect(i1 - 3, j1 - 4, i1 + k + 3, j1 - 3, l1, l1);
		drawGradientRect(i1 - 3, j1 + k1 + 3, i1 + k + 3, j1 + k1 + 4, l1, l1);
		drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 + k1 + 3, l1, l1);
		drawGradientRect(i1 - 4, j1 - 3, i1 - 3, j1 + k1 + 3, l1, l1);
		drawGradientRect(i1 + k + 3, j1 - 3, i1 + k + 4, j1 + k1 + 3, l1, l1);
		int i2 = 1347420415;
		int j2 = (i2 & 0xFEFEFE) >> 1 | i2 & 0xFF000000;
		drawGradientRect(i1 - 3, j1 - 3 + 1, i1 - 3 + 1, j1 + k1 + 3 - 1, i2, j2);
		drawGradientRect(i1 + k + 2, j1 - 3 + 1, i1 + k + 3, j1 + k1 + 3 - 1, i2, j2);
		drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 - 3 + 1, i2, i2);
		drawGradientRect(i1 - 3, j1 + k1 + 2, i1 + k + 3, j1 + k1 + 3, j2, j2);
		for (int k2 = 0; k2 < tooltip.size(); k2++)
		{
			String s1 = (String)tooltip.get(k2);
			font.drawStringWithShadow(StatCollector.translateToLocal(s1), i1, j1, -1);
			if (k2 == 0) {
				j1 += 2;
			}
			j1 += 10;
		}
		//gui.zLevel = 0.0F;
		//GuiContainer.itemRenderer.zLevel = 0.0F;
		//		GL11.glEnable(2896);
		GL11.glEnable(2929);
		GL11.glEnable(32826);
	}
	
	public static void drawGradientRect(int par1, int par2, int par3, int par4, int par5, int par6)
	{
		float f = (float)(par5 >> 24 & 255) / 255.0F;
		float f1 = (float)(par5 >> 16 & 255) / 255.0F;
		float f2 = (float)(par5 >> 8 & 255) / 255.0F;
		float f3 = (float)(par5 & 255) / 255.0F;
		float f4 = (float)(par6 >> 24 & 255) / 255.0F;
		float f5 = (float)(par6 >> 16 & 255) / 255.0F;
		float f6 = (float)(par6 >> 8 & 255) / 255.0F;
		float f7 = (float)(par6 & 255) / 255.0F;
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.setColorRGBA_F(f1, f2, f3, f);
		tessellator.addVertex((double)par3, (double)par2, (double)500);
		tessellator.addVertex((double)par1, (double)par2, (double)500);
		tessellator.setColorRGBA_F(f5, f6, f7, f4);
		tessellator.addVertex((double)par1, (double)par4, (double)500);
		tessellator.addVertex((double)par3, (double)par4, (double)500);
		tessellator.draw();
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
}