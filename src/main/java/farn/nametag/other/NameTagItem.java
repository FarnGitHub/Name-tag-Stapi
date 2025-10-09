package farn.nametag.other;

import farn.nametag.NameTagMain;
import farn.nametag.other.listener.NameTagConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.HitResultType;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import net.modificationstation.stationapi.api.item.StationItemNbt;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class NameTagItem extends TemplateItem implements StationItemNbt, CustomTooltipProvider {

    public NameTagItem(Identifier identifier) {
        super(identifier);
    }

    public void useOnEntity(ItemStack stack, LivingEntity entity) {
        if(entity.world.isRemote || entity instanceof PlayerEntity) return;
        useNameTag(entity, stack);
    }

    @Environment(EnvType.CLIENT)
    public ItemStack use(ItemStack stack, World world, PlayerEntity user) {
        Minecraft mc = NameTagMain.getMinecraftInstance();
        if(isNotPointingAtEntity(mc)) {
            mc.setScreen(new NameTagChangerScreen(stack));
        }
        return stack;
    }

    @Override
    public NbtCompound getStationNbt() {
        NbtCompound nbt = new NbtCompound();
        if(nbt.contains("nameTag")) {
            nbt.putString(NameTagMain.NAMETAG_ITEM_NBT_KEY, nbt.getString("nameTag"));
        } else {
            nbt.putString(NameTagMain.NAMETAG_ITEM_NBT_KEY, "missingno");
        }
        return nbt;
    }

    @Override
    public @NotNull String[] getTooltip(ItemStack stack, String originalTooltip) {
        String nameTagName = stack.getStationNbt().getString(NameTagMain.NAMETAG_ITEM_NBT_KEY);
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

    private static void useNameTag(LivingEntity ent, ItemStack stack) {
        EntityNameTag entityTag = ((EntityNameTag)ent);
        if(entityTag.nametag_entityHasNameTag()) {
            entityTag.nametag_AddTaggedNamedCount();
        }
        entityTag.nametag_setEntityNameTag(stack.getStationNbt().getString(NameTagMain.NAMETAG_ITEM_NBT_KEY));
        if(NameTagConfig.instance.consumeNameTag) {
            --stack.count;
        }
    }
}
