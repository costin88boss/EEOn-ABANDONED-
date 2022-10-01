package com.costin.eeon.net.listeners;

import com.costin.eeon.game.players.LocalPlayer;
import com.costin.eeon.game.players.Player;
import com.costin.eeon.game.players.PlayerManager;
import com.costin.eeon.net.GameClient;
import com.costin.eeon.net.packets.player.updates.serverside.ServerMovePacket;
import com.costin.eeon.net.packets.player.updates.serverside.ServerPlyUpdatePacket;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class  PlayerListener implements Listener {
    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof ServerMovePacket) {
            ServerMovePacket packet = (ServerMovePacket) object;
            if (packet.id == GameClient.client.getID()) {
                LocalPlayer.getInstance().updatePacket(packet, connection);
            } else {
                Player ply = PlayerManager.getInstance().players.get(packet.id);
                if (ply == null) return;
                ply.updatePacket(packet, connection);
            }
        }
        if (object instanceof ServerPlyUpdatePacket) {
            ServerPlyUpdatePacket packet = (ServerPlyUpdatePacket) object;
            if (packet.id == GameClient.client.getID()) {
                LocalPlayer.getInstance().setLocalGodMode(packet.hasGodMode);
                if (packet.newSmiley != -1) LocalPlayer.getInstance().setLocalSmiley(packet.newSmiley);
                if (packet.newAura != -1) LocalPlayer.getInstance().setLocalAura(packet.newAura);
                LocalPlayer.getInstance().setLocalAuraColor(packet.newAuraColor);
            } else {
                Player ply = PlayerManager.getInstance().players.get(packet.id);
                if (ply == null) return;
                ply.setLocalGodMode(packet.hasGodMode);
                if (packet.newSmiley != -1) ply.setLocalSmiley(packet.newSmiley);
                if (packet.newAura != -1) ply.setLocalAura(packet.newAura);
                ply.setLocalAuraColor(packet.newAuraColor);
            }
        }
    }
}
