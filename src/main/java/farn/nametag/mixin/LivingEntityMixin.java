package farn.nametag.mixin;

import farn.nametag.other.EntityNameTag;
import farn.nametag.packet.UpdateClientNameTagPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.LivingEntity;
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

    private LivingEntity self = (LivingEntity)(Object)this;

    @Unique
    private boolean farn_canDespawn = false;

    @Inject(method="writeNbt", at = @At("HEAD"))
    public void onWriteNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putString("farnEntityName", (String)this.farn_EntityName);
        UpdatedClientEntityName(this.farn_EntityName);
        farn_canDespawn = !farn_hasEntityName();
    }

    @Inject(method="readNbt", at = @At("HEAD"))
    public void onReadNbt(NbtCompound nbt, CallbackInfo ci) {
        farn_setEntityName(nbt.getString("farnEntityName"));
        farn_canDespawn = !farn_hasEntityName();
    }

    @Unique
    @Override
    public boolean farn_hasEntityName() {
        return this.farn_EntityName != null && !this.farn_EntityName.isEmpty();
    }

    @Unique
    @Override
    public String farn_getEntityName() {
        return farn_EntityName;
    }

    @Unique
    @Override
    public void farn_setEntityName(String string) {
        farn_EntityName = string;
        UpdatedClientEntityName(string);
    }

    private void UpdatedClientEntityName(String string) {
        if(string != null && FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
            PacketHelper.sendToAllTracking(self, new UpdateClientNameTagPacket(self.id, string));
        }
    }

    @Inject(method = "canDespawn", at =@At("HEAD"), cancellable = true)
    public void stopDespawnWhenHaveNameTag(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(farn_canDespawn);
    }
}
