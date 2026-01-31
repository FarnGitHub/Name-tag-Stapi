package farn.nametag.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {
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
    private void nametag_renderNameTag(LivingEntity entity, double dx, double dy, double dz, CallbackInfo ci) {
        if (entity.nametag_getNametagData().hasName() && !Minecraft.isDebugProfilerEnabled()) {
            this.renderNameTag(entity, entity.nametag_getNametagData().getName(), dx, dy, dz, 64);
        }
    }
}
