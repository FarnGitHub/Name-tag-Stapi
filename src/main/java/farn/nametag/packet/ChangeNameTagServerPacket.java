package farn.nametag.packet;

import farn.nametag.NameTagMain;
import farn.nametag.other.NameTagItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.api.util.SideUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.impl.item.StationNBTSetter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ChangeNameTagServerPacket extends Packet
        implements ManagedPacket<ChangeNameTagServerPacket> {

    public static final PacketType<ChangeNameTagServerPacket> TYPE =
            PacketType.builder(false, true, ChangeNameTagServerPacket::new).build();

    public int slot;
    public String tag;

    public ChangeNameTagServerPacket() {
    }

    public ChangeNameTagServerPacket(int slot, String tag) {
        this.slot = slot;
        this.tag = tag;
    }

    @Override
    public void read(DataInputStream in) {
        try {
            slot = in.readInt();
            tag = in.readUTF();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(DataOutputStream out) {
        try {
            out.writeInt(slot);
            out.writeUTF(tag);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int size() {
        return 4 + 2 + tag.length() * 2;
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
        PlayerEntity player = PlayerHelper.getPlayerFromPacketHandler(handler);
    }

    @Environment(EnvType.SERVER)
    public void handleServer(NetworkHandler handler) {
        PlayerEntity player = PlayerHelper.getPlayerFromPacketHandler(handler);
        ItemStack stack = player.inventory.getStack(slot);
        if (stack != null && stack.getItem() instanceof NameTagItem) {
            NbtCompound nbt = new NbtCompound();
            nbt.putString(NameTagMain.NAMETAG_ITEM_NBT_KEY, tag);
            StationNBTSetter.cast(stack).setStationNbt(nbt);
        }
    }

    @Override
    public PacketType<ChangeNameTagServerPacket> getType() {
        return TYPE;
    }
}