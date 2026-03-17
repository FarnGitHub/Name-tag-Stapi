package farn.nametag.packet;

import farn.nametag.world.NameTagItem;
import farn.nametag.NameTagMain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.api.util.SideUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.NotNull;

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

    @Environment(EnvType.CLIENT)
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
        return 4 + tag.length();
    }

    @Override
    public void apply(NetworkHandler handler) {
        SideUtil.run(
                () -> {},
                () -> handleServer(handler)
        );
    }

    @Environment(EnvType.SERVER)
    public void handleServer(NetworkHandler handler) {
        PlayerEntity player = PlayerHelper.getPlayerFromPacketHandler(handler);
        ItemStack stack = player.inventory.getStack(slot);
        if (stack != null && stack.getItem() instanceof NameTagItem)
            stack.getStationNbt().putString(NameTagMain.NAMETAG_ITEM_NBT_KEY, tag);
    }

    @Override
    public @NotNull PacketType<RenameNameTagPacket> getType() {
        return TYPE;
    }
}