package com.costin.eeon.game.players;

import com.costin.eeon.game.world.BlockGroup;
import com.dongbat.jbump.CollisionFilter;
import com.dongbat.jbump.Response;

public class  CollFilter {
    private static CollFilter instance;
    public CollisionFilter testFilter = ((item, other) -> {
        if (other.userData instanceof BlockGroup.BaseBlock) {
            return Response.cross;
        } else {
            return null;
        }
    });
    public CollisionFilter blockFilter = (item, other) -> {
        if (other.userData instanceof BlockGroup.BaseBlock) {
            return Response.slide;
        } else {
            return null;
        }
    };

    public CollFilter() {
        instance = this;
    }

    public static CollFilter getInstance() {
        return instance;
    }
}
