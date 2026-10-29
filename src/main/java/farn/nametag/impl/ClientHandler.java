package farn.nametag.impl;

import farn.nametag.listener.NameTagStationAPI;
import farn.nametag.world.RenamerScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.api.network.packet.MessagePacket;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;

public class ClientHandler {

    @Environment(EnvType.CLIENT)
    public static void openNameTagScreen() {
        if(Minecraft.INSTANCE.crosshairTarget == null || Minecraft.INSTANCE.crosshairTarget.entity == null)
            Minecraft.INSTANCE.setScreen(new RenamerScreen(Minecraft.INSTANCE.player));
    }

    @Environment(EnvType.CLIENT)
    public static void renameNameTag(int slot, String name) {
        MessagePacket packet = new MessagePacket(NameTagStationAPI.NAMESPACE.id("rename_nametag"));
        packet.ints = new int[]{slot};
        packet.strings = new String[]{name};
        PacketHelper.send(packet);
    }
}
