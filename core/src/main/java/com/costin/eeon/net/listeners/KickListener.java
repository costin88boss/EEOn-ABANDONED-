package com.costin.eeon.net.listeners;

import com.costin.eeon.game.sounds.SoundManager;
import com.costin.eeon.net.GameClient;
import com.costin.eeon.net.packets.player.AutoKickPacket;
import com.costin.eeon.net.packets.player.KickPacket;
import com.costin.eeon.net.packets.player.PacketEnums;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class KickListener implements Listener {
    @Override
    public void disconnected(Connection connection) {
        // idk why I put this method if I'm not using it rn
    }

    @Override
    public void received(Connection connection, Object object) {
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
        }
        if (object instanceof KickPacket) { // admin kicked me, report
            GameClient.kicked = true;
            KickPacket packet = (KickPacket) object;

            GameClient.hasJoined = false;
            GameClient.client.close();
            GameClient.fallbackText.setText(packet.reason);
            SoundManager.getInstance().banned.play(); // yay ban sound
        }
    }
}
