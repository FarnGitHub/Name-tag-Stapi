package farn.nametag.other;

import farn.nametag.packet.ChangeNameTagServerPacket;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.impl.item.StationNBTSetter;
import org.lwjgl.input.Keyboard;

public class NameTagChangerScreen extends Screen {
    private TextFieldWidget nameTagTextbox;
    private ItemStack item;
    private String originalString = "missingno";

    public NameTagChangerScreen(ItemStack itemRaw) {
        if(!(itemRaw.getItem() instanceof NameTagItem)) {
            this.minecraft.setScreen(null);
        } else {
            item = itemRaw;
            originalString = item.getStationNbt().getString("nameTag");
        }
    }

    public void init() {
        TranslationStorage var1 = TranslationStorage.getInstance();
        Keyboard.enableRepeatEvents(true);
        this.buttons.clear();
        this.buttons.add(new ButtonWidget(1, this.width / 2 - 100, this.height / 4 + 84, 100, 20, "Save Tag"));
        this.buttons.add(new ButtonWidget(2, this.width / 2, this.height / 4 + 84, 100, 20, var1.get("gui.cancel")));
        this.nameTagTextbox = new TextFieldWidget(this, this.textRenderer, this.width / 2 - 100, this.height / 16 + 84, 200, 20, originalString);
        ((ButtonWidget)this.buttons.get(0)).active = false;
    }

    protected void buttonClicked(ButtonWidget button) {
        if(button.id == 2) {
            this.minecraft.setScreen(null);
        } else if(button.id == 1) {
            if(nameTagTextbox.getText().length() > 0) {
                if(minecraft.isWorldRemote()) {
                    int slot = minecraft.player.inventory.selectedSlot;
                    PacketHelper.send(new ChangeNameTagServerPacket(slot, nameTagTextbox.getText()));
                } else {
                    NbtCompound nbt = new NbtCompound();
                    nbt.putString("nameTag", nameTagTextbox.getText());
                    StationNBTSetter.cast(item).setStationNbt(nbt);
                }
                this.minecraft.setScreen(null);
            }
        }
    }

    public void render(int i1, int i2, float f3) {
        this.renderBackground();
        this.drawCenteredTextWithShadow(this.textRenderer, "Enter Name Tag", this.width / 2, 60, 10526880);
        this.nameTagTextbox.render();
        super.render(i1, i2, f3);
    }

    protected void keyPressed(char character, int keyCode) {
        if(nameTagTextbox.focused) {
            nameTagTextbox.keyPressed(character, keyCode);
            ((ButtonWidget)this.buttons.get(0)).active = nameTagTextbox.getText().length() > 0 && !originalString.equals(nameTagTextbox.getText());
        }
    }

    protected void mouseClicked(int mouseX, int mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        this.nameTagTextbox.mouseClicked(mouseX, mouseY, button);
    }
}
