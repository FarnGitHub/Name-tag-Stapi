package farn.nametag.other;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.entity.EntityTracker;

public interface EntityNameTag {

    public abstract boolean farn_hasEntityName();

    public abstract String farn_getEntityName();

    public abstract void farn_setEntityName(String string);

    public abstract void updateServerNameTag();
}
