package farn.nametag.world;

import farn.nametag.impl.NameTagMain;
import farn.nametag.packet.NameTagRenamePacket;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import org.lwjgl.input.Keyboard;

public class NameTagRenamerScreen extends Screen {
    private TextFieldWidget nameTagTextbox;
    private final ItemStack item;
    private final String originalString;

    public NameTagRenamerScreen(ItemStack itemRaw) {
        item = itemRaw;
        originalString = item.getStationNbt().getString(NameTagMain.NAMETAG_ITEM_NBT_KEY);
    }

    public void init() {
        TranslationStorage translate = TranslationStorage.getInstance();
        Keyboard.enableRepeatEvents(true);
        this.buttons.clear();
        this.buttons.add(new ButtonWidget(1, this.width / 2 - 100, this.height / 4 + 84, 100, 20, translate.get("screen.farnnametag.apply")));
        this.buttons.add(new ButtonWidget(2, this.width / 2, this.height / 4 + 84, 100, 20, translate.get("gui.cancel")));
        this.nameTagTextbox = new TextFieldWidget(this, this.textRenderer, this.width / 2 - 100, this.height / 16 + 84, 200, 20, originalString);
        ((ButtonWidget)this.buttons.get(0)).active = false;
    }

    protected void buttonClicked(ButtonWidget button) {
        if(button.id == 2) {
            this.minecraft.setScreen(null);
        } else if(button.id == 1) {
            if(!nameTagTextbox.getText().isEmpty()) {
                int slot = minecraft.player.inventory.selectedSlot;
                if(minecraft.isWorldRemote()) {
                    PacketHelper.send(new NameTagRenamePacket(slot, nameTagTextbox.getText()));
                } else {
                    item.getStationNbt().putString(NameTagMain.NAMETAG_ITEM_NBT_KEY, nameTagTextbox.getText());
                }
                this.minecraft.setScreen(null);
            }
        }
    }

    public void render(int i1, int i2, float f3) {
        this.renderBackground();
        this.drawCenteredTextWithShadow(this.textRenderer, TranslationStorage.getInstance().get("screen.farnnametag.title"), this.width / 2, 60, 10526880);
        this.nameTagTextbox.render();
        super.render(i1, i2, f3);
    }

    protected void keyPressed(char character, int keyCode) {
        if(nameTagTextbox.focused) {
            nameTagTextbox.keyPressed(character, keyCode);
            ((ButtonWidget)this.buttons.get(0)).active = !nameTagTextbox.getText().isEmpty() && !originalString.equals(nameTagTextbox.getText());
        }
    }

    protected void mouseClicked(int mouseX, int mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        this.nameTagTextbox.mouseClicked(mouseX, mouseY, button);
    }
}
