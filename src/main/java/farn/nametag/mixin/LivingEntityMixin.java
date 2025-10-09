package farn.nametag.mixin;

import farn.nametag.NameTagMain;
import farn.nametag.other.EntityNameTag;
import farn.nametag.other.listener.NameTagConfig;
import farn.nametag.packet.UpdateClientNameTagPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
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
    private String farn_EntityName = "";

    @Unique
    private int farn_taggedName = 0;

    private LivingEntity self = (LivingEntity)(Object)this;

    @Unique
    private boolean farn_canDespawn = !(self instanceof PlayerEntity);

    private final boolean notPlayer = !(self instanceof PlayerEntity);

    @Inject(method="writeNbt", at = @At("HEAD"))
    public void onWriteNbt(NbtCompound nbt, CallbackInfo ci) {
        if(notPlayer) {
            nbt.putString("farnEntityName", this.farn_EntityName);
            farn_canDespawn = !nametag_entityHasNameTag();
            nametag_updateClientNameTag();
            nbt.putInt("farnEntityTaggedName", this.farn_taggedName);
        }
    }

    @Inject(method="readNbt", at = @At("HEAD"))
    public void onReadNbt(NbtCompound nbt, CallbackInfo ci) {
        if(notPlayer) {
            nametag_setEntityNameTag(nbt.getString("farnEntityName"));
            farn_canDespawn = !nametag_entityHasNameTag();
            nametag_updateClientNameTag();
            farn_taggedName = nbt.getInt("farnEntityTaggedName");
        }
    }

    @Unique
    @Override
    public boolean nametag_entityHasNameTag() {
        return notPlayer && this.farn_EntityName != null && !this.farn_EntityName.isEmpty();
    }

    @Unique
    @Override
    public String nametag_getEntityNameTag() {
        return farn_EntityName;
    }

    @Unique
    @Override
    public void nametag_setEntityNameTag(String string) {
        if(notPlayer) {
            farn_EntityName = string;
            updateServerNameTag(string);
        }
    }

    @Inject(method = "canDespawn", at =@At("HEAD"), cancellable = true)
    public void stopDespawnWhenHaveNameTag(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(farn_canDespawn);
    }

    @Inject(method = "onKilledBy", at = @At(value = "INVOKE",target = "Lnet/minecraft/entity/LivingEntity;dropItems()V"))
    public void nametag_DroppedNameTag(Entity par1, CallbackInfo ci) {
        if(nametag_entityHasNameTag() && NameTagConfig.instance.consumeNameTag) {
            ItemStack nameTag = new ItemStack(NameTagMain.farn_Nametag);
            nameTag.count = farn_taggedName;
            nameTag.getStationNbt().putString(NameTagMain.NAMETAG_ITEM_NBT_KEY, nametag_getEntityNameTag());
            self.dropItem(nameTag, 0);
        }
    }

    @Override
    public void nametag_updateClientNameTag() {
        if(nametag_entityHasNameTag() && FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
            PacketHelper.sendToAllTracking(self, new UpdateClientNameTagPacket(self.id, nametag_getEntityNameTag()));
        }
    }

    @Override
    public void nametag_AddTaggedNamedCount() {
        if(farn_taggedName <= 512) {
            ++farn_taggedName;
        }
    }

    private void updateServerNameTag(String string) {
        if(FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
            PacketHelper.sendToAllTracking(self, new UpdateClientNameTagPacket(self.id, string));
        }
    }
}
