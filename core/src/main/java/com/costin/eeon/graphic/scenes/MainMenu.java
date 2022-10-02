package com.costin.eeon.graphic.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.costin.eeon.Main;
import com.costin.eeon.graphic.ScreenManager;
import com.costin.eeon.graphic.util.UIStyles;
import com.costin.eeon.graphic.util.Utils;
import com.costin.eeon.net.GameClient;

import java.util.Objects;

public class  MainMenu implements Screen {
    private static MainMenu singleton;
    private final Stage menu;

    public MainMenu() {
        singleton = this;
        menu = new Stage(Main.viewport);

        Group mainUI = new Group();
        Group createUI = new Group();
        Group connectUI = new Group();


        float w = Main.viewport.getWorldWidth();
        float h = Main.viewport.getWorldHeight();

        // Main Btns
        Button joinBtn = new TextButton("Join Server", UIStyles.btnStyle);
        joinBtn.setPosition(w / 2.f - joinBtn.getWidth() / 2, h / 2.f + 150);
        Button createBtn = new TextButton("Create Server (WIP)", UIStyles.btnStyle);
        createBtn.setPosition(w / 2.f - createBtn.getWidth() / 2, h / 2.f - 150);

        // Back Btn
        Button backBtn = new TextButton("<- Back", UIStyles.btnStyle);
        backBtn.setPosition(0, 0);
        backBtn.setVisible(false);

        // Button triggers
        joinBtn.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                connectUI.setVisible(true);
                mainUI.setVisible(false);
                backBtn.setVisible(true);
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        createBtn.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                createUI.setVisible(true);
                mainUI.setVisible(false);
                backBtn.setVisible(true);
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        backBtn.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                createUI.setVisible(false);
                connectUI.setVisible(false);
                mainUI.setVisible(true);
                backBtn.setVisible(false);
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        // Create Server UI


        // Join Server UI
        Button cnctBtn = new TextButton("Connect", UIStyles.btnStyle);
        cnctBtn.setPosition(w / 2 - cnctBtn.getWidth() / 2, h / 2 - 150);

        Button cnctBtn2 = new TextButton("Connect to Costin", UIStyles.btnStyle);
        cnctBtn2.setPosition(w / 2 - cnctBtn2.getWidth() / 2, h / 2 - 200);


        TextField serverIP = new TextField("localhost", UIStyles.tFieldStyle);
        serverIP.setWidth(200);
        serverIP.setPosition(w / 2 - serverIP.getWidth() / 2, h / 2);

        Image serverIPBorders = new Image(Utils.dot.tint(Color.DARK_GRAY));
        serverIPBorders.setSize(serverIP.getWidth() + 2, serverIP.getHeight() + 2);
        serverIPBorders.setPosition(w / 2 - serverIP.getWidth() / 2 - 2, h / 2 - 1);

        Label errorText = new Label("", UIStyles.labStyle);
        errorText.setPosition(0, h);
        errorText.setWrap(true);
        errorText.setAlignment(Align.top);
        errorText.setWidth(w);

        cnctBtn.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                String ip = serverIP.getText();
                if (Objects.equals(ip, "")) {
                    errorText.setText("Error: IP is empty!");
                } else {
                    errorText.setText("");
                    GameClient.createConnection(ip, errorText);
                }

                return super.touchDown(event, x, y, pointer, button);
            }
        });
        cnctBtn2.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                GameClient.createConnection("92.81.219.102", errorText);

                return super.touchDown(event, x, y, pointer, button);
            }
        });

        connectUI.addActor(cnctBtn);
        connectUI.addActor(cnctBtn2);
        connectUI.addActor(serverIPBorders);
        connectUI.addActor(serverIP);
        connectUI.addActor(errorText);

        // End of the line

        connectUI.setVisible(false);
        createUI.setVisible(false);

        mainUI.addActor(joinBtn);
        mainUI.addActor(createBtn);
        menu.addActor(backBtn);

        menu.addActor(mainUI);
        menu.addActor(createUI);
        menu.addActor(connectUI);
    }

    public static MainMenu getInstance() {
        return singleton;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(menu);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        menu.act();
        menu.draw();

        if (GameClient.hasJoined) {
            ScreenManager.setScreen(WorldScreen.getInstance());
        }
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
