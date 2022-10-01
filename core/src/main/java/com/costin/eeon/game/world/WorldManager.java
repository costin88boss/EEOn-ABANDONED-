package com.costin.eeon.game.world;

import com.costin.eeon.Main;
import com.costin.eeon.game.GameObject;
import com.dongbat.jbump.World;

public class WorldManager {
    private static WorldManager singleton;
    public EEWorld EEWorld;
    public World<GameObject> collWorld;

    public WorldManager() {
        singleton = this;
        EEWorld = new EEWorld();
        collWorld = new World<>(16);
    }

    public static WorldManager getInstance() {
        return singleton;
    }

    public int convertXToBlock(float x) {
        return x > -0 && x/16 < EEWorld.worldWidth ? (int) (x/16) : -1;
    }
    public int convertYToBlock(float y) {
        return (Main.viewport.getCamera().viewportHeight - y) > -16 && ((Main.viewport.getCamera().viewportHeight - y) / 16 + 1) < WorldManager.getInstance().EEWorld.worldHeight ? (int) ((Main.viewport.getCamera().viewportHeight - y)/16+1) : -1;
    }
}
