package bludecorations.client;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import bludecorations.api.BluDecorationsApi;
import bludecorations.api.ParticleElement;
import bludecorations.api.RenderElement;
import bludecorations.common.CommonProxy;
import bludecorations.common.TileEntityCustomizeableDecoration;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy
{
	public final static int GUISCREENACTIVEBUTTON = 8;
	public final static String QUOTEMARK = Character.toString('"');
	Configuration configModels;
	Configuration configParticles;
	
	@Override
	public void registerRenders()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCustomizeableDecoration.class, new TileRenderCustomizeableDecoration());
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tile = world.getTileEntity(x, y, z);
		if(ID == 0)return new GuiModelCustomization((TileEntityCustomizeableDecoration) tile);
		if(ID == 1)return new GuiParticleCustomization((TileEntityCustomizeableDecoration) tile);
		if(ID == 2)return new GuiInventoryDecoration(player.inventory, (TileEntityCustomizeableDecoration) tile);
		return null;
	}

	@Override
	public void grabPresetConfig(FMLPreInitializationEvent event)
	{
		try{
			File fModels = new File(event.getModConfigurationDirectory(), "bludecorations_presetModels.cfg");
			//			if(!fModels.exists())
			//				fModels.createNewFile();
			configModels = new Configuration(fModels);
			configModels.load();
			for(String catName : configModels.getCategoryNames())
			{
				ConfigCategory cat = configModels.getCategory(catName);
				if(cat != null && !cat.isEmpty())
				{	
					List<RenderElement> reList = new ArrayList();
					for(Entry<String, Property> e : cat.entrySet())
						if(e!=null)
						{
							String[] data = e.getValue().getStringList();
							if(data == null || data.length<9)
								break;
							String modelDomain= data[0].replaceAll(QUOTEMARK, "");
							String modelPath = data[1].replaceAll(QUOTEMARK, "");
							String partName = data[2].replaceAll(QUOTEMARK, "");
							String textureDomain = data[3].replaceAll(QUOTEMARK, "");
							String texturePath = data[4].replaceAll(QUOTEMARK, "");
							String[] sTransl = data[5].replaceAll(QUOTEMARK, "").split("_");
							if(sTransl.length<3)
								break;
							int[] iTransl = new int[]{Integer.parseInt(sTransl[0]),Integer.parseInt(sTransl[1]),Integer.parseInt(sTransl[2])};
							double[] translation = {iTransl[0]/100D, iTransl[1]/100D, iTransl[2]/100D};

							String[] sRot = data[6].replaceAll(QUOTEMARK, "").split("_");
							if(sRot.length<3)
								break;
							int[] iRot = new int[]{Integer.parseInt(sRot[0]),Integer.parseInt(sRot[1]),Integer.parseInt(sRot[2])};
							double[] rotation = {iRot[0]/100D, iRot[1]/100D, iRot[2]/100D};

							String[] sColour = data[7].replaceAll(QUOTEMARK, "").split("_");
							if(sColour.length<3)
								break;
							int[] iColour = new int[]{Integer.parseInt(sColour[0]),Integer.parseInt(sColour[1]),Integer.parseInt(sColour[2])};
							double[] colourModifier = {iColour[0]/255D, iColour[1]/255D, iColour[2]/255D};

							double alpha = Integer.parseInt(data[8].replaceAll(QUOTEMARK, ""))/10000D;

							RenderElement re = new RenderElement().setModel(modelDomain, modelPath).setPart(partName).setTexture(textureDomain, texturePath).setTranslation(translation[0], translation[1], translation[2]).setRotation(rotation[0], rotation[1], rotation[2]).setColour(colourModifier[0], colourModifier[1], colourModifier[2]).setAlpha(alpha);
							reList.add(re.update());
						}
					String[] split = catName.replaceAll(QUOTEMARK, "").split(" ");
					String localizedName = "";
					boolean addSpace = false;
					for(String s : split)
					{
						localizedName += (addSpace?" ":"") + s.substring(0, 1).toUpperCase()+s.substring(1);
						addSpace = true;
					}
					BluDecorationsApi.addPresetModel(localizedName, reList.toArray(new RenderElement[0]));
				}

			}
			configModels.save();

			File fParticles = new File(event.getModConfigurationDirectory(), "bludecorations_presetParticles.cfg");
			//			if(!fParticles.exists())
			//				fParticles.createNewFile();
			configParticles = new Configuration(fParticles);
			configParticles.load();
			for(String catName : configParticles.getCategoryNames())
			{
				ConfigCategory cat = configParticles.getCategory(catName);
				if(cat != null && !cat.isEmpty())
				{	
					List<ParticleElement> peList = new ArrayList();
					for(Entry<String, Property> e : cat.entrySet())
						if(e!=null)
						{
							String[] data = e.getValue().getStringList();
							if(data == null || data.length<5)
								break;
							String classPath = data[0].replaceAll(QUOTEMARK, "");
							float scale = Integer.parseInt(data[1].replaceAll(Character.toString('"'), ""))/10000F;

							String[] sTransl = data[2].replaceAll(QUOTEMARK, "").split("_");
							if(sTransl.length<3)
								break;
							int[] iTransl = new int[]{Integer.parseInt(sTransl[0]),Integer.parseInt(sTransl[1]),Integer.parseInt(sTransl[2])};
							double[] translation = {iTransl[0]/100D, iTransl[1]/100D, iTransl[2]/100D};

							String[] sColour = data[3].replaceAll(QUOTEMARK, "").split("_");
							if(sColour.length<3)
								break;
							int[] iColour = new int[]{Integer.parseInt(sColour[0]),Integer.parseInt(sColour[1]),Integer.parseInt(sColour[2])};
							float[] colourModifier = {iColour[0]/255F, iColour[1]/255F, iColour[2]/255F};

							float alpha = Integer.parseInt(data[4].replaceAll(QUOTEMARK, ""))/10000F;

							ParticleElement pe = new ParticleElement().setParticleClass(classPath).setScale(scale).setTranslation(translation[0], translation[1], translation[2]).setColour(colourModifier[0], colourModifier[1], colourModifier[2]).setAlpha(alpha);
							peList.add(pe.update());
						}
					String[] split = catName.replaceAll(QUOTEMARK, "").split(" ");
					String localizedName = "";
					boolean addSpace = false;
					for(String s : split)
					{
						localizedName += (addSpace?" ":"") + s.substring(0, 1).toUpperCase()+s.substring(1);
						addSpace = true;
					}
					BluDecorationsApi.addPresetParticle(localizedName, peList.toArray(new ParticleElement[0]));
				}

			}
			configParticles.save();
		}catch(Exception exc)
		{
			exc.printStackTrace();
		}

	}


	@Override
	public void savePresetRender(String name, RenderElement[] elements)
	{
		this.configModels.load();
		ConfigCategory cat = this.configModels.getCategory('"'+ name +'"');
		for(int r=0; r<elements.length; r++)
		{
			RenderElement re = elements[r];
			String[] data = new String[9];
			data[0] = QUOTEMARK+ re.getModelDomain() +QUOTEMARK;
			data[1] = QUOTEMARK+ re.getModelPath() +QUOTEMARK;
			data[2] = QUOTEMARK+ re.getPart() +QUOTEMARK;
			data[3] = QUOTEMARK+ re.getTextureDomain() +QUOTEMARK;
			data[4] = QUOTEMARK+ re.getTexturePath() +QUOTEMARK;
			data[5] = QUOTEMARK+ ( (int)Math.floor(re.getTranslation()[0]*100D) ) +"_"+ ( (int)Math.floor(re.getTranslation()[1]*100D) ) +"_"+ ( (int)Math.floor(re.getTranslation()[2]*100D) ) +QUOTEMARK;
			data[6] = QUOTEMARK+ ( (int)Math.floor(re.getRotation()[0]*100D) ) +"_"+ ( (int)Math.floor(re.getRotation()[1]*100D) ) +"_"+ ( (int)Math.floor(re.getRotation()[2]*100D) ) +QUOTEMARK;
			data[7] = QUOTEMARK+ ( (int)Math.floor(re.getColour()[0]*255D) ) +"_"+ ( (int)Math.floor(re.getColour()[1]*255D) ) +"_"+ ( (int)Math.floor(re.getColour()[2]*255D) ) +QUOTEMARK;
			data[8] = QUOTEMARK+ Integer.toString((int) Math.floor(re.getAlpha()*10000D)) +QUOTEMARK;
			cat.put(Integer.toString(r), new Property(Integer.toString(r), data, Property.Type.STRING));
		}
		this.configModels.save();
	}

	@Override
	public void savePresetParticles(String name, ParticleElement[] elements)
	{
		this.configParticles.load();
		ConfigCategory cat = this.configParticles.getCategory('"'+ name +'"');
		for(int p=0; p<elements.length; p++)
		{
			ParticleElement re = elements[p];
			String[] data = new String[5];
			data[0] = QUOTEMARK+ re.getParticleClass() +QUOTEMARK;
			data[1] = QUOTEMARK+ Integer.toString((int) Math.floor(re.getScale()*10000D)) +QUOTEMARK;
			data[2] = QUOTEMARK+ ( (int)Math.floor(re.getTranslation()[0]*100D) ) +"_"+ ( (int)Math.floor(re.getTranslation()[1]*100D) ) +"_"+ ( (int)Math.floor(re.getTranslation()[2]*100D) ) +QUOTEMARK;
			data[3] = QUOTEMARK+ ( (int)Math.floor(re.getColour()[0]*255D) ) +"_"+ ( (int)Math.floor(re.getColour()[1]*255D) ) +"_"+ ( (int)Math.floor(re.getColour()[2]*255D) ) +QUOTEMARK;
			data[4] = QUOTEMARK+ Integer.toString((int) Math.floor(re.getAlpha()*10000D)) +QUOTEMARK;
			cat.put(Integer.toString(p), new Property(Integer.toString(p), data, Property.Type.STRING));
		}
		this.configParticles.save();
	}

}