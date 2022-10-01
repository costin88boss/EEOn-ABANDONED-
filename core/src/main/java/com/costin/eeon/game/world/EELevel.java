package com.costin.eeon.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.costin.eeon.game.world.items.ItemId;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class  EELevel {

    public static List<Integer> getUIntArray(DataInputStream data) throws IOException {
        int count = data.readInt();
        List<Integer> arr2 = new ArrayList<>();

        for (int i = 0; i < count / 2; i++) {
            arr2.add(data.readUnsignedShort());
        }
        return arr2;
    }

    public static EEWorld read(String fileName) {
        ByteArrayInputStream in = new ByteArrayInputStream(Gdx.files.internal(fileName + ".eelvl").readBytes());
        InflaterInputStream inflater = new InflaterInputStream(in, new Inflater(true));
        DataInputStream data = new DataInputStream(new BufferedInputStream(inflater));

        EEWorld EEWorld;

        try {
            Color bgColor = new Color();
            String owner = data.readUTF();
            String title = data.readUTF();
            int worldWidth = data.readInt();
            int worldHeight = data.readInt();
            float gravityMultiplier = data.readFloat();
            Color.rgba8888ToColor(bgColor, data.readInt());
            String description = data.readUTF();
            boolean isCampaign = data.readBoolean();
            String crewId = data.readUTF();
            String crewName = data.readUTF();
            int crewStatus = data.readInt();
            boolean minimapEnabled = data.readBoolean();
            String ownerId = data.readUTF();

            EEWorld = new EEWorld(owner, title, worldWidth, worldHeight, gravityMultiplier, bgColor, description, isCampaign, crewId, crewName, crewStatus, minimapEnabled, ownerId);

            int rotateableCount = 0;
            int portalCount = 0;
            int labelCount = 0;
            int signCount = 0;
            int worldPortalCount = 0;
            int npcCount = 0;
            int blockCount = 0;

            data.mark(0);
            while (data.read() != -1) {
                data.reset();
                BlockGroup blocks = new BlockGroup(data.readInt(), data.readInt(), getUIntArray(data), getUIntArray(data));

                if (ItemId.isBlockRotateable(blocks.blockId) || ItemId.isNonRotatableHalfBlock(blocks.blockId) || ItemId.isBlockNumbered(blocks.blockId)
                        || blocks.blockId == ItemId.GUITAR || blocks.blockId == ItemId.DRUMS || blocks.blockId == ItemId.PIANO
                        || blocks.blockId == ItemId.SPIKE || blocks.blockId == ItemId.SPIKE_SILVER || blocks.blockId == ItemId.SPIKE_BLACK
                        || blocks.blockId == ItemId.SPIKE_RED || blocks.blockId == ItemId.SPIKE_GOLD || blocks.blockId == ItemId.SPIKE_GREEN || blocks.blockId == ItemId.SPIKE_BLUE) {
                    Object rot = data.readInt();
                    rotateableCount++;
                } else if (blocks.blockId == ItemId.PORTAL || blocks.blockId == ItemId.PORTAL_INVISIBLE) {
                    Object rot = data.readInt();
                    Object id = data.readInt();
                    Object target = data.readInt();
                    portalCount++;
                } else if (blocks.blockId == ItemId.TEXT_SIGN) {
                    Object text = data.readUTF();
                    Object rot = data.readInt();
                    signCount++;
                } else if (blocks.blockId == ItemId.WORLD_PORTAL) {
                    Object worldId = data.readUTF();
                    Object spawnId = data.readInt();
                    worldPortalCount++;
                } else if (blocks.blockId == ItemId.LABEL) {
                    Object text = data.readUTF();
                    Object color = data.readUTF();
                    Object width = data.readInt();
                    labelCount++;
                } else if (ItemId.isNPC(blocks.blockId)) {
                    Object name = data.readUTF();
                    Object text1 = data.readUTF();
                    Object text2 = data.readUTF();
                    Object text3 = data.readUTF();
                    npcCount++;
                } else {

                    blockCount++;
                }

                data.mark(0);
                for (int i = 0; i < blocks.xPositions.size(); i++) {
                    BlockGroup.BaseBlock block = new BlockGroup.BaseBlock(blocks.xPositions.get(i), blocks.yPositions.get(i), blocks.blockId, blocks.layer);
                    EEWorld.setBlock(block);
                }
            }

            inflater.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return EEWorld;
    }
}

