package com.costin.eeon.game.world;

import com.costin.eeon.game.GameObject;
import com.dongbat.jbump.Item;

import java.util.ArrayList;
import java.util.List;

public class BlockGroup {
    public final int blockId;
    public final int layer;
    public final List<Integer> xPositions;
    public final List<Integer> yPositions;
    public final List<Item<GameObject>> perBlockItems;

    public BlockGroup() {
        this.blockId = 0;
        this.layer = 0;
        this.xPositions = new ArrayList<>();
        this.yPositions = new ArrayList<>();
        perBlockItems = new ArrayList<>();
    }

    public BlockGroup(int blockId, int layer, List<Integer> xPositions, List<Integer> yPositions) {
        this.blockId = blockId;
        this.layer = layer;
        this.xPositions = xPositions;
        this.yPositions = yPositions;
        perBlockItems = new ArrayList<>();
        for (int i = 0; i < this.xPositions.size(); i++) {
            perBlockItems.add(new Item<>(new BaseBlock(xPositions.get(i), yPositions.get(i), blockId, layer)));
        }
    }

    // BaseBlock = block with no stuff like rotation, etc.
    public static class BaseBlock extends GameObject {
        int x, y, id, layer;

        public BaseBlock(int x, int y, int id, int layer) {
            super();
            this.x = x;
            this.y = y;
            this.id = id;
            this.layer = layer;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getId() {
            return id;
        }

        public int getLayer() {
            return layer;
        }
    }
}
