package farn.nametag.mixin.common;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {

    @Shadow
    public World world;

    @Shadow
    public ItemEntity dropItem(ItemStack itemStack, float yOffset) {
        throw new AssertionError();
    }

    @Inject(method="markDead", at = @At("TAIL"))
    public void nametag_markDead(CallbackInfo ci) {

    }
}
