package farn.nametag.mixin;

import farn.nametag.NameTagMain;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.CraftingRecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = CraftingRecipeManager.class, priority = 900)
public class CraftingRecipeManagerMixin {
    @Inject(method = "craft", at = @At("HEAD"), cancellable = true)
    private void addNameTagItemRecipe(CraftingInventory inventory,
                                      CallbackInfoReturnable<ItemStack> cir) {

        ItemStack item = null;
        ItemStack nameTag = null;

        // Scan the whole grid
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (stack == null) continue;

            if (stack.getItem() == NameTagMain.farn_Nametag) {           // <-- your Name Tag item
                nameTag = stack;
            } else if (item == null) {
                item = stack;                                  // the item that will get renamed
            } else {
                // more than one non-name-tag item → not our recipe
                return;
            }
        }

        // Must have exactly one target item + one name tag
        if (item != null && nameTag != null) {
            boolean correctYes = false;
            ItemStack result = item.copy();
            result.count = 1;
            if (NameTagMain.NameTagHasName(nameTag)) {
                NbtCompound newNbt = result.getStationNbt();
                newNbt.putString(NameTagMain.CUSTOM_NAME_NBT_KEY, nameTag.getStationNbt().getString(NameTagMain.NAMETAG_ITEM_NBT_KEY));
                correctYes = !nameTag.getStationNbt().getString(NameTagMain.NAMETAG_ITEM_NBT_KEY).isEmpty();
            }

            if(correctYes) {
                cir.setReturnValue(result);
            }
        }
    }
}
