package farn.nametag.packet;

import farn.nametag.impl.NameTagMain;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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

public class EntityNameTagUpdatePacket extends Packet implements ManagedPacket<EntityNameTagUpdatePacket> {
    public static final PacketType<EntityNameTagUpdatePacket> TYPE =
            PacketType.builder(true, true, EntityNameTagUpdatePacket::new).build();

    public int entityId;
    public String name;

    public EntityNameTagUpdatePacket() {

    }

    public EntityNameTagUpdatePacket(int entityId, String name) {
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
                () -> {}
        );
    }

    @Environment(EnvType.CLIENT)
    public void handleClient(NetworkHandler handler) {
        if(NameTagMain.getMinecraft().world instanceof ClientWorld world
            && world.getEntity(entityId) instanceof LivingEntity entity)
                 entity.nametag_getNametagData().setName(name);
    }

    @Override
    public int size() {
        return 4 + 2 + name.length() * 2;
    }

    @Override
    public PacketType<EntityNameTagUpdatePacket> getType() {
        return TYPE;
    }
}