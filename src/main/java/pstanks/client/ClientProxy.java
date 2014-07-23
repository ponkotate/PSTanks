package pstanks.client;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import pstanks.client.render.RenderChinu;
import pstanks.client.render.RenderStug3;
import pstanks.entity.EntityChinu;
import pstanks.system.CommonProxy;

public class ClientProxy extends CommonProxy {

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        //GUI

        return null;
    }

    @Override
    public void register(){
        FMLCommonHandler.instance().bus().register(new KeyHandler());
        RenderingRegistry.registerEntityRenderingHandler(EntityChinu.class, new RenderChinu());
        RenderingRegistry.registerEntityRenderingHandler(EntityBoat.class, new RenderStug3());
    }
}
