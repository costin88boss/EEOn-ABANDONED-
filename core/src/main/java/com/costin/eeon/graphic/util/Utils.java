package com.costin.eeon.graphic.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Utils {
    public static final TextureRegionDrawable dot = new TextureRegionDrawable(new Texture(Gdx.files.internal("dot.png")));
    public static final TextureRegionDrawable colorWheel = new TextureRegionDrawable(new Texture(Gdx.files.internal("color_wheel.png")));
    public static final Pixmap colorPixmap = new Pixmap(Gdx.files.internal("color_wheel.png"));
}
