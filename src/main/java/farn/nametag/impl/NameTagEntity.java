package farn.nametag.impl;

import farn.nametag.world.NameTagData;
import net.modificationstation.stationapi.api.util.Util;

public interface NameTagEntity {

    default NameTagData nametag_getNametagData() {
        return Util.assertImpl();
    }
}
