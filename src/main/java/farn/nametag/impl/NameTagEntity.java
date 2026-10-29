package farn.nametag.impl;

import farn.nametag.world.NametagData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.modificationstation.stationapi.api.util.Util;

public interface NameTagEntity {

    default NametagData nametag_getNametagData() {
        return Util.assertImpl();
    }

    default boolean nametag_canPutNameTag() {
        return !(this instanceof PlayerEntity);
    }
}
