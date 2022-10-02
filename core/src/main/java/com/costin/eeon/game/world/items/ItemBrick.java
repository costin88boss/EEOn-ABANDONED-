package com.costin.eeon.game.world.items;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ItemBrick {

    public int id;
    public String payvaultid;
    public int layer;
    public TextureRegion region;
    public int tab;
    public boolean requiresOwnership;
    public boolean requiresAdmin;
    // Requires that the item is bought. Not useable by Gold Members.
    public boolean requiresPurchase;
    public boolean hasShadow;
    public String description;
    public String[] tags;
    public int selectorBG;
    public int minimapColor;

    public ItemBrick(int id, int layer, TextureRegion region, String payvaultid, String description, int tab, boolean requiresOwnership, boolean requiresAdmin, boolean requiresPurchase, boolean shadow, int minimapColor, String[] tags, int selectorBG) {
        this.id = id;
        this.payvaultid = payvaultid;
        this.layer = layer;
        this.region = region;
        this.tab = tab;
        this.requiresOwnership = requiresOwnership;
        this.requiresAdmin = requiresAdmin;
        this.requiresPurchase = requiresPurchase;
        this.hasShadow = shadow;
        this.description = description;
        this.tags = tags;
        this.selectorBG = selectorBG;
        this.minimapColor = minimapColor;
    }
}
