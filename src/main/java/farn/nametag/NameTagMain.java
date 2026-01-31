package farn.nametag;

import farn.nametag.other.EntityNameTag;
import farn.nametag.other.NameTagItem;
import farn.nametag.other.impl.Util;
import farn.nametag.packet.EntityNameTagUpdatePacket;
import farn.nametag.packet.RenameNameTagPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.entity.player.PlayerEntity;
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
import net.modificationstation.stationapi.api.server.event.entity.TrackEntityEvent;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import org.apache.logging.log4j.Logger;

public class NameTagMain {
    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    @Entrypoint.Logger
    public static Logger LOGGER = Null.get();

    @EventListener
    public void registerItems(ItemRegistryEvent event) {
        Util.farn_Nametag = new NameTagItem(NAMESPACE.id("farn_nametag")).setTranslationKey(NAMESPACE, "nametag");
        LOGGER.info(Util.farn_Nametag.getTranslationKey());
    }
    @EventListener
    public void registerPackets(PacketRegisterEvent event) {
        Registry.register(PacketTypeRegistry.INSTANCE, NAMESPACE.id("update_name_tag"), RenameNameTagPacket.TYPE);
        Registry.register(PacketTypeRegistry.INSTANCE, NAMESPACE.id("entity_tag"), EntityNameTagUpdatePacket.TYPE);
        Util.nameTagTrackingId = NAMESPACE.id("farn_nametag_name").hashCode();
    }

    @EventListener
    public void registerRecipes(RecipeRegisterEvent event) {
        RecipeRegisterEvent.Vanilla type = RecipeRegisterEvent.Vanilla.fromType(event.recipeId);

        if (type == RecipeRegisterEvent.Vanilla.CRAFTING_SHAPED) {
            CraftingRegistry.addShapedRecipe(new ItemStack(Util.farn_Nametag), "w", "o", "o", 'w', new ItemStack(Item.IRON_INGOT), 'o', new ItemStack(Item.PAPER));
        }
    }

    @Environment(EnvType.CLIENT)
    @EventListener
    public void registerTextures(TextureRegisterEvent event) {
        Util.farn_Nametag.setTexture(NAMESPACE.id("item/name_tag"));
    }

    @Environment(EnvType.CLIENT)
    @EventListener(priority = ListenerPriority.HIGHEST)
    public void customNameTooltip(TooltipBuildEvent event) {
        if(!event.tooltip.isEmpty()) {
            if(Util.itemHasNameTag(event.itemStack)) {
                event.tooltip.set(0, event.itemStack.getStationNbt().getString(Util.CUSTOM_NAME_NBT_KEY));
                event.add("§7§o" + event.itemStack.getItem().getTranslatedName() + "§r");
            }
        }
    }

}
