package farn.nametag.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

public class MC {

    @Environment(EnvType.CLIENT)
    public static Minecraft get() {
        return Minecraft.INSTANCE;
    }
}
