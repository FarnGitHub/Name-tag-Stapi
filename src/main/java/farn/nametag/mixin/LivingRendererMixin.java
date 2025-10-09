package farn.nametag.mixin;

import farn.nametag.other.EntityNameTag;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingRendererMixin {
    @Shadow
    protected abstract void renderNameTag(
            LivingEntity entity,
            String name,
            double dx, double dy, double dz,
            int range
    );

    @Inject(
            method = "renderNameTag(Lnet/minecraft/entity/LivingEntity;DDD)V",
            at = @At("TAIL")
    )
    private void onRenderNameTag(LivingEntity entity, double dx, double dy, double dz, CallbackInfo ci) {
        EntityNameTag tag = (EntityNameTag) entity;
        if (tag.nametag_entityHasNameTag() && !Minecraft.isDebugProfilerEnabled()) {
            this.renderNameTag(entity, tag.nametag_getEntityNameTag(), dx, dy, dz, 64);
        }
    }
}
