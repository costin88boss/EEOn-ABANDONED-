package com.costin.eeon.game.players;

import java.util.HashMap;

public class PlayerManager {

    private static PlayerManager singleton;
    public HashMap<Integer, Player> players;

    public PlayerManager() {
        singleton = this;
        players = new HashMap<>();
    }

    public static PlayerManager getInstance() {
        return singleton;
    }

}

