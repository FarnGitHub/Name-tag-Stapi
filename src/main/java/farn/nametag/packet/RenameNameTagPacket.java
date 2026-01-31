package farn.nametag.packet;

import farn.nametag.other.NameTagItem;
import farn.nametag.other.impl.Util;
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

public class RenameNameTagPacket extends Packet
        implements ManagedPacket<RenameNameTagPacket> {

    public static final PacketType<RenameNameTagPacket> TYPE =
            PacketType.builder(false, true, RenameNameTagPacket::new).build();

    public int slot;
    public String tag;

    public RenameNameTagPacket() {
    }

    public RenameNameTagPacket(int slot, String tag) {
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
            nbt.putString(Util.NAMETAG_ITEM_NBT_KEY, tag);
            StationNBTSetter.cast(stack).setStationNbt(nbt);
        }
    }

    @Override
    public PacketType<RenameNameTagPacket> getType() {
        return TYPE;
    }
}