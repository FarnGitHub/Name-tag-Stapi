package farn.nametag.mixin;

import farn.nametag.other.EntityNameTag;
import net.minecraft.entity.Entity;
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

    @Shadow
    public boolean newPlayerDataUpdated;

    @Inject(method="notifyNewLocation", at = @At("TAIL"))
    public void onSendAround(List par1, CallbackInfo ci) {
        if(this.newPlayerDataUpdated && currentTrackedEntity instanceof EntityNameTag tagged && tagged.nametag_entityHasNameTag()) {
            tagged.nametag_updateClientNameTag();
        }
    }
}
