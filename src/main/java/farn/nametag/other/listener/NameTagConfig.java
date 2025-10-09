package farn.nametag.other.listener;

import net.glasslauncher.mods.gcapi3.api.ConfigEntry;
import net.glasslauncher.mods.gcapi3.api.ConfigRoot;

public class NameTagConfig {

    @ConfigRoot(value = "nametag_config", visibleName = "NameTag Config")
    public static final InstanceConfig instance = new InstanceConfig();

    public static class InstanceConfig {

        @ConfigEntry(name = "Consume Nametag", description = "Consume Nametag upon using on entity", multiplayerSynced = true)
        public Boolean consumeNameTag = false;

    }
}
