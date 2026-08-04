package farn.nametag.impl;

import farn.nametag.world.NametagData;
import net.modificationstation.stationapi.api.util.Util;

public interface NameTagEntity {

    default NametagData nametag_getNametagData() {
        return Util.assertImpl();
    }
}
