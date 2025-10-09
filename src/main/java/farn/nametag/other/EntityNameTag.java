package farn.nametag.other;

public interface EntityNameTag {

    boolean nametag_entityHasNameTag();

    String nametag_getEntityNameTag();

    void nametag_setEntityNameTag(String string);

    void nametag_updateClientNameTag();

    void nametag_AddTaggedNamedCount();
}
