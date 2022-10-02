package com.costin.eeon.graphic.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.costin.eeon.Main;
import com.costin.eeon.game.Laws;
import com.costin.eeon.game.players.CollFilter;
import com.costin.eeon.game.players.LocalPlayer;
import com.costin.eeon.game.players.PlayerManager;
import com.costin.eeon.game.smileys.SmileyManager;
import com.costin.eeon.game.sounds.SoundManager;
import com.costin.eeon.game.world.WorldManager;
import com.costin.eeon.game.world.items.BlockManager;
import com.costin.eeon.graphic.ScreenManager;
import com.costin.eeon.graphic.util.Font;
import com.costin.eeon.net.GameClient;

public class  SplashScreen implements Screen {

    private static SplashScreen singleton;
    private final Sprite splash;
    private final SpriteBatch batch;
    private float timer = 4f;

    public SplashScreen() {
        splash = new Sprite(new Texture(Gdx.files.internal("titlescreen.png")));
        batch = new SpriteBatch();
        singleton = this;

        // loading stuff
        if (WorldManager.getInstance() == null) {
            Font.init();
            GameClient.init();
            new SoundManager();
            new SmileyManager(); // WARNING: these classes must-
            new MainMenu();      //-be constructed in this order.
            new WorldScreen();
            new WorldManager();
            new BlockManager();
            new PlayerManager();
            new CollFilter();
            new LocalPlayer();
        }

    }

    public static SplashScreen getInstance() {
        return singleton;
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        Main.viewport.apply(true);
        batch.setProjectionMatrix(Main.viewport.getCamera().combined);

        timer -= Gdx.graphics.getDeltaTime();
        batch.begin();
        if (timer >= 3f) splash.setColor(1, 1, 1, -timer + 4);
        else splash.setColor(1, 1, 1, timer);
        splash.draw(batch);
        batch.end();
        if (timer <= -1f && !Laws.skipSplash) {
            ScreenManager.setScreen(MainMenu.getInstance());
            timer = 4f;
        } else ScreenManager.setScreen(MainMenu.getInstance());
    }

    @Override
    public void resize(int width, int height) {
        Main.viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
