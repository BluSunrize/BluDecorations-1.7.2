package bludecorations.common;

import java.text.DecimalFormat;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;

import bludecorations.api.ParticleElement;
import bludecorations.api.RenderElement;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemTool extends Item
{
	IIcon iconWrench;
	IIcon iconSpray;
	IIcon iconInstr0;
	IIcon iconInstr1;
	public ItemTool()
	{
		super();
		this.setMaxStackSize(1);
		this.setHasSubtypes(true);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List itemList)
	{	
		itemList.add(new ItemStack(item,1,0));
		itemList.add(new ItemStack(item,1,1));
		itemList.add(new ItemStack(item,1,2));
	}

	@Override
	public void registerIcons(IIconRegister ir)
	{
		iconWrench = ir.registerIcon("bludecorations:wrench");
		iconSpray = ir.registerIcon("bludecorations:spraycan");
		iconInstr0 = ir.registerIcon("bludecorations:instructions0");
		iconInstr1 = ir.registerIcon("bludecorations:instructions1");
	}
	@Override
	public IIcon getIconIndex(ItemStack stack)
	{
		switch(stack.getItemDamage())
		{
		case 0:
			return iconWrench;
		case 1:
			return iconSpray;
		case 2:
			return stack.hasTagCompound() && stack.getTagCompound().hasKey("copiedDecoration") ? iconInstr1 : iconInstr0;
		}
		return iconWrench;
	}
	@Override
	public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining)
	{
		return getIconIndex(stack);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
	{
		if(stack.getItemDamage()==2)
		{
			if(stack.hasTagCompound())
			{
				if(stack.getTagCompound().hasKey("copiedDecoration"))
				{
					NBTTagCompound decoTag = stack.getTagCompound().getCompoundTag("copiedDecoration");
					DecimalFormat df = new DecimalFormat("0");
					if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)||Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
					{
						list.add(StatCollector.translateToLocalFormatted("gui.text.lightValue", decoTag.getInteger("lightValue")));
						list.add(StatCollector.translateToLocalFormatted("gui.text.scale", df.format(decoTag.getDouble("scale")*100d)) +"%");
						list.add(StatCollector.translateToLocalFormatted("gui.text.aabb", 0.5f-decoTag.getFloat("xMin"),0.5f-decoTag.getFloat("yMin"),0.5f-decoTag.getFloat("zMin"), 0.5f+decoTag.getFloat("xMax"),0.5f+decoTag.getFloat("yMax"),0.5f+decoTag.getFloat("zMax")));
						list.add(StatCollector.translateToLocalFormatted("gui.text.orientation", decoTag.getDouble("yRotation")));

						NBTTagList renderList = decoTag.getTagList("renderElements",10);
						for(int i = 0; i < renderList.tagCount(); i++)
						{
							NBTTagCompound elementTag = renderList.getCompoundTagAt(i);
							String[] sA = RenderElement.readFromNBT(elementTag).toTranslatedString().split("!LINE!");
							for(String sAi:sA)
								list.add(sAi);
						}

					}
					else
						list.add(StatCollector.translateToLocal("gui.text.shiftForModelConfig"));

					if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)||Keyboard.isKeyDown(Keyboard.KEY_RCONTROL))
					{
						NBTTagList particleList = decoTag.getTagList("particleElements",10);
						for(int i = 0; i < particleList.tagCount(); i++)
						{
							NBTTagCompound elementTag = particleList.getCompoundTagAt(i);
							String[] sA = ParticleElement.readFromNBT(elementTag).toTranslatedString().split("!LINE!");
							for(String sAi:sA)
								list.add(sAi);
						}
					}
					else
						list.add(StatCollector.translateToLocal("gui.text.ctrlForParticleConfig"));

				}
				else
				{
					list.add(StatCollector.translateToLocal("gui.text.copyInstructions0"));
					list.add(StatCollector.translateToLocal("gui.text.copyInstructions1"));
					list.add(StatCollector.translateToLocal("gui.text.copyInstructions2"));
				}
			}
			else
				stack.setTagCompound(new NBTTagCompound());
		}
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		if(stack.getItemDamage()==2 && player.isSneaking())
			if(stack.hasTagCompound())
			{
				if(stack.getTagCompound().hasKey("copiedDecoration"))
					stack.getTagCompound().removeTag("copiedDecoration");
			}
			else
				stack.setTagCompound(new NBTTagCompound());
		return stack;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return getUnlocalizedName()+"."+stack.getItemDamage();
	}
}
