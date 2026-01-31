package farn.nametag.world;

import farn.nametag.impl.NameTagMain;
import farn.nametag.listener.NameTagGlassConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResultType;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import net.modificationstation.stationapi.api.item.UseOnEntityFirst;
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
        Minecraft mc = NameTagMain.getMinecraft();
        if(isNotPointingAtEntity(mc)) {
            mc.setScreen(new NameTagRenamerScreen(stack));
        }
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

    private static void useNameTag(PlayerEntity plr, Entity entRaw, ItemStack stack) {
        String nameTagName = stack.getStationNbt().getString(NameTagMain.NAMETAG_ITEM_NBT_KEY);
        if(!nameTagName.isEmpty() && !(entRaw instanceof PlayerEntity) && entRaw instanceof LivingEntity livEnt) {
            if(livEnt.nametag_getNametagData().hasName()) {
                livEnt.nametag_getNametagData().markOverridden();
            }
            livEnt.nametag_getNametagData().setName(stack.getStationNbt().getString(NameTagMain.NAMETAG_ITEM_NBT_KEY));
            if(NameTagGlassConfig.instance.consumeNameTag) {
                if(--stack.count <= 0) {
                    plr.inventory.removeStack(plr.inventory.selectedSlot, 0);
                }
            }
        }
    }

    @Override
    public boolean onUseOnEntityFirst(ItemStack stack, PlayerEntity player, World world, Entity entity) {
        if(!entity.world.isRemote) {
            useNameTag(player, entity, stack);
        }
        return true;
    }
}
