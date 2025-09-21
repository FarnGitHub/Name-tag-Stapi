package farn.nametag.other.listener;

import farn.nametag.NameTagMain;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.modificationstation.stationapi.api.client.event.gui.screen.container.TooltipBuildEvent;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;
import org.apache.logging.log4j.Logger;

public class NameTagClient {
    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    @Entrypoint.Logger
    public static Logger LOGGER = Null.get();

    @EventListener
    public void registerTextures(TextureRegisterEvent event) {
        NameTagMain.farn_Nametag.setTexture(NAMESPACE.id("item/name_tag"));
    }

    @EventListener(priority = ListenerPriority.HIGHEST)
    public void testTooltip(TooltipBuildEvent event) {
        if(event.tooltip.isEmpty()) {
            return;
        } else {
            if(NameTagMain.itemHasNameTag(event.itemStack)) {
                event.tooltip.set(0, event.itemStack.getStationNbt().getString(NameTagMain.CUSTOM_NAME_NBT_KEY));
                event.add("§7§o" + event.itemStack.getItem().getTranslatedName() + "§r");
            }
        }
    }
}
