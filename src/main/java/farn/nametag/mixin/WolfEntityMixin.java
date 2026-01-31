package farn.nametag.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WolfEntity.class)
public abstract class WolfEntityMixin extends AnimalEntity {

    public WolfEntityMixin(World world) {
        super(world);
    }

    @WrapMethod(method = "canDespawn")
    public boolean nametag_preventDespawn(Operation<Boolean> original) {
        return nametag_getNametagData().canDespawn() && original.call();
    }
}
