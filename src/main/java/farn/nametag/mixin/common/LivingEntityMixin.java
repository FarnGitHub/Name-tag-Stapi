package farn.nametag.mixin.common;

import farn.farn_util.api.id_tracker.IDDataTracker;
import farn.nametag.NameTagMain;
import farn.nametag.impl.NameTagEntity;
import farn.nametag.listener.NameTagGlassConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin extends EntityMixin implements NameTagEntity {

    @Inject(method="writeNbt", at = @At("HEAD"))
    public void nametag_write(NbtCompound nbt, CallbackInfo ci) {
        if(!nametag_canPutNameTag()) return;
        nbt.putString("farnEntityName", nametag_getName());
    }

    @Inject(method="readNbt", at = @At("HEAD"))
    public void nametag_read(NbtCompound nbt, CallbackInfo ci) {
        if(!nametag_canPutNameTag()) return;
        nametag_setName(nbt.getString("farnEntityName"));
    }

    @Inject(method="tryDespawn", at = @At("HEAD"), cancellable = true)
    public void nametag_preventDespawn(CallbackInfo ci) {
        if(nametag_hasName()) ci.cancel();
    }

    @Inject(method="<init>", at = @At("TAIL"))
    public void nametag_init(CallbackInfo ci) {
        nametag_getDataTracker().startTracking(NameTagMain.id("name"), "");
    }

    @Override
    public void nametag_markDead(CallbackInfo ci) {
        this.nametag_dropNameTag();
    }

    @Override
    public boolean nametag_hasName() {
        return nametag_canPutNameTag() && !nametag_getName().isEmpty();
    }

    @Override
    public String nametag_getName() {
        return nametag_getDataTracker().getString(NameTagMain.id("name"));
    }

    @Override
    public void nametag_setName(String string) {
        nametag_getDataTracker().set(NameTagMain.id("name"), string);
    }

    @Override
    public void nametag_putNameTag(PlayerEntity plr, ItemStack stack) {
        nametag_setName(stack.getStationNbt().getString(NameTagMain.NAMETAG_ITEM_NBT_KEY));
        if(NameTagGlassConfig.instance.consumeNameTag)
            if(--stack.count <= 0)
                plr.inventory.removeStack(plr.inventory.selectedSlot, 0);
    }

    @Override
    public void nametag_dropNameTag() {
        if(NameTagGlassConfig.instance.consumeNameTag && nametag_hasName() && !this.world.isRemote) {
            this.dropItem(nametag_createNameTagItem(), 0.0F);
        }
    }

    @Override
    public ItemStack nametag_createNameTagItem() {
        ItemStack nameTag = new ItemStack(NameTagMain.nametag_item, 1);
        nameTag.getStationNbt().putString(NameTagMain.NAMETAG_ITEM_NBT_KEY, nametag_getName());
        return nameTag;
    }

    @Unique
    private IDDataTracker nametag_getDataTracker() {
        return ((LivingEntity)(Object)this).farnutil_getIdDataTracker();
    }
}
