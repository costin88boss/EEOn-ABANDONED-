package com.costin.eeon.net.packets.info;

public class PlayerChangePacket {
    public boolean isAdminStateChanged;
    public boolean isAdmin;
    public boolean isRenamed;
    public String[] names; // who is this guy
    public String[] newNames; // only if isRenamed
}