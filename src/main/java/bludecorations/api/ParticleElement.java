package bludecorations.api;

import io.netty.buffer.ByteBuf;

import java.text.DecimalFormat;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import bludecorations.client.GraphicUtilities;
import bludecorations.common.network.PacketPipeline;

public class ParticleElement
{
	private String classPath = "";
	private float scale = 1;
	private double[] translation = {0,0,0};
	private float[] colourModifier = {1,1,1};
	private float alpha = 1;

	/**
	 * perform this after modifying any values on a ParticleElement to avoid any fields being null
	 * @return the ParticleElement
	 */
	public ParticleElement update()
	{
		if(classPath==null)
			this.classPath = "";
		if(translation==null)
			this.translation = new double[]{0,0,0};
		if(colourModifier==null)
			this.colourModifier = new float[]{1,1,1};
		return this;
	}

	public ParticleElement setParticleClass(String s) {
		this.classPath = s;
		return this;
	}
	public String getParticleClass() {
		return this.classPath;
	}
	public ParticleElement setTranslation(double dX, double dY, double dZ) {
		this.translation = new double[]{dX,dY,dZ};
		return this;
	}
	public double[] getTranslation() {
		return this.translation;
	}
	public ParticleElement setColour(float fX, float fY, float fZ) {
		this.colourModifier = new float[]{fX,fY,fZ};
		return this;
	}
	public float[] getColour() {
		return this.colourModifier;
	}
	public ParticleElement setAlpha(float f) {
		this.alpha = f;
		return this;
	}
	public float getAlpha() {
		return this.alpha;
	}
	public ParticleElement setScale(float f) {
		this.scale = f;
		return this;
	}
	public float getScale() {
		return this.scale;
	}

	public NBTTagCompound writeToNBT()
	{
		NBTTagCompound elementTag = new NBTTagCompound();
		elementTag.setString("classPath", classPath);
		if(translation != null)
		{
			elementTag.setDouble("translationX", translation[0]);
			elementTag.setDouble("translationY", translation[1]);
			elementTag.setDouble("translationZ", translation[2]);
		}
		if(colourModifier != null)
		{
			elementTag.setFloat("colourModifierR", colourModifier[0]);
			elementTag.setFloat("colourModifierG", colourModifier[1]);
			elementTag.setFloat("colourModifierB", colourModifier[2]);
		}
		elementTag.setFloat("scale", scale);
		elementTag.setFloat("alpha", alpha);
		return elementTag;
	}

	public static ParticleElement readFromNBT(NBTTagCompound tag)
	{
		ParticleElement element = new ParticleElement();
		element.setParticleClass(tag.getString("classPath"));
		if(tag.hasKey("translationX"))
			element.setTranslation(tag.getDouble("translationX"),tag.getDouble("translationY"),tag.getDouble("translationZ"));
		if(tag.hasKey("colourModifierR"))
			element.setColour(tag.getFloat("colourModifierR"),tag.getFloat("colourModifierG"),tag.getFloat("colourModifierB"));
		element.setScale(tag.getFloat("scale"));
		element.setAlpha(tag.getFloat("alpha"));
		return element.update();
	}

	public void writeToBuffer(ByteBuf buffer)
	{
		PacketPipeline.writeStringToBuffer(classPath, buffer);
		buffer.writeDouble(translation != null ? translation[0]: 0);
		buffer.writeDouble(translation != null ? translation[1]: 0);
		buffer.writeDouble(translation != null ? translation[2]: 0);
		buffer.writeFloat(colourModifier != null ? colourModifier[0]: 0);
		buffer.writeFloat(colourModifier != null ? colourModifier[1]: 0);
		buffer.writeFloat(colourModifier != null ? colourModifier[2]: 0);
		buffer.writeFloat(scale);
		buffer.writeFloat(alpha);
	}

	public static ParticleElement readFromBuffer(ByteBuf buffer)
	{
		ParticleElement element = new ParticleElement();
		element.setParticleClass(PacketPipeline.readStringFromBuffer(buffer));
		double trX = buffer.readDouble();
		double trY = buffer.readDouble();
		double trZ = buffer.readDouble();
		element.setTranslation(trX,trY,trZ);
		float colX = buffer.readFloat();
		float colY = buffer.readFloat();
		float colZ = buffer.readFloat();
		element.setColour(colX,colY,colZ);
		element.setScale(buffer.readFloat());
		element.setAlpha(buffer.readFloat());
		return element.update();
	}
	
	public String toTranslatedString()
	{
		String result = "";
		DecimalFormat df0 = new DecimalFormat("0.####");
		DecimalFormat df1 = new DecimalFormat("0.###");
		try
		{
			String clazz = GraphicUtilities.isParticleClassValid(classPath)?classPath.subSequence(classPath.lastIndexOf(".")+1, classPath.length()).toString() : "INVALID";
			String transl = null;
			String scl = null;
			String col = null;
			String alp = null;
			if(translation != null)
				transl = StatCollector.translateToLocalFormatted("gui.text.translation", df0.format(translation[0]),df0.format(translation[1]),df0.format(translation[2]));
			if(scale!=1)
				scl = StatCollector.translateToLocalFormatted("gui.text.scale", df0.format(scale));
			if(colourModifier != null && (colourModifier[0]!=1||colourModifier[1]!=1||colourModifier[2]!=1))
				col= StatCollector.translateToLocalFormatted("gui.text.colour", df1.format(colourModifier[0]*255),df1.format(colourModifier[1]*255),df1.format(colourModifier[2]*255));
			if(alpha != 1)
				alp = StatCollector.translateToLocalFormatted("gui.text.alpha", df0.format(alpha));
			result = StatCollector.translateToLocalFormatted("gui.text.particleElementText", clazz);
			if(transl!=null)
				result += "!LINE! "+transl;
			if(scl!=null)
				result += "!LINE! "+scl;
			if(col!=null)
				result += "!LINE! "+col;
			if(alp!=null)
				result += "!LINE! "+alp;
		}
		catch(Exception e)
		{
			//e.printStackTrace();
		}
		return result;
	}
}
