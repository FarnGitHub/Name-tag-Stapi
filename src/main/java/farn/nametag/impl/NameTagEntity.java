package farn.nametag.impl;

import farn.nametag.world.NameTagEntityData;
import net.modificationstation.stationapi.api.util.Util;

public interface NameTagEntity {

    default NameTagEntityData nametag_getNametagData() {
        return Util.assertImpl();
    }
}
