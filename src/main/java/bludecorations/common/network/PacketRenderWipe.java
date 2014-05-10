package bludecorations.common.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import bludecorations.api.RenderElement;
import bludecorations.common.TileEntityCustomizeableDecoration;

public class PacketRenderWipe extends AbstractPacket {

	int dim;
	int x;
	int y;
	int z;
	
	public PacketRenderWipe(){}
	public PacketRenderWipe(World world, int xCoord, int yCoord, int zCoord)
	{
		dim = world.provider.dimensionId;
		x = xCoord;
		y = yCoord;
		z = zCoord;
		
		TileEntity tileEntity = world.getTileEntity(this.x, this.y, this.z);
		if(tileEntity!= null && tileEntity instanceof TileEntityCustomizeableDecoration)
		{
			TileEntityCustomizeableDecoration tile = (TileEntityCustomizeableDecoration) tileEntity;
			tile.setRenderElements(new RenderElement[]{});
		}
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		buffer.writeInt(dim);
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		this.dim = buffer.readInt();
		this.x = buffer.readInt();
		this.y = buffer.readInt();
		this.z = buffer.readInt();
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		// TODO Auto-generated method stub
	}

	@Override
	public void handleServerSide(EntityPlayer player)
	{
		World world = DimensionManager.getWorld(this.dim);
		if (world == null) return;
		TileEntity tileEntity = world.getTileEntity(this.x, this.y, this.z);
		if(tileEntity!= null && tileEntity instanceof TileEntityCustomizeableDecoration)
		{
			TileEntityCustomizeableDecoration tile = (TileEntityCustomizeableDecoration) tileEntity;
			tile.setRenderElements(new RenderElement[]{});
		}
	}

}
