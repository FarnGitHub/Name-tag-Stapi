package farn.nametag.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import farn.nametag.world.NameTagEntityData;
import farn.nametag.impl.NameTagEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements NameTagEntity {

    @Unique
    private NameTagEntityData nametag_impl;

    @Override
    public NameTagEntityData nametag_getNametagData() {
        return nametag_impl;
    }

    @Inject(method="writeNbt", at = @At("HEAD"))
    public void nametag_write(NbtCompound nbt, CallbackInfo ci) {
        nametag_impl.write(nbt);
    }

    @Inject(method="readNbt", at = @At("HEAD"))
    public void nametag_read(NbtCompound nbt, CallbackInfo ci) {
        nametag_impl.read(nbt);
    }

    @WrapMethod(method = "canDespawn")
    public boolean nametag_preventDespawn(Operation<Boolean> original) {
        return nametag_getNametagData().canDespawn() && original.call();
    }

    @Inject(method = "onKilledBy", at = @At(value = "INVOKE",target = "Lnet/minecraft/entity/LivingEntity;dropItems()V"))
    public void nametag_DroppedNameTag(CallbackInfo ci) {
        nametag_impl.dropNameTag();
    }

    @Environment(EnvType.SERVER)
    @Inject(method="baseTick", at = @At("TAIL"))
    public void trackNameTagServer(CallbackInfo ci) {
        nametag_impl.tick();
    }

    @Inject(method="<init>", at = @At("TAIL"))
    public void nametag_init(CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        nametag_impl = new NameTagEntityData(self);
        nametag_impl.setCanPut(!(self instanceof  PlayerEntity));
    }
}
