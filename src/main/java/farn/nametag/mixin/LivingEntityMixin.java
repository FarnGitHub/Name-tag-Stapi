package farn.nametag.mixin;

import farn.nametag.other.impl.Util;
import farn.nametag.other.EntityNameTag;
import farn.nametag.other.listener.NameTagConfig;
import farn.nametag.packet.EntityNameTagUpdatePacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements EntityNameTag {

    @Unique
    private int farn_taggedName = 1;

    private final LivingEntity self = (LivingEntity)(Object)this;

    private final boolean notPlayer = !(self instanceof PlayerEntity);

    @Unique
    private String farn_nametag = "";

    @Unique
    private boolean farn_canDespawn = notPlayer;

    @Inject(method="writeNbt", at = @At("HEAD"))
    public void nametag_write(NbtCompound nbt, CallbackInfo ci) {
        if(notPlayer) {
            nbt.putString("farnEntityName", nametag_getEntityNameTag());
            farn_canDespawn = !nametag_entityHasNameTag();
            nbt.putInt("farnEntityTaggedName", this.farn_taggedName);
        }
    }

    @Inject(method="readNbt", at = @At("HEAD"))
    public void nametag_read(NbtCompound nbt, CallbackInfo ci) {
        if(notPlayer) {
            nametag_setEntityNameTag(nbt.getString("farnEntityName"));
            farn_canDespawn = !nametag_entityHasNameTag();
            farn_taggedName = nbt.getInt("farnEntityTaggedName");
        }
    }

    @Unique
    @Override
    public boolean nametag_entityHasNameTag() {
        return notPlayer && nametag_getEntityNameTag() != null && !nametag_getEntityNameTag().isEmpty();
    }

    @Unique
    @Override
    public String nametag_getEntityNameTag() {
        return farn_nametag;
    }

    @Unique
    @Override
    public void nametag_setEntityNameTag(String string) {
        if(notPlayer) {
            farn_nametag = string;
        }
    }

    @Inject(method = "canDespawn", at =@At("HEAD"), cancellable = true)
    public void nametag_preventDespawn(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(farn_canDespawn);
    }

    @Inject(method = "onKilledBy", at = @At(value = "INVOKE",target = "Lnet/minecraft/entity/LivingEntity;dropItems()V"))
    public void nametag_DroppedNameTag(CallbackInfo ci) {
        if(NameTagConfig.instance.consumeNameTag && nametag_entityHasNameTag() && !self.world.isRemote) {
            ItemStack nameTag = new ItemStack(Util.farn_Nametag);
            nameTag.count = farn_taggedName;
            nameTag.getStationNbt().putString(Util.NAMETAG_ITEM_NBT_KEY, nametag_getEntityNameTag());
            self.dropItem(nameTag, 0.0F);
        }
    }

    @Override
    public void nametag_AddTaggedNamedCount() {
        if(farn_taggedName <= 512) {
            ++farn_taggedName;
        }
    }

    @Override
    public void nametag_updateClientNameTag() {
        if(FabricLoader.getInstance().getEnvironmentType().equals(EnvType.SERVER))
            PacketHelper.sendToAllTracking(self, new EntityNameTagUpdatePacket(self.id, nametag_getEntityNameTag()));
    }
}
