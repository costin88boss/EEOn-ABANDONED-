package com.costin.eeon.graphic.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.costin.eeon.Main;
import com.costin.eeon.game.players.LocalPlayer;
import com.costin.eeon.game.players.Player;
import com.costin.eeon.game.players.PlayerManager;
import com.costin.eeon.game.smileys.Aura;
import com.costin.eeon.game.smileys.Smiley;
import com.costin.eeon.game.smileys.SmileyManager;
import com.costin.eeon.game.world.BlockGroup;
import com.costin.eeon.game.world.WorldManager;
import com.costin.eeon.game.world.items.BlockManager;
import com.costin.eeon.graphic.ScreenManager;
import com.costin.eeon.graphic.util.CustomShaper;
import com.costin.eeon.graphic.util.ImageWithID;
import com.costin.eeon.graphic.util.UIStyles;
import com.costin.eeon.graphic.util.Utils;
import com.costin.eeon.net.GameClient;
import com.costin.eeon.net.packets.player.updates.clientside.PlayerUpdatePacket;
import com.dongbat.jbump.Rect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.List;

import static com.badlogic.gdx.Gdx.input;

public class  WorldScreen implements Screen {

    private static WorldScreen singleton;
    private final Stage HUD;
    private final SpriteBatch worldBatch;
    private final CustomShaper debugBatch;
    private final Label debugLab, inspectToolLab;
    private boolean devMenu, inspectTool;
    private boolean blocksObstructed;

