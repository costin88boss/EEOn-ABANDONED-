package com.costin.eeon.game.smileys;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class  Smiley {
    protected TextureRegion texture;
    protected TextureRegion goldenTexture;
    protected String name;
    protected String description;
    protected int minimapColor;
    // totally unused
    protected String vaultID;

    public TextureRegion getTexture() {
        return texture;
    }

    public TextureRegion getGoldenTexture() {
        return goldenTexture;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getMinimapColor() {
        return minimapColor;
    }

    public String getVaultID() {
        return vaultID;
    }
}

