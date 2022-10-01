package com.costin.eeon.game.players;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.costin.eeon.Main;
import com.costin.eeon.game.Laws;
import com.costin.eeon.net.GameClient;
import com.costin.eeon.net.packets.player.updates.clientside.PlayerMovePacket;
import com.costin.eeon.net.packets.player.updates.clientside.PlayerUpdatePacket;

import static com.badlogic.gdx.Gdx.input;

public class  LocalPlayer extends Player {

    private static LocalPlayer instance;
    private final Vector2 camPos = new Vector2();
    private boolean devCam = false;
    private float devSpeed = 250;

    public LocalPlayer() {
        super("");
        instance = this;
    }

    public static LocalPlayer getInstance() {
        return instance;
    }

    public void prepare() {
        Vector2 pos = getPos();
        Main.viewport.getCamera().position.set(pos.x, pos.y, 0);
        camPos.x = Main.viewport.getCamera().position.x;
        camPos.y = Main.viewport.getCamera().position.y;


    }

    private void move() {
        PlayerMovePacket packet = new PlayerMovePacket();
        if (input.isKeyPressed(Input.Keys.D) || input.isKeyPressed(Input.Keys.RIGHT)) {
            packet.xAction = 1;
            diffX = Laws.playerForce / 100;
        } else if (input.isKeyPressed(Input.Keys.A) || input.isKeyPressed(Input.Keys.LEFT)) {
            packet.xAction = -1;
            diffX = -(Laws.playerForce / 100);
        }
        if (isHasGodMode()) {
            if (input.isKeyPressed(Input.Keys.W) || input.isKeyPressed(Input.Keys.UP)) {
                packet.yAction = 1;
                diffY = Laws.playerForce / 100;
            }
            if (input.isKeyPressed(Input.Keys.S) || input.isKeyPressed(Input.Keys.DOWN)) {
                packet.yAction = -1;
                diffY = -(Laws.playerForce / 100);
            }
        }
        if (input.isKeyPressed(Input.Keys.SPACE)) {
            if (isGrounded || !oldPacket) {
                packet.spaced = true;
                diffY = Laws.jumpHeight / 4;
                isGrounded = false;
            }
        }
        if (input.isKeyJustPressed(Input.Keys.G)) {
            PlayerUpdatePacket update = new PlayerUpdatePacket();
            update.hasGodMode = !isHasGodMode();
            update.newAuraColor = LocalPlayer.getInstance().getAuraColor();
            GameClient.client.sendTCP(update);
        }

        if (input.isKeyJustPressed(Input.Keys.NUMPAD_DOT)) {
            devCam = !devCam;
        }
        if (input.isKeyJustPressed(Input.Keys.NUMPAD_ADD)) {
            devSpeed += 50;
        }
        if (input.isKeyJustPressed(Input.Keys.NUMPAD_SUBTRACT)) {
            if (devSpeed - 50 > 0) devSpeed -= 50;
        }

        if (!devCam) {
            camPos.scl(1 - Laws.cameraLag * 3);
            Vector2 pos = getPos();
            camPos.add(pos.x * Laws.cameraLag * 3, pos.y * Laws.cameraLag * 3);
        } else {
            if (input.isKeyPressed(Input.Keys.NUMPAD_8)) camPos.add(0, devSpeed * Gdx.graphics.getDeltaTime());
            if (input.isKeyPressed(Input.Keys.NUMPAD_6)) camPos.add(devSpeed * Gdx.graphics.getDeltaTime(), 0);
            if (input.isKeyPressed(Input.Keys.NUMPAD_5)) camPos.add(0, -devSpeed * Gdx.graphics.getDeltaTime());
            if (input.isKeyPressed(Input.Keys.NUMPAD_4)) camPos.add(-devSpeed * Gdx.graphics.getDeltaTime(), 0);
        }
        Main.viewport.getCamera().position.set(camPos, 0);

        GameClient.client.sendUDP(packet);
    }

    public void update() {
        GameClient.client.updateReturnTripTime();
        move();
        predict();
    }
}
