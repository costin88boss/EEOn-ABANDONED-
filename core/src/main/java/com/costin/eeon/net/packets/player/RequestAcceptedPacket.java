package com.costin.eeon.net.packets.player;

import com.costin.eeon.net.packets.info.WorldPacket;

import java.util.HashMap;

public class RequestAcceptedPacket {
    public String newUsername;
    public float x, y;
    public WorldPacket EEWorld;
    public HashMap<Integer, PlayerPacket> players;

}
