package com.costin.eeon.graphic.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;

public class  ImageWithID extends Image {
    public final int id;

    public ImageWithID(int id) {
        super();
        this.id = id;
    }

    public ImageWithID(int id, NinePatch patch) {
        super(patch);
        this.id = id;
    }

    public ImageWithID(int id, TextureRegion region) {
        super(region);
        this.id = id;
    }

    public ImageWithID(int id, Texture texture) {
        super(texture);
        this.id = id;
    }

    public ImageWithID(int id, Skin skin, String drawableName) {
        super(skin, drawableName);
        this.id = id;
    }

    public ImageWithID(int id, Drawable drawable) {
        super(drawable);
        this.id = id;
    }

    public ImageWithID(int id, Drawable drawable, Scaling scaling) {
        super(drawable, scaling);
        this.id = id;
    }

    public ImageWithID(int id, Drawable drawable, Scaling scaling, int align) {
        super(drawable, scaling, align);
        this.id = id;
    }
}

