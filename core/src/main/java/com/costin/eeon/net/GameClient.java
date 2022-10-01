package com.costin.eeon.net;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.costin.eeon.game.world.BlockGroup;
import com.costin.eeon.game.world.EEWorld;
import com.costin.eeon.net.listeners.JoinLeaveListener;
import com.costin.eeon.net.listeners.PlayerListener;
import com.costin.eeon.net.packets.info.BlockGroupPacket;
import com.costin.eeon.net.packets.info.WorldPacket;
import com.costin.eeon.net.packets.player.*;
import com.costin.eeon.net.packets.player.updates.clientside.PlayerMovePacket;
import com.costin.eeon.net.packets.player.updates.clientside.PlayerUpdatePacket;
import com.costin.eeon.net.packets.player.updates.serverside.ServerMovePacket;
import com.costin.eeon.net.packets.player.updates.serverside.ServerPlyUpdatePacket;
import com.dongbat.jbump.Item;
import com.esotericsoftware.kryonet.Client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class GameClient {
    static final int port = 20600;
    public static Client client;
    public static Label fallbackText;
    public static boolean hasJoined = false;

    private static void registerPackets() {
        client.getKryo().register(JoinRequestPacket.class);
        client.getKryo().register(PlayerJoinPacket.class);
        client.getKryo().register(PlayerLeftPacket.class);
        client.getKryo().register(RequestAcceptedPacket.class);
        client.getKryo().register(PlayerMovePacket.class);
        client.getKryo().register(ServerMovePacket.class);
        client.getKryo().register(ConnectionDenyPacket.class);
        client.getKryo().register(PacketEnums.ConnectionDenyReason.class);
        client.getKryo().register(PlayerUpdatePacket.class);
        client.getKryo().register(ServerPlyUpdatePacket.class);
        client.getKryo().register(AutoKickPacket.class);
        client.getKryo().register(PacketEnums.AutoKickReason.class);

        client.getKryo().register(PlayerPacket.class);
        client.getKryo().register(WorldPacket.class);
        client.getKryo().register(BlockGroupPacket.class);

        client.getKryo().register(HashMap.class);
        client.getKryo().register(ArrayList.class);

        client.getKryo().register(EEWorld.class);
        client.getKryo().register(Color.class);
        client.getKryo().register(BlockGroup.class);
        client.getKryo().register(Item.class);
    }

    public static void init() {
        client = new Client(8192, 16384);
        client.addListener(new JoinLeaveListener());
        client.addListener(new PlayerListener());
        registerPackets();
        client.start();
    }

    public static void createConnection(String ip, Label fallbackText) {
        if (hasJoined) return;

        System.out.println("Connecting to server " + ip);

        GameClient.fallbackText = fallbackText;

        try {
            client.connect(5000, ip, port, port);
        } catch (IOException e) {
            fallbackText.setText(e.getMessage());
            e.printStackTrace();
            return;
        }
        client.setKeepAliveUDP(5000);
        client.setKeepAliveTCP(5000);

        fallbackText.setText("Connected. sending player info..");
        System.out.println("Connected. sending player info..");
    }
}
