package bludecorations.common;

import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import bludecorations.api.BluDecorationsApi;
import bludecorations.api.ParticleElement;
import bludecorations.api.RenderElement;
import bludecorations.common.network.PacketPipeline;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = "bludecorations", name = "BluDecorations", version = "${version}")
public class BluDecorations {

	@Instance("bludecorations")
	public static BluDecorations instance = new BluDecorations();

	public static Block BlockDecoration;
	public static Item ItemTool;

	public static final Logger logger = Logger.getLogger("WitchingGadgets");
	public static final PacketPipeline packetPipeline = new PacketPipeline();
	
	@SidedProxy(clientSide="bludecorations.client.ClientProxy", serverSide="bludecorations.common.CommonProxy")
	public static CommonProxy proxy;


	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());

		config.load();
		config.save();

		BlockDecoration = new BlockCustomizeableDecoration().setBlockName("BluDec_CustomizeableDecoration");
		GameRegistry.registerBlock(BlockDecoration, BlockDecoration.getUnlocalizedName());
		BlockDecoration.setCreativeTab(CreativeTabs.tabDecorations);

		ItemTool = new ItemTool().setUnlocalizedName("BluDec_Tool");
		GameRegistry.registerItem(ItemTool, ItemTool.getUnlocalizedName());
		ItemTool.setCreativeTab(CreativeTabs.tabTools);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		GameRegistry.registerTileEntity(TileEntityCustomizeableDecoration.class, "TileEntityCustomizeableDecoration");
		proxy.registerRenders();
		packetPipeline.initialise();
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockDecoration,8), "dgd","psp","dgd", 'd',new ItemStack(Items.dye,1,OreDictionary.WILDCARD_VALUE), 'g',new ItemStack(Items.glowstone_dust), 'p',new ItemStack(Blocks.glass_pane), 's',"stone"));
		GameRegistry.addShapedRecipe(new ItemStack(ItemTool), "ii "," b "," ii", 'i',Items.iron_ingot, 'b',BlockDecoration);
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemTool,1,1), " f ","dbd","iii", 'i',Items.iron_ingot, 'b',BlockDecoration, 'd',"dyeBlue", 'f',Items.firework_charge));
		GameRegistry.addShapedRecipe(new ItemStack(ItemTool,1,2), " b ","ppp", 'b',BlockDecoration, 'p',Items.paper);

		BluDecorationsApi.addPresetModel("Simple Cube", new RenderElement().setModel("bludecorations","models/BluDecorations.obj").setTexture("minecraft","textures/blocks/stone.png").setPart("Cube_00").setTranslation(0.5,0,0.5).update());
		BluDecorationsApi.addPresetModel("Stone Torch", new RenderElement().setModel("bludecorations","models/BluDecorations.obj").setTexture("bludecorations","textures/models/ZeldaTorch.png").setPart("Torch_07").setTranslation(0.5,0,0.5).update());
		BluDecorationsApi.addPresetModel("Lantern", new RenderElement().setModel("bludecorations","models/BluDecorations.obj").setTexture("bludecorations","textures/models/ZeldaLantern.png").setPart("Lantern_04").setTranslation(0.5,0,0.5).update());
		BluDecorationsApi.addPresetModel("Lantern w/ Holder", new RenderElement().setModel("bludecorations","models/BluDecorations.obj").setTexture("bludecorations","textures/models/ZeldaLantern.png").setPart("Lantern_04").setTranslation(0.5,0,0.1875).update(), new RenderElement().setModel("bludecorations","models/BluDecorations.obj").setTexture("bludecorations","textures/models/ZeldaLantern.png").setPart("Lantern_Holder_04_1").setTranslation(0.5,0,0.1875).update());
		BluDecorationsApi.addPresetModel("Cage Torch", new RenderElement().setModel("bludecorations","models/BluDecorations.obj").setTexture("bludecorations","textures/models/ZeldaWWTorch.png").setPart("ZeldaWWTorch_12").setTranslation(0.5,0,0.5).update());
		BluDecorationsApi.addPresetModel("Twilit Sol", new RenderElement().setModel("bludecorations","models/BluDecorations.obj").setTexture("bludecorations","textures/models/ZeldaSol.png").setPart("ZeldaSol_11").setTranslation(0.5,0,0.5).setAlpha(0.6).update());
		BluDecorationsApi.addPresetModel("Twilit Sol w/ Holder", new RenderElement().setModel("bludecorations","models/BluDecorations.obj").setTexture("bludecorations","textures/models/ZeldaSol.png").setPart("ZeldaSol_11").setTranslation(0.5,0,0.5).setAlpha(0.6).update(), new RenderElement().setModel("bludecorations","models/BluDecorations.obj").setTexture("bludecorations","textures/models/ZeldaSol.png").setPart("ZeldaSol_Holder_11_1").setTranslation(0.5,0,0.5).update());
		BluDecorationsApi.addPresetModel("Winged Torch", new RenderElement().setModel("bludecorations","models/BluDecorations.obj").setTexture("bludecorations","textures/models/WingedTorch.png").setPart("FancyTorch_01").setTranslation(0.5,0,0.5).update());
		BluDecorationsApi.addPresetModel("Ionic Torch", new RenderElement().setModel("bludecorations","models/BluDecorations.obj").setTexture("bludecorations","textures/models/IonicTorch.png").setPart("IonicTorch_02").setTranslation(0.5,0,0.5).update());
		BluDecorationsApi.addPresetModel("Paraffin Lamp", new RenderElement().setModel("bludecorations","models/BluDecorations.obj").setTexture("bludecorations","textures/models/ParaffinLantern.png").setPart("ParaffinLantern_05").setTranslation(0.5,0,0.5).update());
		BluDecorationsApi.addPresetModel("Paraffin Lamp w/ Wall Holder", new RenderElement().setModel("bludecorations","models/BluDecorations.obj").setTexture("bludecorations","textures/models/ParaffinLantern.png").setPart("ParaffinLantern_05").setTranslation(0.8125,0,0.5).update(), new RenderElement().setModel("bludecorations","models/BluDecorations.obj").setTexture("bludecorations","textures/models/ParaffinLantern.png").setPart("ParaffinLantern_WallHolder_05_2").setTranslation(0.8125,0,0.5).update());
		BluDecorationsApi.addPresetModel("Paraffin Lamp w/ Ceiling Holder", new RenderElement().setModel("bludecorations","models/BluDecorations.obj").setTexture("bludecorations","textures/models/ParaffinLantern.png").setPart("ParaffinLantern_05").setTranslation(0.5,0.18,0.5).update(), new RenderElement().setModel("bludecorations","models/BluDecorations.obj").setTexture("bludecorations","textures/models/ParaffinLantern.png").setPart("ParaffinLantern_CeilingHolder_05_1").setTranslation(0.5,0.18,0.5).update());
		BluDecorationsApi.addPresetModel("Skull Lamp (Zombie)", new RenderElement().setModel("bludecorations","models/BluDecorations.obj").setTexture("bludecorations","textures/models/ZombieHeadLantern.png").setPart("SkullLantern_06").setTranslation(0.5,0,0.5).update(), new RenderElement().setModel("bludecorations","models/BluDecorations.obj").setTexture("bludecorations","textures/models/ZombieHeadLantern.png").setPart("SkullLantern_Jaw_06_2").setTranslation(0.5,0,0.5).update(), new RenderElement().setModel("bludecorations","models/BluDecorations.obj").setTexture("bludecorations","textures/models/Candle.png").setPart("SkullLantern_Candle_06_1").setTranslation(0.5,0,0.5).update());
		BluDecorationsApi.addPresetModel("Skull Lamp (Skeleton)", new RenderElement().setModel("bludecorations","models/BluDecorations.obj").setTexture("minecraft","textures/entity/skeleton/skeleton.png").setPart("SkullLantern_06").setTranslation(0.5,0,0.5).update(), new RenderElement().setModel("bludecorations","models/BluDecorations.obj").setTexture("minecraft","textures/entity/skeleton/skeleton.png").setPart("SkullLantern_Jaw_06_2").setTranslation(0.5,0,0.5).update(), new RenderElement().setModel("bludecorations","models/BluDecorations.obj").setTexture("bludecorations","textures/models/Candle.png").setPart("SkullLantern_Candle_06_1").setTranslation(0.5,0,0.5).update());
		BluDecorationsApi.addPresetModel("Skull Lamp (Wither)", new RenderElement().setModel("bludecorations","models/BluDecorations.obj").setTexture("minecraft","textures/entity/skeleton/wither_skeleton.png").setPart("SkullLantern_06").setTranslation(0.5,0,0.5).update(), new RenderElement().setModel("bludecorations","models/BluDecorations.obj").setTexture("minecraft","textures/entity/skeleton/wither_skeleton.png").setPart("SkullLantern_Jaw_06_2").setTranslation(0.5,0,0.5).update(), new RenderElement().setModel("bludecorations","models/BluDecorations.obj").setTexture("bludecorations","textures/models/Candle.png").setPart("SkullLantern_Candle_06_1").setTranslation(0.5,0,0.5).update());
		BluDecorationsApi.addPresetModel("Ceramic Pot", new RenderElement().setModel("bludecorations","models/BluDecorations.obj").setTexture("bludecorations","textures/models/ZeldaPot.png").setPart("ZeldaPot_10").setTranslation(0.5,0,0.5).update());
		BluDecorationsApi.addPresetModel("Wine Rack", new RenderElement().setModel("bludecorations","models/BluDecorations.obj").setTexture("minecraft","textures/blocks/planks_oak.png").setPart("Winerack_08").setTranslation(0.5,0,0.5).update(), new RenderElement().setModel("bludecorations","models/BluDecorations.obj").setTexture("minecraft","textures/blocks/planks_spruce.png").setPart("Winerack_Internal_08_2").setTranslation(0.5,0,0.5).update());
		BluDecorationsApi.addPresetModel("Wine Rack (filled)", new RenderElement().setModel("bludecorations","models/BluDecorations.obj").setTexture("minecraft","textures/blocks/planks_oak.png").setPart("Winerack_08").setTranslation(0.5,0,0.5).update(), new RenderElement().setModel("bludecorations","models/BluDecorations.obj").setTexture("minecraft","textures/blocks/planks_spruce.png").setPart("Winerack_Internal_08_2").setTranslation(0.5,0,0.5).update(), new RenderElement().setModel("bludecorations","models/BluDecorations.obj").setTexture("bludecorations","textures/models/winebottle.png").setPart("Winerack_BottleSet_08_1_3").setTranslation(0.5,0,0.5).update());


		BluDecorationsApi.addPresetParticle("Flame (small, very high)", new ParticleElement().setParticleClass("net.minecraft.client.particle.EntityFlameFX").setTranslation(0.5,1.19,0.5).update());
		BluDecorationsApi.addPresetParticle("Flame (small, high)", new ParticleElement().setParticleClass("net.minecraft.client.particle.EntityFlameFX").setTranslation(0.5,1,0.5).update());
		BluDecorationsApi.addPresetParticle("Flame (small, lowered)", new ParticleElement().setParticleClass("net.minecraft.client.particle.EntityFlameFX").setTranslation(0.5,0.38,0.5).update());
		BluDecorationsApi.addPresetParticle("Flame (small, low)", new ParticleElement().setParticleClass("net.minecraft.client.particle.EntityFlameFX").setTranslation(0.5,0.25,0.5).update());
		BluDecorationsApi.addPresetParticle("Flame (small, very low)", new ParticleElement().setParticleClass("net.minecraft.client.particle.EntityFlameFX").setTranslation(0.5,0.19,0.5).update());
		BluDecorationsApi.addPresetParticle("Flame (many, very high)", new ParticleElement().setParticleClass("net.minecraft.client.particle.EntityFlameFX").setTranslation(0.5,1.19,0.5).update(), new ParticleElement().setParticleClass("net.minecraft.client.particle.EntityFlameFX").setTranslation(0.5625,1.19,0.5).update(), new ParticleElement().setParticleClass("net.minecraft.client.particle.EntityFlameFX").setTranslation(0.5,1.19,0.5625).update(), new ParticleElement().setParticleClass("net.minecraft.client.particle.EntityFlameFX").setTranslation(0.4375,1.19,0.5).update(), new ParticleElement().setParticleClass("net.minecraft.client.particle.EntityFlameFX").setTranslation(0.5,1.19,0.4375).update());
		BluDecorationsApi.addPresetParticle("Flame (many, high)", new ParticleElement().setParticleClass("net.minecraft.client.particle.EntityFlameFX").setTranslation(0.5,1,0.5).update(), new ParticleElement().setParticleClass("net.minecraft.client.particle.EntityFlameFX").setTranslation(0.5625,1,0.5).update(), new ParticleElement().setParticleClass("net.minecraft.client.particle.EntityFlameFX").setTranslation(0.5,1,0.5625).update(), new ParticleElement().setParticleClass("net.minecraft.client.particle.EntityFlameFX").setTranslation(0.4375,1,0.5).update(), new ParticleElement().setParticleClass("net.minecraft.client.particle.EntityFlameFX").setTranslation(0.5,1,0.4375).update());
	}

	@Mod.EventHandler
	public static void postInit(FMLPostInitializationEvent event)
	{
		packetPipeline.postInitialise();
	}
}
