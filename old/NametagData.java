package farn.nametag.world;

import farn.farn_util.api.id_tracker.IDDataTracker;
import farn.nametag.NameTagMain;
import farn.nametag.listener.NameTagGlassConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.MathHelper;

public class NametagData {

    private int overriddenCount = 1;

    private final LivingEntity ent;

    private boolean canPut = true;
    private boolean despawn = false;

    private static final Identifier trackId = NameTagMain.id("name");

    public NametagData(LivingEntity self) {
        this.ent = self;
        this.setCanPut(self.nametag_canPutNameTag());
        this.putNameTagTracker();
    }

    public void write(NbtCompound nbt) {
        if(canPut) {
            nbt.putString("farnEntityName", getName());
            nbt.putInt("farnEntityTaggedName", this.overriddenCount);
        }
    }

    public void read(NbtCompound nbt) {
        if(canPut) {
            setName(nbt.getString("farnEntityName"));
            overriddenCount = nbt.getInt("farnEntityTaggedName");
        }
    }

    public boolean hasName() {
        return canPut && getName() != null && !getName().isEmpty();
    }

    public String getName() {
        return getTracker().getString(trackId);
    }

    public void setName(String string) {
        if(canPut) {
            getTracker().set(trackId, string);
            despawn = !hasName();
        }
    }

    public void putNameTag(PlayerEntity plr, ItemStack stack) {
        if(hasName()) markOverridden();
        setName(stack.getStationNbt().getString(NameTagMain.NAMETAG_ITEM_NBT_KEY));
        if(NameTagGlassConfig.instance.consumeNameTag)
            if(--stack.count <= 0)
                plr.inventory.removeStack(plr.inventory.selectedSlot, 0);
    }

    public boolean canDespawn() {
        return despawn;
    }

    public void dropNameTag() {
        if(NameTagGlassConfig.instance.consumeNameTag && hasName() && !ent.world.isRemote) {
            ent.dropItem(createNameTagItem(), 0.0F);
        }
    }

    public void markOverridden() {
        overriddenCount = MathHelper.clamp(++overriddenCount, 1, 64);
    }

    public void putNameTagTracker() {
        getTracker().startTracking(trackId, "");
    }

    public void setCanPut(boolean canPut) {
        this.canPut = canPut;
    }

    private IDDataTracker getTracker() {
        return ent.farnutil_getIdDataTracker();
    }

    public ItemStack createNameTagItem() {
        ItemStack nameTag = new ItemStack(NameTagMain.nametag_item, overriddenCount);
        nameTag.getStationNbt().putString(NameTagMain.NAMETAG_ITEM_NBT_KEY, getName());
        return nameTag;
    }
}
