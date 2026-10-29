package farn.nametag.impl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;

public class ServerHandler {

    @Environment(EnvType.SERVER)
    public static void kickPlayer(PlayerEntity player, String reason) {
        if (player instanceof ServerPlayerEntity serPlr) {
            serPlr.networkHandler.disconnect(reason);
        }
    }
}
