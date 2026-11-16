package farn.nametag.impl;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface NameTagEntity {


    default boolean nametag_hasName() {
        throw new AssertionError();
    }

    default String nametag_getName() {
        throw new AssertionError();
    }

    default void nametag_setName(String string) {
        throw new AssertionError();
    }

    default void nametag_putNameTag(PlayerEntity plr, ItemStack stack) {
        throw new AssertionError();
    }

    default void nametag_dropNameTag() {
        throw new AssertionError();
    }

    default ItemStack nametag_createNameTagItem() {
        throw new AssertionError();
    }

    default boolean nametag_canPutNameTag() {
        return !(this instanceof PlayerEntity);
    }
}
