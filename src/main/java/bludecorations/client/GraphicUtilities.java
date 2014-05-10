package bludecorations.client;

import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.client.model.obj.GroupObject;
import net.minecraftforge.client.model.obj.WavefrontObject;

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
}