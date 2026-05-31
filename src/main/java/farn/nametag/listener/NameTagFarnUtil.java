package farn.nametag.listener;

import farn.farn_util.api.dungeon.DungeonAPI;
import farn.farn_util.api.dungeon.DungeonLoot;
import farn.nametag.NameTagMain;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.event.init.InitFinishedEvent;

public class NameTagFarnUtil {

    @EventListener
    public void initFinished(InitFinishedEvent event) {
        if(NameTagGlassConfig.instance.addToDungeonLoot) {
            DungeonLoot loot = new DungeonLoot(new ItemStack(NameTagMain.nametag_item));
            loot.weight = 50;
            DungeonAPI.addLoot(loot);
        }
    }
}
