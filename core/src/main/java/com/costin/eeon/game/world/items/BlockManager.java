package com.costin.eeon.game.world.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;

public class BlockManager {

    private static BlockManager singleton;
    public final Texture blocks = new Texture(Gdx.files.internal("blocks/blocks.png"));
    private HashMap<Integer, BlockType> blockTypes;

    public BlockManager() {
        blocks.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        generateBlocks();
        singleton = this;
    }

    public static BlockManager getInstance() {
        return singleton;
    }

    private void generateBlocks() {

        blockTypes = new HashMap<>();
        for (int i = 0; i <= 97; i++) {
            BlockType block = new BlockType();
            block.texture = new TextureRegion(blocks, i * 16, 0, 16, 16);

            // 0-15 (air, arrows, dot, crown, bricks)
            switch (i) {
                case 0:
                    block.codename = "AIR";
                    block.tags = new String[]{"nothing", "null", "invisible", "background"};
                    break;
                case 1:
                    block.codename = "LEFT ARROW";
                    block.tags = new String[]{"arrow", "left"};
                    break;
                case 2:
                    block.codename = "UP ARROW";
                    block.tags = new String[]{"tag, test1, hey!"};
                    break;
                case 3:
                    block.codename = "RIGHT ARROW";
                    block.tags = new String[]{"tag, test1, hey!"};
                    break;
                case 4:
                    block.codename = "GRAVITY DOT";
                    block.tags = new String[]{"tag, test1, hey!"};
                    break;
                case 5:
                    block.codename = "CROWN";
                    block.tags = new String[]{"tag, test1, hey!"};
                    break;
                case 6:
                    block.codename = "RED KEY";
                    block.tags = new String[]{"tag, test1, hey!"};
                    break;
                case 7:
                    block.codename = "GREEN KEY";
                    block.tags = new String[]{"tag, test1, hey!"};
                    break;
                case 8:
                    block.codename = "BLUE KEY";
                    block.tags = new String[]{"tag, test1, hey!"};
                    break;
                case 9:
                    block.codename = "GRAY BRICK";
                    block.tags = new String[]{"tag, test1, hey!"};
                    break;
                case 10:
                    block.codename = "BLUE BRICK";
                    block.tags = new String[]{"tag, test1, hey!"};
                    break;
                case 11:
                    block.codename = "PINK BRICK";
                    block.tags = new String[]{"tag, test1, hey!"};
                    break;
                case 12:
                    block.codename = "RED BRICK";
                    block.tags = new String[]{"tag, test1, hey!"};
                    break;
                case 13:
                    block.codename = "YELLOW BRICK";
                    block.tags = new String[]{"tag, test1, hey!"};
                    break;
                case 14:
                    block.codename = "GREEN BRICK";
                    block.tags = new String[]{"tag, test1, hey!"};
                    break;
                case 15:
                    block.codename = "CYAN BRICK";
                    block.tags = new String[]{"tag, test1, hey!"};
            }
            blockTypes.put(i, block);
        }
    }

    public BlockType getBlockTypeByID(int id) {
        return blockTypes.get(id);
    }
}
