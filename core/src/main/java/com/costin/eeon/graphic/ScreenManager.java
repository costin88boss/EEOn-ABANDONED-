package com.costin.eeon.graphic;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public class  ScreenManager {
    public static void setScreen(Screen screen) {
        ((Game) Gdx.app.getApplicationListener()).setScreen(screen);
    }
}
