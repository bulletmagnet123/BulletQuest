package com.bulletmagnet.bulletquest.enviroments;

import com.badlogic.gdx.math.Rectangle;
import com.bulletmagnet.bulletquest.entities.enemies.Skeleton;
import com.bulletmagnet.bulletquest.helpers.GameConstants;

import java.util.ArrayList;


public class GameMap {

    private int[][] spriteIds;

    // Each map keeps its own doors and enemies, so they're still there when you come back
    private final ArrayList<Door> doors = new ArrayList<>();
    private final ArrayList<Skeleton> skeletons = new ArrayList<>();
    private final ArrayList<Building> buildings = new ArrayList<>();

    public GameMap(int[][] spriteIds) {
        this.spriteIds = spriteIds;
    }



    public int getSpriteID(int xIndex, int yIndex){
        return spriteIds[yIndex][xIndex];
    }
    public int getArrayWidth(){
        return spriteIds[0].length;
    }
    public int getArrayHeight(){
        return spriteIds.length;
    }

    public ArrayList<Door> getDoors() {
        return doors;
    }

    public Door getDoor(String name) {
        for (Door d : doors)
            if (d.getName().equals(name))
                return d;
        return null;
    }

    public ArrayList<Skeleton> getSkeletons() {
        return skeletons;
    }

    public ArrayList<Building> getBuildings() {
        return buildings;
    }

    /** Puts a building in this map. tileX/tileY = its top-left tile. */
    public Building addBuilding(Buildings type, int tileX, int tileY) {
        return addBuilding(type, tileX, tileY, false);
    }

    /** Same, but winter = true uses the snowy version. */
    public Building addBuilding(Buildings type, int tileX, int tileY, boolean winter) {
        int tile = GameConstants.SPRITE.SIZESCALLED;
        Building b = new Building(type, tileX * tile, tileY * tile, winter);
        buildings.add(b);
        return b;
    }

    /** True if this rectangle (map pixels) overlaps any building. */
    public boolean isBlockedByBuilding(Rectangle r) {
        for (Building b : buildings)
            if (b.blocks(r))
                return true;
        return false;
    }

}
