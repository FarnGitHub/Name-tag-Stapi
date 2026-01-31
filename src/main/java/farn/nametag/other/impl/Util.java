package farn.nametag.other.impl;

import farn.nametag.NameTagMain;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class Util {
    public static Item farn_Nametag;

    public static final String CUSTOM_NAME_NBT_KEY = "farn_nametag_customname";
    public static final String NAMETAG_ITEM_NBT_KEY = "farn_nametag_nametagname";

    public static int nameTagTrackingId;

    @Environment(EnvType.CLIENT)
    private static Minecraft mc;

    public static boolean nameTagHasName(ItemStack item) {
        return item.getStationNbt().contains(NAMETAG_ITEM_NBT_KEY);
    }

    public static boolean itemHasNameTag(ItemStack item) {
        return item.getStationNbt().contains(CUSTOM_NAME_NBT_KEY);
    }

    @Environment(EnvType.CLIENT)
    public static Minecraft getMinecraftInstance() {
        if(mc == null) {
            mc = ((Minecraft) FabricLoader.getInstance().getGameInstance());
        }

        return mc;
    }

    public static ItemStack findItemAndPutNameTag(CraftingInventory inventory) {
        ItemStack item = null;
        ItemStack nameTag = null;

        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (stack == null) continue;

            if (stack.getItem() == farn_Nametag) {
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
