package farn.nametag.listener;

import farn.nametag.impl.NameTagMain;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.item.ItemStack;
import paulevs.bhcreative.listeners.VanillaTabListener;
import paulevs.bhcreative.registry.TabRegistryEvent;

public class NameTagBHCreative {

    @EventListener(priority = ListenerPriority.LOW)
    public static void registerItem(TabRegistryEvent event) {
        VanillaTabListener.tabItems.addItem(new ItemStack(NameTagMain.nametag_item));
    }
}
