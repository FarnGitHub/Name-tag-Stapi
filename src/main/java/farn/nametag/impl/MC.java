package farn.nametag.impl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.util.hit.HitResultType;

public class MC {


    @Environment(EnvType.CLIENT)
    public static Minecraft get() {
        return Minecraft.INSTANCE;
    }

    @Environment(EnvType.CLIENT)
    public static boolean isNotPointingAtEntity() {
        return get().crosshairTarget == null || get().crosshairTarget.type != HitResultType.ENTITY;
    }
}
