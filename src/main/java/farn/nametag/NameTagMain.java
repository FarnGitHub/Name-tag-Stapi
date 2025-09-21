package farn.nametag;

import farn.nametag.other.NameTagItem;
import farn.nametag.packet.UpdateClientNameTagPacket;
import farn.nametag.packet.ChangeNameTagServerPacket;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.event.network.packet.PacketRegisterEvent;
import net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.recipe.CraftingRegistry;
import net.modificationstation.stationapi.api.registry.PacketTypeRegistry;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import org.apache.logging.log4j.Logger;

public class NameTagMain {
    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    @Entrypoint.Logger
    public static Logger LOGGER = Null.get();

    public static Item farn_Nametag;

    public static final String CUSTOM_NAME_NBT_KEY = "farn_nametag_customname";
    public static final String NAMETAG_ITEM_NBT_KEY = "farn_nametag_nametagname";

    @EventListener
    public void registerItems(ItemRegistryEvent event) {
        farn_Nametag = new NameTagItem(NAMESPACE.id("farn_nametag")).setTranslationKey(NAMESPACE, "nametag");
        LOGGER.info(farn_Nametag.getTranslationKey());
    }
    @EventListener
    public void registerPackets(PacketRegisterEvent event) {
        Registry.register(PacketTypeRegistry.INSTANCE, NAMESPACE.id("update_name_tag"), ChangeNameTagServerPacket.TYPE);
        Registry.register(PacketTypeRegistry.INSTANCE, NAMESPACE.id("entity_tag"), UpdateClientNameTagPacket.TYPE);
    }

    @EventListener
    public void registerRecipes(RecipeRegisterEvent event) {
        RecipeRegisterEvent.Vanilla type = RecipeRegisterEvent.Vanilla.fromType(event.recipeId);

        if (type == RecipeRegisterEvent.Vanilla.CRAFTING_SHAPED) {
            CraftingRegistry.addShapedRecipe(new ItemStack(farn_Nametag), "w", "o", "o", 'w', new ItemStack(Item.IRON_INGOT), 'o', new ItemStack(Item.PAPER));
        }
    }

    public static boolean NameTagHasName(ItemStack item) {
        return item.getStationNbt().contains(NAMETAG_ITEM_NBT_KEY);
    }

    public static boolean itemHasNameTag(ItemStack item) {
        return item.getStationNbt().contains(CUSTOM_NAME_NBT_KEY);
    }

}
