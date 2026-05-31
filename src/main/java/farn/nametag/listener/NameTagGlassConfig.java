package farn.nametag.listener;

import net.glasslauncher.mods.gcapi3.api.ConfigEntry;
import net.glasslauncher.mods.gcapi3.api.ConfigRoot;

public class NameTagGlassConfig {

    @ConfigRoot(value = "nametag_config", visibleName = "NameTag Config")
    public static final InstanceConfig instance = new InstanceConfig();

    public static class InstanceConfig {

        @ConfigEntry(name = "Consume Nametag", description = "Consume Nametag upon using on entity", multiplayerSynced = true)
        public Boolean consumeNameTag = false;

        @ConfigEntry(name = "Add to dungeon loot", description = "Add Nametag to dungeon loot (This option require FarnUtil to actually work)", multiplayerSynced = true, requiresRestart = true)
        public Boolean addToDungeonLoot = true;

        @ConfigEntry(name = "Disabled recipe", description = "Disabled crafting recipe for nametag item", multiplayerSynced = true, requiresRestart = true)
        public Boolean disabledRecipe = true;
    }
}
