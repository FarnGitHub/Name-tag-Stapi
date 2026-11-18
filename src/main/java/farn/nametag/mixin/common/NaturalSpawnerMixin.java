package farn.nametag.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import farn.nametag.NameTagMain;
import net.minecraft.world.NaturalSpawner;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(NaturalSpawner.class)
public class NaturalSpawnerMixin {

    @WrapOperation(method="tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;countEntities(Ljava/lang/Class;)I"))
    private static int nametag_fixSpawnrate(World instance, Class<?> aClass, Operation<Integer> original) {
        return original.call(instance, aClass) - NameTagMain.countMobWithNametag(instance, aClass);
    }
}
