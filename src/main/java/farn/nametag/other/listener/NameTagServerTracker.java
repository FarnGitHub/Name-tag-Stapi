package farn.nametag.other.listener;

import farn.nametag.other.EntityNameTag;
import farn.nametag.packet.UpdateClientNameTagPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.server.event.entity.TrackEntityEvent;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;
import org.apache.logging.log4j.Logger;

@Environment(EnvType.SERVER)
public class NameTagServerTracker {
    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    @Entrypoint.Logger
    public static Logger LOGGER = Null.get();

    @EventListener
    public void trackEntity(TrackEntityEvent event) {
        if(event.entityToTrack instanceof LivingEntity && (!(event.entityToTrack instanceof PlayerEntity))) {
            EntityNameTag entityNameTag = ((EntityNameTag)event.entityToTrack);
            if(entityNameTag.farn_hasEntityName()) {
                PacketHelper.sendToAllTracking(event.entityToTrack, new UpdateClientNameTagPacket(event.entityToTrack.id, entityNameTag.farn_getEntityName()));
            }
        }

    }
}