    public WorldScreen() {

        HUD = new Stage(Main.hudViewport);
        worldBatch = new SpriteBatch();
        debugBatch = new CustomShaper();
        singleton = this;

        debugLab = new Label("", UIStyles.defLabStyle);
        debugLab.setPosition(0, Gdx.graphics.getHeight() - 50);
        debugLab.setFontScale(0.5f);
        inspectToolLab = new Label("", UIStyles.defLabStyle);
        inspectToolLab.setFontScale(0.5f);

        inspectToolLab.setVisible(false);
        debugLab.setVisible(false);

        float gray = 0.07f;
        float barHeight = Gdx.graphics.getHeight() / 13f;

        Image HUDBar = new Image(Utils.dot);
        HUDBar.setSize(Gdx.graphics.getWidth() / 1.3f, barHeight);
        HUDBar.setColor(gray, gray, gray, 1);

        Image HUDChat = new Image(Utils.dot);
        HUDChat.setPosition(Gdx.graphics.getWidth() / 1.3f, 0);
        HUDChat.setSize(Gdx.graphics.getWidth() / 4f, Gdx.graphics.getHeight());
        HUDChat.setColor(0, 0, 0, 1);

        TextButton gotoLobbyButt = new TextButton("Goto\nLobby", UIStyles.defButtStyle);
        gotoLobbyButt.setSize(50, barHeight);
        gotoLobbyButt.getLabel().setFontScale(0.5f);
        gotoLobbyButt.getLabel().setAlignment(Align.center);
        gotoLobbyButt.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                GameClient.hasJoined = false;
                GameClient.client.close();
                GameClient.fallbackText.setText("");
                ScreenManager.setScreen(SplashScreen.getInstance());
                return true;
            }
        });

        Group smileyMenu = new Group();
        Group auraMenu = new Group();

        ImageButton smileyButt = new ImageButton(new TextureRegionDrawable(SmileyManager.getInstance().getSmileyByID(0).getTexture()));
        smileyButt.setPosition(50, 0);
        smileyButt.setSize(33, barHeight);
        smileyButt.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                smileyMenu.setVisible(!smileyMenu.isVisible());
                auraMenu.setVisible(false);
                blocksObstructed = smileyMenu.isVisible();
                return true;
            }
        });

        ImageButton auraButt = new ImageButton(new TextureRegionDrawable(SmileyManager.getInstance().getAuraByID(0).getTexture()));
        auraButt.setPosition(83, 0);
        auraButt.setSize(33, barHeight);
        auraButt.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                auraMenu.setVisible(!auraMenu.isVisible());
                smileyMenu.setVisible(false);
                blocksObstructed = auraMenu.isVisible();
                return true;
            }
        });

        Image smileyHUD = new Image(Utils.dot);
        smileyHUD.setPosition(10, barHeight + 10);
        smileyHUD.setSize(14 * 20 + 10, 14 * 20 + 106);
        smileyHUD.setColor(gray, gray, gray, 1);

        Image auraHUD = new Image(Utils.dot);
        auraHUD.setPosition(10, barHeight + 10);
        auraHUD.setSize(14 * 20 + 10, Gdx.graphics.getHeight() / 1.6f);
        auraHUD.setColor(gray, gray, gray, 1);

        List<ImageWithID> smileyShowcases = new ArrayList<>();

        int j = 0;
        for (int i = 0; i < SmileyManager.getInstance().getObtainableSmileyCount(); i++) {
            Smiley smiley = SmileyManager.getInstance().getSmileyByID(i + j);
            if (smiley.getVaultID().equals("unobtainable")) {
                i--;
                j++;
                continue;
            }
            ImageWithID img = new ImageWithID(i + j, new TextureRegionDrawable(smiley.getTexture()));
            img.setPosition(10 + ((i) % 14 * 20), barHeight + 106 + ((int) ((i) / 14f) * 20));
            img.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    PlayerUpdatePacket packet = new PlayerUpdatePacket();
                    packet.hasGodMode = LocalPlayer.getInstance().isHasGodMode();
                    packet.newAuraColor = LocalPlayer.getInstance().getAuraColor();
                    Color color = new Color();
                    Color.rgba8888ToColor(color, packet.newAuraColor);
                    packet.newSmiley = img.id;
                    GameClient.client.sendTCP(packet);
                    return true;
                }
            });
            smileyShowcases.add(img);
        }

        List<ImageWithID> auraShowcases = new ArrayList<>();

        for (int i = 0; i < SmileyManager.getInstance().getAuraCount(); i++) {
            Aura aura = SmileyManager.getInstance().getAuraByID(i);
            if (aura == null) continue;
            ImageWithID img = new ImageWithID(i, new TextureRegionDrawable(aura.getTexture()));
            img.setPosition(10 + (i % 4 * 64), barHeight + 96 + ((int) (i / 4f) * 64));
            img.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    PlayerUpdatePacket packet = new PlayerUpdatePacket();
                    packet.hasGodMode = LocalPlayer.getInstance().isHasGodMode();
                    packet.newAuraColor = LocalPlayer.getInstance().getAuraColor();
                    packet.newAura = img.id;
                    GameClient.client.sendTCP(packet);
                    return true;
                }
            });
            auraShowcases.add(img);
        }

        Image auraWheelDot = new Image(Utils.dot);
        auraWheelDot.setSize(10, 10);
        auraWheelDot.setColor(0, 0, 0, 1);
        auraWheelDot.setTouchable(Touchable.disabled);
        Image auraWheel = new Image(Utils.colorWheel);
        auraWheel.setPosition(12, barHeight + 12);
        auraWheel.setSize(96, 96);
        auraWheel.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                touchDragged(event, x, y, pointer);
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                int rgba = Utils.colorPixmap.getPixel((int) (x * 150 / 96), (int) (Utils.colorPixmap.getHeight() - y * 150 / 96));
                Color color = new Color();
                Color.rgba8888ToColor(color, rgba);
                if (color.a == 1) {
                    PlayerUpdatePacket packet = new PlayerUpdatePacket();
                    packet.hasGodMode = LocalPlayer.getInstance().isHasGodMode();
                    packet.newAuraColor = rgba;
                    GameClient.client.sendTCP(packet);
                    auraWheelDot.setPosition(7 + x, barHeight + 7 + y);
                }
            }
        });

        auraWheelDot.setPosition(auraWheel.getX() + 96 / 2f - 5, auraWheel.getY() + 96 / 2f - 5);

        TextButton auraPresetWhite = new TextButton("Set to white", UIStyles.defButtStyle);
        auraPresetWhite.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                PlayerUpdatePacket packet = new PlayerUpdatePacket();
                packet.hasGodMode = LocalPlayer.getInstance().isHasGodMode();
                packet.newAuraColor = Color.rgba8888(Color.WHITE);
                GameClient.client.sendTCP(packet);
                auraWheelDot.setPosition(auraWheel.getX() + 96 / 2f - 5, auraWheel.getY() + 96 / 2f - 5);
                return true;
            }
        });
        auraPresetWhite.setPosition(auraWheel.getX() + auraWheel.getWidth(), barHeight + 10);

        TextButton auraPresetRainbow = new TextButton("RaInBoW MoDe", UIStyles.defButtStyle);
        auraPresetRainbow.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                PlayerUpdatePacket packet = new PlayerUpdatePacket();
                packet.hasGodMode = LocalPlayer.getInstance().isHasGodMode();
                packet.newAuraColor = Color.rgba8888(Color.CLEAR);
                GameClient.client.sendTCP(packet);
                return true;
            }
        });
        auraPresetRainbow.setPosition(auraWheel.getX() + auraWheel.getWidth(), barHeight + 35);

        TextButton auraPresetBlack = new TextButton("RaInBoW MoDe", UIStyles.defButtStyle);
        auraPresetBlack.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                PlayerUpdatePacket packet = new PlayerUpdatePacket();
                packet.hasGodMode = LocalPlayer.getInstance().isHasGodMode();
                packet.newAuraColor = Color.rgba8888(Color.BLACK);
                GameClient.client.sendTCP(packet);
                return true;
            }
        });
        auraPresetBlack.setPosition(auraWheel.getX() + auraWheel.getWidth(), barHeight + 35);


        smileyMenu.addActor(smileyHUD);
        for (ImageWithID img :
                smileyShowcases) {
            smileyMenu.addActor(img);
        }
        smileyMenu.setVisible(false);

        auraMenu.addActor(auraHUD);
        for (ImageWithID img :
                auraShowcases) {
            auraMenu.addActor(img);
        }
        auraMenu.addActor(auraWheel);
        auraMenu.addActor(auraPresetWhite);
        auraMenu.addActor(auraPresetRainbow);
        auraMenu.addActor(auraWheelDot);
        auraMenu.setVisible(false);

        HUD.addActor(debugLab);
        HUD.addActor(inspectToolLab);

        HUD.addActor(HUDChat);
        HUD.addActor(HUDBar);

        HUD.addActor(gotoLobbyButt);
        HUD.addActor(smileyButt);
        HUD.addActor(auraButt);

        HUD.addActor(auraMenu);
        HUD.addActor(smileyMenu);
    }

    public static WorldScreen getInstance() {
        return singleton;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(HUD);
        // create a collision world
        for (BlockGroup group :
                WorldManager.getInstance().EEWorld.blocks) {
            for (int i = 0; i < group.xPositions.size(); i++) {
                WorldManager.getInstance().collWorld.add(group.perBlockItems.get(i), group.xPositions.get(i) * 16, 480 - group.yPositions.get(i) * 16, 16, 16);
            }
        }
        LocalPlayer.getInstance().prepare();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        Main.viewport.apply();
        worldBatch.setProjectionMatrix(Main.viewport.getCamera().combined);
        debugBatch.setProjectionMatrix(Main.viewport.getCamera().combined);
        float camX = Main.viewport.getCamera().position.x;
        float camY = Main.viewport.getCamera().position.y;
        float camW = Main.viewport.getCamera().viewportWidth;
        float camH = Main.viewport.getCamera().viewportHeight;
        Vector2 MainCursor = Main.viewport.unproject(new Vector2(input.getX(), input.getY()));
        Vector2 HUDCursor = Main.hudViewport.unproject(new Vector2(input.getX(), input.getY()));

        LocalPlayer.getInstance().update();

        if (!GameClient.client.isConnected()) {
            GameClient.hasJoined = false;
            GameClient.client.close();
            GameClient.fallbackText.setText("Server is down!");
            ScreenManager.setScreen(SplashScreen.getInstance());
        }

        if (!blocksObstructed) {
            if (input.isButtonPressed(Input.Buttons.LEFT)) {
                //Block
                BlockGroup.BaseBlock block = new BlockGroup.BaseBlock(WorldManager.getInstance().convertXToBlock(MainCursor.x), WorldManager.getInstance().convertYToBlock(MainCursor.y), 15, 0);
                boolean placed = WorldManager.getInstance().EEWorld.setBlock(block);
            }
            if (input.isButtonPressed(Input.Buttons.RIGHT)) {
                //Block
                BlockGroup.BaseBlock block = new BlockGroup.BaseBlock(WorldManager.getInstance().convertXToBlock(MainCursor.x), WorldManager.getInstance().convertYToBlock(MainCursor.y), 0, 0);
                boolean placed = WorldManager.getInstance().EEWorld.setBlock(block);
            }
        }

        if (input.isKeyJustPressed(Input.Keys.F3)) {
            devMenu = !devMenu;
        }
        debugLab.setVisible(devMenu ||  LocalPlayer.getInstance().isDevCam());
        if (input.isKeyJustPressed(Input.Keys.I)) {
            inspectTool = !inspectTool;
            inspectToolLab.setVisible(inspectTool);
        }

        if (devMenu) {
            if(!LocalPlayer.getInstance().isDevCam()) {
                debugLab.setText("ping: " + GameClient.client.getReturnTripTime() +
                        "\nfps: " + Gdx.graphics.getFramesPerSecond() +
                        "\npos: " + LocalPlayer.getInstance().getPos());
            } else {
                debugLab.setText("ping: " + GameClient.client.getReturnTripTime() +
                        "\nfps: " + Gdx.graphics.getFramesPerSecond() +
                        "\npos: " + LocalPlayer.getInstance().getPos() +
                        "\nDEV CAM ON. CONTROLS: 4,6,5,8. +/- SPEED");
            }
        } else if(LocalPlayer.getInstance().isDevCam()) {
            debugLab.setText("\nDEV CAM ON. CONTROLS: 4,6,5,8. +/- SPEED");
        }
        if (inspectTool) {
            int x = WorldManager.getInstance().convertXToBlock(MainCursor.x);
            int y = WorldManager.getInstance().convertYToBlock(MainCursor.y);
            if (x != -1 && y != -1) {
                BlockGroup.BaseBlock block = WorldManager.getInstance().EEWorld.getBlockAt(x, y, 0);
                inspectToolLab.setText("ID: " + block.getId() +
                        "\nTags: " + Arrays.toString(BlockManager.getInstance().bricks.get(block.getId()).tags) +
                        "\nX: " + x +
                        "\nY: " + y);
            } else inspectToolLab.setText("Out of bounds!");
            inspectToolLab.setPosition(HUDCursor.x, HUDCursor.y);
        }
        worldBatch.begin();
        TextureRegion air = BlockManager.getInstance().brickRegions.get(0);
        for (int i = 0; i < WorldManager.getInstance().EEWorld.worldWidth; i++) {
            for (int j = 0; j < WorldManager.getInstance().EEWorld.worldHeight; j++) {
                Vector2 pos = new Vector2(i * 16, j * 16);
                pos.y = Main.viewport.getCamera().viewportHeight - pos.y;
                if (Rect.rect_isIntersecting(camX - camW / 2, camY - camH / 2, camW, camH, pos.x, pos.y, 16, 16)) {
                    worldBatch.draw(air, pos.x, pos.y);
                }
            }
        }

        for (BlockGroup group :
                WorldManager.getInstance().EEWorld.blocks) {
            TextureRegion reg = BlockManager.getInstance().brickRegions.get(group.blockId);
            for (int i = 0; i < group.xPositions.size(); i++) {
                Vector2 pos = new Vector2(group.xPositions.get(i) * 16, group.yPositions.get(i) * 16);
                pos.y = camH - pos.y;
                if (Rect.rect_isIntersecting(camX - camW / 2, camY - camH / 2, camW, camH, pos.x, pos.y, 16, 16)) {
                    worldBatch.draw(reg, pos.x, pos.y);
                }
            }
        }
        try {
            for (Player player :
                    PlayerManager.getInstance().players.values()) {
                try {
                    player.predict();
                    player.draw(worldBatch);
                } catch (ConcurrentModificationException ignored) {
                }
            }
        } catch (ConcurrentModificationException ignored) {

        }
        LocalPlayer.getInstance().draw(worldBatch);
        worldBatch.end();
        if (devMenu) {
            debugBatch.begin(ShapeRenderer.ShapeType.Line);
            debugBatch.setColor(0, 1, 0, 1);
            for (Rect rect :
                    WorldManager.getInstance().collWorld.getRects()) {
                debugBatch.rect(rect.x, rect.y, rect.w, rect.h);
            }
            debugBatch.setColor(1, 0, 0, 1);
            debugBatch.end(); // Supposed to be for inspect
        }

        // Draw HUD
        HUD.act();
        HUD.draw();
    }

    @Override
    public void resize(int width, int height) {
        Main.viewport.update(width, height);
        Main.hudViewport.update(width, height);
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
