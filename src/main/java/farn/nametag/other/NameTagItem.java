package farn.nametag.other;

import farn.nametag.other.impl.Util;
import farn.nametag.other.listener.NameTagConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.HitResultType;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import net.modificationstation.stationapi.api.item.UseOnEntityFirst;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class NameTagItem extends TemplateItem implements CustomTooltipProvider, UseOnEntityFirst {

    public NameTagItem(Identifier identifier) {
        super(identifier);
    }

    @Environment(EnvType.CLIENT)
    public ItemStack use(ItemStack stack, World world, PlayerEntity user) {
        nametag_ClientUse(stack);
        return stack;
    }

    @Environment(EnvType.CLIENT)
    private void nametag_ClientUse(ItemStack stack) {
        Minecraft mc = Util.getMinecraftInstance();
        if(isNotPointingAtEntity(mc)) {
            mc.setScreen(new NameTagChangerScreen(stack));
        }
    }

    @Override
    public @NotNull String[] getTooltip(ItemStack stack, String originalTooltip) {
        String nameTagName = stack.getStationNbt().getString(Util.NAMETAG_ITEM_NBT_KEY);
        if (nameTagName == null || nameTagName.isEmpty()) {
            return new String[]{originalTooltip};
        } else {
            return new String[]{originalTooltip, "§7§o" + nameTagName + "§r"};
        }
    }

    @Environment(EnvType.CLIENT)
    private boolean isNotPointingAtEntity(Minecraft mc) {
        return mc.crosshairTarget == null || mc.crosshairTarget.type != HitResultType.ENTITY;
    }

    private static void useNameTag(Entity entRaw, ItemStack stack) {
        if(!(entRaw instanceof PlayerEntity) && entRaw instanceof EntityNameTag entityTag) {
            if(entityTag.nametag_entityHasNameTag()) {
                entityTag.nametag_AddTaggedNamedCount();
            }
            entityTag.nametag_setEntityNameTag(stack.getStationNbt().getString(Util.NAMETAG_ITEM_NBT_KEY));
            if(NameTagConfig.instance.consumeNameTag) {
                --stack.count;
            }
        }
    }

    @Override
    public boolean onUseOnEntityFirst(ItemStack stack, PlayerEntity player, World world, Entity entity) {
        if(!entity.world.isRemote) {
            useNameTag(entity, stack);
        }
        return true;
    }
}
