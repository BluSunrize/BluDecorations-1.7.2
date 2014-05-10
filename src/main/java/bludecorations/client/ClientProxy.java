package bludecorations.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import bludecorations.common.CommonProxy;
import bludecorations.common.TileEntityCustomizeableDecoration;
import cpw.mods.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy
{
	public final static int GUISCREENACTIVEBUTTON = 8;
	
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

}