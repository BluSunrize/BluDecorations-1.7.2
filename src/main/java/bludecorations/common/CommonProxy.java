package bludecorations.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import bludecorations.api.ParticleElement;
import bludecorations.api.RenderElement;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler
{
	
	public void registerRenders(){}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tile = world.getTileEntity(x, y, z);
		if(ID == 2)return new ContainerInventoryDecoration(player.inventory, (TileEntityCustomizeableDecoration) tile);
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		return null;
	}
	public void grabPresetConfig(FMLPreInitializationEvent event){}
	public void savePresetRender(String name, RenderElement[] elements){}
	public void savePresetParticles(String name, ParticleElement[] elements){}
}