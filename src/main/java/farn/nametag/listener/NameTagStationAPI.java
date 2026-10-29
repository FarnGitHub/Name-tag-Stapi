package farn.nametag.listener;

import farn.nametag.impl.ServerHandler;
import farn.nametag.world.NametagItem;
import farn.nametag.NameTagMain;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.client.event.gui.screen.container.TooltipBuildEvent;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.event.registry.MessageListenerRegistryEvent;
import net.modificationstation.stationapi.api.recipe.CraftingRegistry;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("unused")
public class NameTagStationAPI {
    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    @Entrypoint.Logger
    public static Logger LOGGER = Null.get();

    @EventListener
    public void registerItems(ItemRegistryEvent event) {
        NameTagMain.nametag_item = new NametagItem(NAMESPACE.id("farn_nametag")).setTranslationKey(NAMESPACE, "nametag");
    }

    @EventListener
    public void registerRecipes(RecipeRegisterEvent event) {
        RecipeRegisterEvent.Vanilla type = RecipeRegisterEvent.Vanilla.fromType(event.recipeId);

        if (type == RecipeRegisterEvent.Vanilla.CRAFTING_SHAPED)
            CraftingRegistry.addShapedRecipe(new ItemStack(NameTagMain.nametag_item), "w", "o", "o", 'w', new ItemStack(Item.IRON_INGOT), 'o', new ItemStack(Item.PAPER));
    }

    @Environment(EnvType.CLIENT)
    @EventListener
    public void registerTextures(TextureRegisterEvent event) {
        NameTagMain.nametag_item.setTexture(NAMESPACE.id("item/name_tag"));
    }

    @Environment(EnvType.CLIENT)
    @EventListener(priority = ListenerPriority.HIGHEST)
    public void customNameTooltip(TooltipBuildEvent event) {
        if(!event.tooltip.isEmpty())
            if(NameTagMain.itemHasCustomName(event.itemStack))
                event.tooltip.set(0, event.itemStack.getStationNbt().getString(NameTagMain.CUSTOM_NAME_NBT_KEY));
    }

    @EventListener
    public void register_nameTagPacket(MessageListenerRegistryEvent event) {
        event.register(NAMESPACE.id("rename_nametag"), ((player, messagePacket) -> {
            try {
                int slot = messagePacket.ints[0];
                String tag = messagePacket.strings[0];
                ItemStack stack = player.inventory.getStack(slot);
                if (stack != null && stack.getItem() instanceof NametagItem)
                    stack.getStationNbt().putString(NameTagMain.NAMETAG_ITEM_NBT_KEY, tag);
            } catch (Exception e) {
                if(FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER)
                    ServerHandler.kickPlayer(player, e.getMessage());
            }
        }));
    }

}
