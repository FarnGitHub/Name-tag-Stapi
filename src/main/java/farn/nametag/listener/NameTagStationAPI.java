package farn.nametag.listener;

import farn.nametag.world.NameTagItem;
import farn.nametag.impl.NameTagMain;
import farn.nametag.packet.EntityNameTagUpdatePacket;
import farn.nametag.packet.NameTagRenamePacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.client.event.gui.screen.container.TooltipBuildEvent;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
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

public class NameTagStationAPI {
    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    @Entrypoint.Logger
    public static Logger LOGGER = Null.get();

    @EventListener
    public void registerItems(ItemRegistryEvent event) {
        NameTagMain.nametag_item = new NameTagItem(NAMESPACE.id("farn_nametag")).setTranslationKey(NAMESPACE, "nametag");
        LOGGER.info(NameTagMain.nametag_item.getTranslationKey());
    }
    @EventListener
    public void registerPackets(PacketRegisterEvent event) {
        Registry.register(PacketTypeRegistry.INSTANCE, NAMESPACE.id("update_name_tag"), NameTagRenamePacket.TYPE);
        Registry.register(PacketTypeRegistry.INSTANCE, NAMESPACE.id("entity_tag"), EntityNameTagUpdatePacket.TYPE);
    }

    @EventListener
    public void registerRecipes(RecipeRegisterEvent event) {
        RecipeRegisterEvent.Vanilla type = RecipeRegisterEvent.Vanilla.fromType(event.recipeId);

        if (type == RecipeRegisterEvent.Vanilla.CRAFTING_SHAPED) {
            CraftingRegistry.addShapedRecipe(new ItemStack(NameTagMain.nametag_item), "w", "o", "o", 'w', new ItemStack(Item.IRON_INGOT), 'o', new ItemStack(Item.PAPER));
        }
    }

    @Environment(EnvType.CLIENT)
    @EventListener
    public void registerTextures(TextureRegisterEvent event) {
        NameTagMain.nametag_item.setTexture(NAMESPACE.id("item/name_tag"));
    }

    @Environment(EnvType.CLIENT)
    @EventListener(priority = ListenerPriority.HIGHEST)
    public void customNameTooltip(TooltipBuildEvent event) {
        if(!event.tooltip.isEmpty()) {
            if(NameTagMain.itemHasCustomName(event.itemStack)) {
                event.tooltip.set(0, event.itemStack.getStationNbt().getString(NameTagMain.CUSTOM_NAME_NBT_KEY));
            }
        }
    }

}
