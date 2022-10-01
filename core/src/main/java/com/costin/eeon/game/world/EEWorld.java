package com.costin.eeon.game.world;

import com.badlogic.gdx.graphics.Color;
import com.costin.eeon.Main;
import com.costin.eeon.game.GameObject;
import com.costin.eeon.game.world.items.BlockManager;
import com.dongbat.jbump.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class  EEWorld {
    public final List<BlockGroup> blocks;
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

    public EEWorld() {
        blocks = new ArrayList<>();
        reset();
    }

    public EEWorld(String owner, String title, int worldWidth, int worldHeight, float gravityMultiplier, Color bgColor, String description, boolean isCampaign, String crewId, String crewName, int crewStatus, boolean minimapEnabled, String ownerId) {
        this.owner = owner;
        this.title = title;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.gravityMultiplier = gravityMultiplier;
        this.bgColor = bgColor;
        this.description = description;
        this.isCampaign = isCampaign;
        this.crewId = crewId;
        this.crewName = crewName;
        this.crewStatus = crewStatus;
        this.minimapEnabled = minimapEnabled;
        this.ownerId = ownerId;
        blocks = new ArrayList<>();
    }

    public BlockGroup getGroupAt(int x, int y, int layer) {
        Optional<BlockGroup> group = blocks.stream().filter(blockGroup -> {
            for (int i = 0; i < blockGroup.xPositions.size(); i++) {
                if (blockGroup.xPositions.get(i) == x
                        && blockGroup.yPositions.get(i) == y
                        && blockGroup.layer == layer) {
                    return true;
                }
            }
            return false;
        }).findFirst();
        return group.orElse(null);
    }

    public BlockGroup getGroupWith(int id, int layer) {
        Optional<BlockGroup> group = blocks.stream().filter(blockGroup -> {
            for (int i = 0; i < blockGroup.xPositions.size(); i++) {
                if (blockGroup.blockId == id
                        && blockGroup.layer == layer) {
                    return true;
                }
            }
            return false;
        }).findFirst();
        return group.orElse(null);
    }

    public BlockGroup.BaseBlock getBlockAt(int x, int y, int layer) {
        BlockGroup.BaseBlock block = new BlockGroup.BaseBlock(x, y, 0, layer);
        BlockGroup group = getGroupAt(x, y, layer);
        if (group != null) {
            block.id = group.blockId;
            block.layer = group.layer;
        }
        return block;
    }

    private boolean checkAndAddBlock(int id, int layer, int x, int y) {
        if(id == 0) return false;
        BlockGroup group;
        group = getGroupWith(id, layer);
        if (group == null) {
            group = new BlockGroup(id, layer, new ArrayList<>(), new ArrayList<>());
            for (int i = 0; i < group.xPositions.size(); i++) {
                WorldManager.getInstance().collWorld.add(group.perBlockItems.get(i), group.xPositions.get(i) * 16, group.yPositions.get(i) * 16, 16, 16);
            }
            blocks.add(group);
        } else {
            if (getGroupAt(x, y, layer) == group) {
                return false;
            }
            group.xPositions.add(x);
            group.yPositions.add(y);
            Item<GameObject> item = new Item<>(new BlockGroup.BaseBlock(x, y, id, layer));
            group.perBlockItems.add(item);
            WorldManager.getInstance().collWorld.add(item, x * 16, -(y - Main.viewport.getCamera().viewportHeight / 16) * 16, 16, 16);
        }
        return true;
    }

    public boolean setBlock(BlockGroup.BaseBlock block) {
        if (block.x < 0 || block.y < 0 || block.layer < 0 || block.id < 0
                || block.x > worldWidth || block.y > worldHeight) return false;
        BlockGroup group = getGroupAt(block.x, block.y, block.layer);
        if (group != null && group.blockId != block.id) {
            for (int i = 0; i < group.xPositions.size(); i++) {
                if (group.xPositions.get(i) == block.x
                        && group.yPositions.get(i) == block.y) {
                    group.xPositions.remove(i);
                    group.yPositions.remove(i);
                    Item<GameObject> item = group.perBlockItems.remove(i);
                    WorldManager.getInstance().collWorld.remove(item);
                    //System.out.println("Removed " + BlockManager.getInstance().getBlockTypeByID(i).codename);
                    break;
                }
            }
        }
        return checkAndAddBlock(block.id, block.layer, block.x, block.y);
    }

    public void printInfo() {
        System.out.println("Owner '" + owner + "', Title '" + title + "', Size " + worldWidth + "x" + worldHeight +
                ", Gravity Mp " + gravityMultiplier + ", Bgcolor " + bgColor + ", Description '" + description +
                "', Is Campaign? " + isCampaign + ", CrewID '" + crewId + "', Crew Name '" + crewName +
                "', CrewStatus " + crewStatus + ", Is Minimap Enabled? " + minimapEnabled + ", ownerId '" + ownerId + "'");
    }

    public void reset() {
        this.owner = "Unknown";
        this.title = "Untitled";
        this.worldWidth = 3;
        this.worldHeight = 3;
        this.gravityMultiplier = 1f;
        this.bgColor = new Color();
        this.description = "description";
        this.isCampaign = false;
        this.crewId = "";
        this.crewName = "";
        this.crewStatus = 0;
        this.minimapEnabled = true;
        this.ownerId = "";
        blocks.clear();
    }
}
