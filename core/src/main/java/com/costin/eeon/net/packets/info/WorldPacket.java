package com.costin.eeon.net.packets.info;

import com.badlogic.gdx.graphics.Color;

import java.util.List;

public class WorldPacket {
    public List<BlockGroupPacket> blocks;
    public String owner;
    public String title;
    public int worldWidth;
    public int worldHeight;
    public float gravityMultiplier;
    public Color bgColor;
    public String description;
    public boolean isCampaign;
    public String crewId;
    public String crewName;
    public int crewStatus;
    public boolean minimapEnabled;
    public String ownerId;
}
