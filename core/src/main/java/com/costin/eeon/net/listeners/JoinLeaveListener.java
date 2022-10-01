package com.costin.eeon.net.listeners;

import com.badlogic.gdx.graphics.Color;
import com.costin.eeon.game.Laws;
import com.costin.eeon.game.players.LocalPlayer;
import com.costin.eeon.game.players.Player;
import com.costin.eeon.game.players.PlayerManager;
import com.costin.eeon.game.world.BlockGroup;
import com.costin.eeon.game.world.EEWorld;
import com.costin.eeon.game.world.WorldManager;
import com.costin.eeon.graphic.ScreenManager;
import com.costin.eeon.graphic.scenes.SplashScreen;
import com.costin.eeon.net.GameClient;
import com.costin.eeon.net.packets.info.BlockGroupPacket;
import com.costin.eeon.net.packets.player.*;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

import java.util.ArrayList;
import java.util.List;

public class  JoinLeaveListener implements Listener {
    @Override
    public void connected(Connection connection) {
        JoinRequestPacket packet = new JoinRequestPacket();
        packet.desiredUsername = "User";
        packet.desiredSmiley = 0;
        packet.desiredGolden = false;
        packet.clientVersion = Laws.clientVersion;
        LocalPlayer.getInstance().setLocalSmiley(packet.desiredSmiley);
        //noinspection ConstantConditions
        LocalPlayer.getInstance().setLocalGolden(packet.desiredGolden);
        connection.sendTCP(packet);
    }

    @Override
    public void disconnected(Connection connection) {
        GameClient.hasJoined = false;
    }

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof ConnectionDenyPacket) {
            PacketEnums.ConnectionDenyReason reason = ((ConnectionDenyPacket) object).reason;
            switch (reason) {
                case UNKNOWN_VERSION:
                    GameClient.fallbackText.setText("Kicked: Client's on a unknown version!");
                    break;
                case OLD_VERSION:
                    GameClient.fallbackText.setText("Kicked: Client's on an outdated version!");
                    break;
                case NEW_VERSION:
                    GameClient.fallbackText.setText("Kicked: Server's on an outdated version!");
            }
        }
        if (object instanceof AutoKickPacket) {
            PacketEnums.AutoKickReason reason = ((AutoKickPacket) object).reason;
            switch (reason) {
                case CLIENTSIDE_ERROR:
                    GameClient.fallbackText.setText("Kicked: Unexpected clientside error");
                    break;
                case SERVERSIDE_ERROR:
                    GameClient.fallbackText.setText("Kicked: Unexpected serverside error");
                    break;
            }
            GameClient.hasJoined = false;
            GameClient.client.close();
            ScreenManager.setScreen(SplashScreen.getInstance());
        }
        if (object instanceof RequestAcceptedPacket) {
            RequestAcceptedPacket packet = (RequestAcceptedPacket) object;
            if (GameClient.hasJoined) return;
            WorldManager.getInstance().collWorld.reset();
            WorldManager.getInstance().EEWorld.reset();
            WorldManager.getInstance().collWorld.add(LocalPlayer.getInstance(), packet.x + 1, packet.y + 1, 14, 14);
            WorldManager.getInstance().collWorld.add(LocalPlayer.getInstance().innerCollision, packet.x, packet.y + 1, 16, 15);
            WorldManager.getInstance().collWorld.add(LocalPlayer.getInstance().actionCollision, packet.x - 6, packet.y - 6, 12, 12);
            PlayerManager.getInstance().players.clear();
            LocalPlayer.getInstance().setLocalUsername(packet.newUsername);
            LocalPlayer.getInstance().setLocalPosition(packet.x, packet.y);
            LocalPlayer.getInstance().setLocalGolden(false);
            LocalPlayer.getInstance().setLocalVelocity(0, 0);
            LocalPlayer.getInstance().setLocalSmiley(0);
            LocalPlayer.getInstance().setLocalAuraColor(Color.rgba8888(Color.WHITE));
            LocalPlayer.getInstance().setLocalAura(0);
            LocalPlayer.getInstance().setLocalGodMode(false);
            List<BlockGroup> blockGroups = new ArrayList<>();
            for (BlockGroupPacket groupPacket :
                    packet.EEWorld.blocks) {
                BlockGroup group = new BlockGroup(groupPacket.blockId, groupPacket.layer, groupPacket.xPositions, groupPacket.yPositions);
                blockGroups.add(group);
            }
            WorldManager.getInstance().EEWorld = new EEWorld(packet.EEWorld.owner, packet.EEWorld.title, packet.EEWorld.worldWidth, packet.EEWorld.worldHeight, packet.EEWorld.gravityMultiplier, packet.EEWorld.bgColor, packet.EEWorld.description, packet.EEWorld.isCampaign, packet.EEWorld.crewId, packet.EEWorld.crewName, packet.EEWorld.crewStatus, packet.EEWorld.minimapEnabled, packet.EEWorld.ownerId);
            WorldManager.getInstance().EEWorld.blocks.clear();
            WorldManager.getInstance().EEWorld.blocks.addAll(blockGroups);
            packet.players.forEach((integer, playerPacket) -> {
                Player ply = new Player(playerPacket.username);
                WorldManager.getInstance().collWorld.add(ply, packet.x + 1, packet.y + 1, 14, 14);
                WorldManager.getInstance().collWorld.add(ply.innerCollision, packet.x + 2, packet.y + 2, 12, 12);
                WorldManager.getInstance().collWorld.add(ply.actionCollision, packet.x - 6, packet.y - 6, 12, 12);
                ply.setLocalVelocity(playerPacket.vX, playerPacket.vY);
                ply.setLocalPosition(playerPacket.x, playerPacket.y);
                ply.setLocalAura(playerPacket.aura);
                ply.setLocalGolden(playerPacket.golden);
                ply.setLocalGodMode(playerPacket.god);
                ply.setLocalSmiley(playerPacket.smiley);
                ply.setLocalAuraColor(playerPacket.auraColor);
                PlayerManager.getInstance().players.put(integer, ply);
            });
            GameClient.hasJoined = true;
        }

        if (object instanceof PlayerJoinPacket) {
            PlayerJoinPacket packet = (PlayerJoinPacket) object;
            Player ply = new Player(packet.username);
            WorldManager.getInstance().collWorld.add(ply, packet.x + 1, packet.y + 1, 14, 14);
            WorldManager.getInstance().collWorld.add(ply.innerCollision, packet.x + 2, packet.y + 2, 12, 12);
            WorldManager.getInstance().collWorld.add(ply.actionCollision, packet.x - 6, packet.y - 6, 12, 12);
            ply.setLocalVelocity(0, 0);
            ply.setLocalPosition(packet.x, packet.y);
            ply.setLocalAura(packet.aura);
            ply.setLocalGolden(packet.golden);
            ply.setLocalGodMode(packet.god);
            ply.setLocalSmiley(packet.smiley);
            PlayerManager.getInstance().players.put(packet.playerID, ply);
            Log.info(packet.username + " has joined!");
        }
        if (object instanceof PlayerLeftPacket) {
            PlayerLeftPacket packet = (PlayerLeftPacket) object;
            Player ply = PlayerManager.getInstance().players.remove(packet.playerID);
            if (ply == null) {
                Log.error("eeon", "unknown player left, id: " + packet.playerID);
            } else {
                WorldManager.getInstance().collWorld.remove(ply);
                WorldManager.getInstance().collWorld.remove(ply.innerCollision);
                WorldManager.getInstance().collWorld.remove(ply.actionCollision);
                Log.info("eeon", ply.getUsername() + " has left!");
            }
        }
    }
}
