package com.costin.eeon;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.costin.eeon.graphic.ScreenManager;
import com.costin.eeon.graphic.scenes.SplashScreen;

public class  Main extends Game {
    public static FitViewport viewport;
    public static FitViewport hudViewport;

    @Override
    public void create() {
        hudViewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        ScreenManager.setScreen(new SplashScreen());
    }
}
