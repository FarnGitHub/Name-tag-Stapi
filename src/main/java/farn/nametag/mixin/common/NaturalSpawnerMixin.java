package farn.nametag.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import farn.nametag.NameTagMain;
import farn.nametag.listener.NameTagGlassConfig;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(World.class)
public class NaturalSpawnerMixin {

    @WrapOperation(method="countEntities", at = @At(value = "INVOKE", target = "Ljava/lang/Class;isAssignableFrom(Ljava/lang/Class;)Z"))
    private boolean nametag_dontCountNameTagged(Class<?> type, @NotNull Class<?> target, Operation<Boolean> original, @Local(type= Entity.class) Entity entity) {
        return original.call(type, target) && !(NameTagGlassConfig.instance.fixSpawnRate && NameTagMain.hasNametag(entity));
    }
}
