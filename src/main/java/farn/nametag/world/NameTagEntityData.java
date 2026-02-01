package farn.nametag.world;

import farn.nametag.impl.NameTagMain;
import farn.nametag.listener.NameTagGlassConfig;
import farn.nametag.packet.EntityNameTagUpdatePacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.util.math.MathHelper;

public class NameTagEntityData {

    private int overriddenCount = 1;
    private String name = "";

    private final LivingEntity ent;

    private boolean canPut = true;
    private boolean despawn = false;
    private byte tick = 0;

    public NameTagEntityData(LivingEntity self) {
        this.ent = self;
    }

    public void write(NbtCompound nbt) {
        if(canPut) {
            nbt.putString("farnEntityName", getName());
            despawn = !hasName();
            nbt.putInt("farnEntityTaggedName", this.overriddenCount);
        }
    }

    public void read(NbtCompound nbt) {
        if(canPut) {
            setName(nbt.getString("farnEntityName"));
            despawn = !hasName();
            overriddenCount = nbt.getInt("farnEntityTaggedName");
        }
    }

    public boolean hasName() {
        return canPut && name != null && !name.isEmpty();
    }

    public String getName() {
        return name;
    }

    public void setName(String string) {
        if(canPut) {
            name = string;
            despawn = !hasName();
            updateClient();
        }
    }

    public boolean canDespawn() {
        return despawn;
    }

    public void dropNameTag() {
        if(NameTagGlassConfig.instance.consumeNameTag && hasName() && !ent.world.isRemote) {
            ItemStack nameTag = new ItemStack(NameTagMain.nametag_item);
            nameTag.count = overriddenCount;
            nameTag.getStationNbt().putString(NameTagMain.NAMETAG_ITEM_NBT_KEY, getName());
            ent.dropItem(nameTag, 0.0F);
        }
    }

    public void markOverridden() {
        overriddenCount = MathHelper.clamp(++overriddenCount, 1, 64);
    }

    public void updateClient() {
        if(FabricLoader.getInstance().getEnvironmentType().equals(EnvType.SERVER))
            PacketHelper.sendToAllTracking(ent, new EntityNameTagUpdatePacket(ent.id, getName()));
    }

    public void setCanPut(boolean canPut) {
        this.canPut = canPut;
    }

    public void tick() {
        if(++tick == (byte) 30) {
            if(hasName()) updateClient();
            tick = 0;
        }
    }
}
