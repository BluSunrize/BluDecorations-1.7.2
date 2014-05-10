package bludecorations.common.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import bludecorations.common.TileEntityCustomizeableDecoration;

public class PacketTileAABBLight extends AbstractPacket {

	int dim;
	int x;
	int y;
	int z;
	float xMin;
	float xMax;
	float yMin;
	float yMax;
	float zMin;
	float zMax;
	int light;
	
	public PacketTileAABBLight(){}
	public PacketTileAABBLight(World world, int xCoord, int yCoord, int zCoord, float xMin, float xMax, float yMin, float yMax, float zMin, float zMax, int l)
	{
		this.dim = world.provider.dimensionId;
		this.x = xCoord;
		this.y = yCoord;
		this.z = zCoord;
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
		this.zMin = zMin;
		this.zMax = zMax;
		this.light = l;
		
		TileEntity tileEntity = world.getTileEntity(this.x, this.y, this.z);
		if(tileEntity!= null && tileEntity instanceof TileEntityCustomizeableDecoration)
		{
			TileEntityCustomizeableDecoration tile = (TileEntityCustomizeableDecoration) tileEntity;
			tile.setAABBLimits(new float[]{xMin,xMax,yMin,yMax, zMin, zMax});
			tile.setLightValue(light);
		}
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		buffer.writeInt(dim);
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
		buffer.writeFloat(xMin);
		buffer.writeFloat(xMax);
		buffer.writeFloat(yMin);
		buffer.writeFloat(yMax);
		buffer.writeFloat(zMin);
		buffer.writeFloat(zMax);
		buffer.writeInt(light);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		this.dim = buffer.readInt();
		this.x = buffer.readInt();
		this.y = buffer.readInt();
		this.z = buffer.readInt();
		this.xMin = buffer.readFloat();
		this.xMax = buffer.readFloat();
		this.yMin = buffer.readFloat();
		this.yMax = buffer.readFloat();
		this.zMin = buffer.readFloat();
		this.zMax = buffer.readFloat();
		this.light = buffer.readInt();
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
			tile.setAABBLimits(new float[]{xMin,xMax,yMin,yMax, zMin, zMax});
			tile.setLightValue(light);
		}
	}

}
