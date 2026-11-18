package farn.nametag;

import farn.nametag.listener.NameTagGlassConfig;
import farn.nametag.listener.NameTagStationAPI;
import farn.nametag.listener.NameTagUniTweak;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;

public class NameTagMain {
    public static Item nametag_item;

    public static final String CUSTOM_NAME_NBT_KEY = "farn_nametag_customname";
    public static final String NAMETAG_ITEM_NBT_KEY = "farn_nametag_nametagname";

    public static boolean uniTweak = false;

    public static boolean nameTagHasName(ItemStack item) {
        return item.getStationNbt().contains(NAMETAG_ITEM_NBT_KEY);
    }

    public static boolean itemHasCustomName(ItemStack item) {
        return item.getStationNbt().contains(CUSTOM_NAME_NBT_KEY);
    }

    public static void addCustomNameToItem(ItemStack item, String customName) {
        NbtCompound newNbt = item.getStationNbt();
        newNbt.putString(CUSTOM_NAME_NBT_KEY, customName);
    }

    public static ItemStack attemptToCombineNametagWithItem(CraftingInventory inventory) {
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
                addCustomNameToItem(result, nameTag.getStationNbt().getString(NAMETAG_ITEM_NBT_KEY));
                notEmpty = !nameTag.getStationNbt().getString(NAMETAG_ITEM_NBT_KEY).isEmpty();
            }

            if(notEmpty) {
                return result;
            }
        }

        return null;
    }

    public static boolean IsUniTweakHideF3EntityID() {
        if(uniTweak)
            return NameTagUniTweak.disableF3EntityId();
        return false;
    }

    public static Identifier id(String name) {
        return NameTagStationAPI.NAMESPACE.id(name);
    }

    public static int countMobWithNametag(World world, Class<?> type) {
        if(!NameTagGlassConfig.instance.fixSpawnRate) return 0;

        int count = 0;
        //noinspection unchecked
        for(Entity ent : (Iterable<? extends Entity>) world.entities)
            if(type.isInstance(ent) && hasNametag(ent))
                ++count;
        return count;
    }

    public static boolean hasNametag(Entity entity) {
        return entity instanceof LivingEntity liv && liv.nametag_hasName();
    }
}
