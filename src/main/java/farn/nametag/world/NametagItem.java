package farn.nametag.world;

import farn.nametag.NameTagMain;
import farn.nametag.impl.ClientHandler;
import farn.nametag.listener.NameTagGlassConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import net.modificationstation.stationapi.api.item.UseOnEntityFirst;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class NametagItem extends TemplateItem implements CustomTooltipProvider, UseOnEntityFirst {

    public NametagItem(Identifier identifier) {
        super(identifier);
    }

    public ItemStack use(ItemStack stack, World world, PlayerEntity user) {
        if(FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
            ClientHandler.openNameTagScreen();
        return stack;
    }

    @Override
    public @NotNull String[] getTooltip(ItemStack stack, String originalTooltip) {
        String nameTagName = stack.getStationNbt().getString(NameTagMain.NAMETAG_ITEM_NBT_KEY);
        if (nameTagName == null || nameTagName.isEmpty())
            return new String[]{originalTooltip};
        else
            return new String[]{originalTooltip, "§7§o" + nameTagName + "§r"};
    }

    private static void useNameTag(PlayerEntity plr, Entity entRaw, ItemStack stack) {
        String nameTagName = stack.getStationNbt().getString(NameTagMain.NAMETAG_ITEM_NBT_KEY);
        if(!nameTagName.isEmpty() && !(entRaw instanceof PlayerEntity) && entRaw instanceof LivingEntity livEnt) {
            if(livEnt.nametag_getNametagData().hasName())
                livEnt.nametag_getNametagData().markOverridden();
            livEnt.nametag_getNametagData().setName(stack.getStationNbt().getString(NameTagMain.NAMETAG_ITEM_NBT_KEY));
            if(NameTagGlassConfig.instance.consumeNameTag)
                if(--stack.count <= 0)
                    plr.inventory.removeStack(plr.inventory.selectedSlot, 0);
        }
    }

    @Override
    public boolean onUseOnEntityFirst(ItemStack stack, PlayerEntity player, World world, Entity entity) {
        if(!entity.world.isRemote)
            useNameTag(player, entity, stack);
        return true;
    }
}
