package com.costin.eeon.graphic.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Font {
    public static BitmapFont Nokia22;
    public static BitmapFont TEST;

    public static void init() {
        FreeTypeFontGenerator nokGen = new FreeTypeFontGenerator(Gdx.files.internal("nokiafc22.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.size = 22;

        Nokia22 = nokGen.generateFont(param);
        TEST = nokGen.generateFont(param);

        nokGen.dispose();
    }
}

