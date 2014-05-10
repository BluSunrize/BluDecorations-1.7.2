package bludecorations.common.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import bludecorations.api.ParticleElement;
import bludecorations.common.TileEntityCustomizeableDecoration;

public class PacketParticleElement extends AbstractPacket {

	int dim;
	int x;
	int y;
	int z;
	int iterator;
	ParticleElement element;
	
	public PacketParticleElement(){}
	public PacketParticleElement(World world, int xCoord, int yCoord, int zCoord, int i, ParticleElement re)
	{
		dim = world.provider.dimensionId;
		x = xCoord;
		y = yCoord;
		z = zCoord;
		iterator = i;
		element = re;
		
		TileEntity tileEntity = world.getTileEntity(this.x, this.y, this.z);
		if(tileEntity!= null && tileEntity instanceof TileEntityCustomizeableDecoration)
		{
			TileEntityCustomizeableDecoration tile = (TileEntityCustomizeableDecoration) tileEntity;
			int size = tile.getParticleElements().length;
			if(size <= iterator)
				size = iterator+1;

			ParticleElement[] newElements = new ParticleElement[size];
			for(int oldIt=0;oldIt<tile.getParticleElements().length;oldIt++)
				if(oldIt != iterator)
					newElements[oldIt]=tile.getParticleElements()[oldIt];

			newElements[iterator] = element.update();
			tile.setParticleElements(newElements);
			tile.getWorldObj().markBlockForUpdate(x, y, z);
		}
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		buffer.writeInt(dim);
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
		buffer.writeInt(iterator);
		element.writeToBuffer(buffer);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		this.dim = buffer.readInt();
		this.x = buffer.readInt();
		this.y = buffer.readInt();
		this.z = buffer.readInt();
		this.iterator = buffer.readInt();
		element = ParticleElement.readFromBuffer(buffer);
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
			int size = tile.getParticleElements().length;
			if(size <= iterator)
				size = iterator+1;

			ParticleElement[] newElements = new ParticleElement[size];
			for(int oldIt=0;oldIt<tile.getParticleElements().length;oldIt++)
				if(oldIt != iterator)
					newElements[oldIt]=tile.getParticleElements()[oldIt];

			newElements[iterator] = element.update();
			tile.setParticleElements(newElements);
			tile.getWorldObj().markBlockForUpdate(x, y, z);
		}
	}

}
