package pstanks.system;

import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;
import java.util.List;

public class DataManager {//ハードコーディング大量の闇
    public static final int keyLeft = 1;
    public static final int keyRight = 2;
    public static final int keyForward = 3;
    public static final int keyBack = 4;
    public static final int keyShot = 5;

    private static HashMap<EntityPlayer, List<Integer>> KeyData = new HashMap<EntityPlayer, List<Integer>>();

    public static void setKeyData(EntityPlayer p, List<Integer> keys)
    {
        if(p == null)
            return;

        KeyData.put(p, keys);
    }

    public static boolean isKeyPress(EntityPlayer p, int key)
    {
        if(p == null)
            return false;

        if(KeyData.containsKey(p))
            if(KeyData.get(p).contains(key))
                return true;

        return false;
    }
}
