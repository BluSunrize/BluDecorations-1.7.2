package bludecorations.api;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Map.Entry;

public class BluDecorationsApi
{

	public static ArrayList<Entry<String,RenderElement[]>> presetModels = new ArrayList<Entry<String,RenderElement[]>>();
	public static ArrayList<Entry<String,ParticleElement[]>> presetParticles = new ArrayList<Entry<String,ParticleElement[]>>();

	public static void addPresetModel(String name, RenderElement... model)
	{
		Entry e = new SimpleEntry(name, model);
		presetModels.add(e);
	}
	
	public static void addPresetParticle(String name, ParticleElement... model)
	{
		Entry e = new SimpleEntry(name, model);
		presetParticles.add(e);
	}
}
