package farn.nametag.listener;

import farn.nametag.NameTagMain;
import net.danygames2014.unitweaks.UniTweaks;
import net.fabricmc.loader.api.FabricLoader;

public class NameTagUniTweak {

    public NameTagUniTweak() {
        NameTagMain.uniTweak = FabricLoader.getInstance().isModLoaded("unitweaks");
    }

    public static boolean disableF3EntityId() {
        return UniTweaks.USER_INTERFACE_CONFIG.disableDebugEntityIdTags;
    }
}
