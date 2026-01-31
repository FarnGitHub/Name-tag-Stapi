package farn.nametag.impl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class NameTagMain {
    public static Item nametag_item;

    public static final String CUSTOM_NAME_NBT_KEY = "farn_nametag_customname";
    public static final String NAMETAG_ITEM_NBT_KEY = "farn_nametag_nametagname";

    @Environment(EnvType.CLIENT)
    private static Minecraft mc;

    public static boolean nameTagHasName(ItemStack item) {
        return item.getStationNbt().contains(NAMETAG_ITEM_NBT_KEY);
    }

    public static boolean itemHasCustomName(ItemStack item) {
        return item.getStationNbt().contains(CUSTOM_NAME_NBT_KEY);
    }

    @Environment(EnvType.CLIENT)
    public static Minecraft getMinecraft() {
        if(mc == null) {
            mc = ((Minecraft) FabricLoader.getInstance().getGameInstance());
        }

        return mc;
    }

    public static ItemStack putCustomNameToItem(CraftingInventory inventory) {
        ItemStack item = null;
        ItemStack nameTag = null;

        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (stack == null) continue;

            if (stack.getItem() == nametag_item) {
                nameTag = stack;
            } else if (item == null) {
                item = stack;
            } else {
                return null;
            }
        }
        if (item != null && nameTag != null) {
            boolean notEmpty = false;
            ItemStack result = item.copy();
            result.count = 1;
            if (nameTagHasName(nameTag)) {
                NbtCompound newNbt = result.getStationNbt();
                newNbt.putString(CUSTOM_NAME_NBT_KEY, nameTag.getStationNbt().getString(NAMETAG_ITEM_NBT_KEY));
                notEmpty = !nameTag.getStationNbt().getString(NAMETAG_ITEM_NBT_KEY).isEmpty();
            }

            if(notEmpty) {
                return result;
            }
        }

        return null;
    }
}
