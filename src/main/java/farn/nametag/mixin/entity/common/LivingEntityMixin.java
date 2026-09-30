package farn.nametag.mixin.entity.common;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import farn.nametag.world.NametagData;
import farn.nametag.impl.NameTagEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements NameTagEntity {

    @Shadow
    protected int despawnCounter;
    @Unique
    private NametagData nametag_impl;

    @Override
    public NametagData nametag_getNametagData() {
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

    @WrapMethod(method="tryDespawn")
    public void nametag_preventDespawn(Operation<Void> original) {
        if(nametag_getNametagData().canDespawn()) {
            original.call();
        } else {
            despawnCounter = 0;
        }
    }

    @Inject(method = "onKilledBy", at = @At(value = "INVOKE",target = "Lnet/minecraft/entity/LivingEntity;dropItems()V"))
    public void nametag_DroppedNameTag(CallbackInfo ci) {
        nametag_impl.dropNameTag();
    }

    @Inject(method="<init>", at = @At("TAIL"))
    public void nametag_init(CallbackInfo ci) {
        nametag_impl = new NametagData((LivingEntity)(Object)this);
    }
}
