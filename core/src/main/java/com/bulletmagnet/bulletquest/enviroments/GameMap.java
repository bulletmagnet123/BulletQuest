package com.bulletmagnet.bulletquest.enviroments;

import com.bulletmagnet.bulletquest.entities.enemies.Skeleton;

import java.util.ArrayList;


public class GameMap {

    private int[][] spriteIds;

    // Each map keeps its own doors and enemies, so they're still there when you come back
    private final ArrayList<Door> doors = new ArrayList<>();
    private final ArrayList<Skeleton> skeletons = new ArrayList<>();

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

}
