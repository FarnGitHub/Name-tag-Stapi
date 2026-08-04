package farn.nametag.world;

import farn.nametag.NameTagMain;
import farn.nametag.packet.RenameNameTagPacket;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import org.lwjgl.input.Keyboard;

public class RenamerScreen extends Screen {
    private TextFieldWidget textField;
    private final ItemStack nameTag;
    private String currentName = "";

    public RenamerScreen(PlayerEntity player) {
        nameTag = player.getHand();
        if(nameTag != null)
            currentName = nameTag.getStationNbt().getString(NameTagMain.NAMETAG_ITEM_NBT_KEY);
    }

    @SuppressWarnings("unchecked")
    public void init() {
        TranslationStorage translate = TranslationStorage.getInstance();
        Keyboard.enableRepeatEvents(true);
        this.buttons.clear();
        this.buttons.add(new ButtonWidget(1, this.width / 2 - 100, this.height / 4 + 84, 100, 20, translate.get("screen.farnnametag.apply")));
        this.buttons.add(new ButtonWidget(2, this.width / 2, this.height / 4 + 84, 100, 20, translate.get("gui.cancel")));
        this.textField = new TextFieldWidget(this, this.textRenderer, this.width / 2 - 100, this.height / 16 + 84, 200, 20, currentName);
        ((ButtonWidget)this.buttons.get(0)).active = false;
    }

    protected void buttonClicked(ButtonWidget button) {
        if(button.id == 2)
            this.minecraft.setScreen(null);
        else if(button.id == 1) {
            if(!textField.getText().isEmpty()) {
                int slot = minecraft.player.inventory.selectedSlot;
                if(minecraft.isWorldRemote())
                    PacketHelper.send(new RenameNameTagPacket(slot, textField.getText()));
                else
                    nameTag.getStationNbt().putString(NameTagMain.NAMETAG_ITEM_NBT_KEY, textField.getText());
                this.minecraft.setScreen(null);
            }
        }
    }

    public void render(int i1, int i2, float f3) {
        this.renderBackground();
        this.drawCenteredTextWithShadow(this.textRenderer, TranslationStorage.getInstance().get("screen.farnnametag.title"), this.width / 2, 60, 10526880);
        this.textField.render();
        super.render(i1, i2, f3);
    }

    protected void keyPressed(char character, int keyCode) {
        if(textField.focused) {
            textField.keyPressed(character, keyCode);
            ((ButtonWidget)this.buttons.get(0)).active =
                    isValidText(textField.getText());
        }
    }

    protected void mouseClicked(int mouseX, int mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        this.textField.mouseClicked(mouseX, mouseY, button);
    }

    private boolean isValidText(String str) {
        return !str.isEmpty() && !str.equals(currentName);
    }

    public boolean shouldPause() {
        return false;
    }
}
