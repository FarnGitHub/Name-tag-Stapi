package farn.nametag.mixin.entity.server;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.entity.EntityTrackerEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(EntityTrackerEntry.class)
public class EntityTrackerEntryMixin {

    @Shadow
    public Entity currentTrackedEntity;

    @Inject(method="notifyNewLocation", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/data/DataTracker;isDirty()Z"))
    public void markNametagDirty(List par1, CallbackInfo ci) {
        if(this.currentTrackedEntity instanceof LivingEntity living && living.nametag_getNametagData().dirty) {
            living.nametag_getNametagData().updateClient();
        }
    }
}
