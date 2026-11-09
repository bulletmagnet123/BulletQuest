package com.bulletmagnet.bulletquest.enviroments;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import com.bulletmagnet.bulletquest.helpers.GameConstants;

/**
 * A door that takes the player to another map.
 * Doors come from an object layer called "doors" in the Tiled map, with these custom properties:
 *   targetMap     - map file to go to (e.g. "TownOne.json")
 *   targetDoor    - name of the door in that map where the player comes out
 *   exitDirection - "up", "down", "left" or "right": which side of the door the player appears on
 */
public class Door {

    private final String name;
    private final Rectangle area; // in world pixels (already scaled)
    private final String targetMap;
    private final String targetDoor;
    private final String exitDirection;

    public Door(String name, Rectangle area, String targetMap, String targetDoor, String exitDirection) {
        this.name = name;
        this.area = area;
        this.targetMap = targetMap;
        this.targetDoor = targetDoor;
        this.exitDirection = exitDirection;
    }

    public boolean contains(float x, float y) {
        return area.contains(x, y);
    }

    /** Top-left world position for the player when they come out of this door (one tile beside it). */
    public Vector2 getSpawnPosition() {
        float tile = GameConstants.SPRITE.SIZESCALLED;
        switch (exitDirection) {
            case "up":
                return new Vector2(area.x, area.y - tile);
            case "left":
                return new Vector2(area.x - tile, area.y);
            case "right":
                return new Vector2(area.x + area.width, area.y);
            case "down":
            default:
                return new Vector2(area.x, area.y + area.height);
        }
    }

    public String getName() {
        return name;
    }

    public Rectangle getArea() {
        return area;
    }

    public String getTargetMap() {
        return targetMap;
    }

    public String getTargetDoor() {
        return targetDoor;
    }
}
