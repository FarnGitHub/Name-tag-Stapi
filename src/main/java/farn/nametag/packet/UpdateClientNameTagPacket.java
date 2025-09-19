package farn.nametag.packet;

import farn.nametag.other.EntityNameTag;
import farn.nametag.NameTagMain;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.world.ClientWorld;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.api.util.SideUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class UpdateClientNameTagPacket extends Packet implements ManagedPacket<UpdateClientNameTagPacket> {
    public static final PacketType<UpdateClientNameTagPacket> TYPE =
            PacketType.builder(true, true, UpdateClientNameTagPacket::new).build();

    public int entityId;
    public String name;

    public UpdateClientNameTagPacket() {

    }

    public UpdateClientNameTagPacket(int entityId, String name) {
        this.entityId = entityId;
        this.name = name;
    }

    @Override
    public void read(DataInputStream in) {
        try {
            entityId = in.readInt();
            name = in.readUTF();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(DataOutputStream out) {
        try {
            out.writeInt(entityId);
            out.writeUTF(name);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void apply(NetworkHandler handler) {
        SideUtil.run(
                () -> handleClient(handler),
                () -> handleServer(handler)
        );
    }

    @Environment(EnvType.CLIENT)
    public void handleClient(NetworkHandler handler) {
        ClientWorld world = (ClientWorld) ((Minecraft) FabricLoader.getInstance().getGameInstance()).world;
        LivingEntity entity = (LivingEntity) world.getEntity(entityId);
        if (entity instanceof EntityNameTag tagEntity) {
            tagEntity.farn_setEntityName(name);
        }
    }

    @Environment(EnvType.SERVER)
    public void handleServer(NetworkHandler handler) {

    }

    @Override
    public int size() {
        return 4 + 2 + name.length() * 2;
    }

    @Override
    public PacketType<UpdateClientNameTagPacket> getType() {
        return TYPE;
    }
}