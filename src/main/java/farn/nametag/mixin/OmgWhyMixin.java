package farn.nametag.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import farn.nametag.NameTagMain;
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
public class OmgWhyMixin {

    @Shadow
    public Entity currentTrackedEntity;

    @Shadow
    public int ticks;

    @Shadow
    public int trackingFrequency;

    @Inject(method="notifyNewLocation", at = @At("TAIL"))
    public void onSendAround(List par1, CallbackInfo ci) {
        if(this.ticks % this.trackingFrequency == 0 && currentTrackedEntity instanceof EntityNameTag tagged && tagged.nametag_entityHasNameTag()) {
            tagged.nametag_updateClientNameTag();
        }
    }
}
