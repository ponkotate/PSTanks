package pstanks.client;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import pstanks.Packet.KeyPacket;
import pstanks.Tanks;
import pstanks.system.DataManager;

import java.util.ArrayList;
import java.util.List;

public class KeyHandler {

    private static Minecraft mc = FMLClientHandler.instance().getClient();

    private int a = 0;

    @SubscribeEvent
    public void key(TickEvent.ClientTickEvent event) {
        a++;

        if (mc.thePlayer != null&&event.phase.equals(TickEvent.Phase.END)&&a%5 == 0) {
            List<Integer> keyData = new ArrayList<Integer>();

            if (mc.gameSettings.keyBindForward.getIsKeyPressed()) {
                keyData.add(DataManager.keyForward);
            }
            if (mc.gameSettings.keyBindBack.getIsKeyPressed()) {
                keyData.add(DataManager.keyBack);
            }
            if (mc.gameSettings.keyBindLeft.getIsKeyPressed()) {
                keyData.add(DataManager.keyLeft);
            }
            if (mc.gameSettings.keyBindRight.getIsKeyPressed()) {
                keyData.add(DataManager.keyRight);
            }
            if (mc.gameSettings.keyBindAttack.getIsKeyPressed()) {
                keyData.add(DataManager.keyShot);
            }

            if (mc.gameSettings.keyBindJump.getIsKeyPressed()) {
                keyData.add(DataManager.keyJump);
            }

            //闇コードここまで
            DataManager.setKeyData(mc.thePlayer, keyData);

            Tanks.packetPipeline.sendToServer(new KeyPacket((keyData)));
        }
    }
}
