package farn.nametag.impl;

import farn.nametag.world.RenamerScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

public class ClientHandler {

    @Environment(EnvType.CLIENT)
    public static void openNameTagScreen() {
        if(Minecraft.INSTANCE.crosshairTarget == null || Minecraft.INSTANCE.crosshairTarget.entity == null)
            Minecraft.INSTANCE.setScreen(new RenamerScreen(Minecraft.INSTANCE.player));
    }
}
