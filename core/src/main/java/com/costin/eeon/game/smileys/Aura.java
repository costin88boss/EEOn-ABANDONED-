package com.costin.eeon.game.smileys;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Aura {
    protected Animation<TextureRegion> animation;
    protected Animation<TextureRegion> goldenAnimation;
    protected TextureRegion texture;
    protected TextureRegion goldenTexture;
    protected String name;
    protected int minimapColor;
    // totally unused
    protected String vaultID;

    public boolean isAnimated() {
        return animation != null;
    }

    public Animation<TextureRegion> getAnimation() {
        return animation;
    }

    public Animation<TextureRegion> getGoldenAnimation() {
        return goldenAnimation;
    }

    public TextureRegion getTexture() {
        return texture;
    }

    public TextureRegion getGoldenTexture() {
        return goldenTexture;
    }

    public String getName() {
        return name;
    }

    public int getMinimapColor() {
        return minimapColor;
    }

    public String getVaultID() {
        return vaultID;
    }
}
