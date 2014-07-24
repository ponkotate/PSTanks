package pstanks;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import pstanks.Packet.PacketPipeline;
import pstanks.entity.EntityChair;
import pstanks.entity.EntityChinu;
import pstanks.entity.EntityStug3;
import pstanks.item.ItemChinu;
import pstanks.item.ItemStug3;
import pstanks.system.CommonProxy;

@Mod(modid = "psTanks", name = "test")
public class Tanks {
    public static final PacketPipeline packetPipeline = new PacketPipeline();

    public Item itemChinu;
    public Item itemStug3;

    @SidedProxy(clientSide = "pstanks.client.ClientProxy", serverSide = "pstanks.system.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance("psTanks")
    public static Tanks instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        packetPipeline.initialise();

        itemChinu = new ItemChinu().setUnlocalizedName("chinu");
        GameRegistry.registerItem(itemChinu, "chinu");

        GameRegistry.addRecipe(
                new ItemStack(itemChinu, 1),
                new Object[]{
                        "SSS",
                        "SSS",
                        "SSS",
                        'S', Item.itemRegistry.getObject("iron_ingot")
                });

        itemStug3 = new ItemStug3().setUnlocalizedName("Stug3");
        GameRegistry.registerItem(itemStug3, "Stug3");

        GameRegistry.addRecipe(
                new ItemStack(itemStug3, 1),
                new Object[]{
                        "SSS",
                        "SSS",
                        "SSS",
                        'S', Item.itemRegistry.getObject("iron_ingot")
                });
    }

    @Mod.EventHandler
    public void initialise(FMLInitializationEvent evt) {
        EntityRegistry.registerModEntity(EntityChinu.class, "EntityChinu", 1, this, 250, 1, false);
        EntityRegistry.registerModEntity(EntityStug3.class, "EntityStug3", 2, this, 250, 1, false);
        EntityRegistry.registerModEntity(EntityChair.class, "EntityChair", 3, this, 250, 1, false);

        proxy.register();
    }

    @Mod.EventHandler
    public void postInitialise(FMLPostInitializationEvent evt) {
        packetPipeline.postInitialise();
    }
}
