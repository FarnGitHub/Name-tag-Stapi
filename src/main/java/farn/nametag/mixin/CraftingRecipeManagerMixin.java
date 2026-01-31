package farn.nametag.mixin;

import farn.nametag.other.impl.Util;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = CraftingRecipeManager.class, priority = 900)
public class CraftingRecipeManagerMixin {
    @Inject(method = "craft", at = @At("HEAD"), cancellable = true)
    private void namettag_addItemNameChangerRecipe(CraftingInventory inventory, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack stack = Util.findItemAndPutNameTag(inventory);
        if(stack != null) {
            cir.setReturnValue(stack);
        }
    }
}
