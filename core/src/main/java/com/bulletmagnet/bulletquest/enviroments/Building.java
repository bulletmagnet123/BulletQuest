package com.bulletmagnet.bulletquest.enviroments;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import com.bulletmagnet.bulletquest.helpers.GameConstants;

/**
 * One building placed in a map. The player can't walk through it (the collision
 * follows the building's actual pixels, not the whole image), and its door opens
 * when the player walks up to the front of it.
 */
public class Building {

    private static final float DOOR_SPEED = 15f; // frames per second

    private final Buildings type;
    private final Rectangle area = new Rectangle();       // where the image is drawn (map pixels)
    private final Rectangle solidArea = new Rectangle();  // box around the solid pixels (map pixels), quick check before the pixel check
    private final Rectangle frontArea = new Rectangle();  // strip in front of the building: standing here opens the door
    private boolean winter;
    private float doorFrame;            // 0 = closed, Buildings.FRAMES - 1 = open

    /** x, y = top-left corner in map pixels. Use GameMap.addBuilding(...) for tile positions. */
    public Building(Buildings type, float x, float y, boolean winter) {
        this.type = type;
        area.set(x, y, type.getWidth(), type.getHeight());
        setWinter(winter);
    }

    // The snowy version can have a slightly different outline, so recalculate the collision boxes
    private void updateCollision() {
        int scale = GameConstants.SPRITE.DEFAULT_SCALE;
        Rectangle b = type.getSolidBounds(winter);
        solidArea.set(area.x + b.x * scale, area.y + b.y * scale, b.width * scale, b.height * scale);
        frontArea.set(solidArea.x, solidArea.y + solidArea.height, solidArea.width, GameConstants.SPRITE.SIZESCALLED);
    }

    /** playerFeet = the player's feet in map pixels */
    public void update(double delta, Rectangle playerFeet) {
        float maxFrame = Buildings.FRAMES - 1;
        if (frontArea.overlaps(playerFeet) && !blocks(playerFeet))
            doorFrame = Math.min(maxFrame, doorFrame + (float) delta * DOOR_SPEED);
        else
            doorFrame = Math.max(0, doorFrame - (float) delta * DOOR_SPEED);
    }

    public void draw(SpriteBatch c, float cameraX, float cameraY) {
        c.draw(type.getFrame((int) doorFrame, winter), area.x + cameraX, area.y + cameraY, area.width, area.height);
    }

    /** True if r (map pixels) touches any solid pixel of the building. */
    public boolean blocks(Rectangle r) {
        if (!solidArea.overlaps(r))
            return false;

        // Which image pixels does r cover?
        int scale = GameConstants.SPRITE.DEFAULT_SCALE;
        int x0 = (int) Math.floor((Math.max(r.x, solidArea.x) - area.x) / scale);
        int y0 = (int) Math.floor((Math.max(r.y, solidArea.y) - area.y) / scale);
        int x1 = (int) Math.ceil((Math.min(r.x + r.width, solidArea.x + solidArea.width) - area.x) / scale) - 1;
        int y1 = (int) Math.ceil((Math.min(r.y + r.height, solidArea.y + solidArea.height) - area.y) / scale) - 1;

        for (int y = y0; y <= y1; y++)
            for (int x = x0; x <= x1; x++)
                if (type.isSolid(x, y, winter))
                    return true;
        return false;
    }

    public boolean isDoorOpen() {
        return doorFrame >= Buildings.FRAMES - 1;
    }

    public Buildings getType() {
        return type;
    }

    public Rectangle getArea() {
        return area;
    }

    public Rectangle getSolidArea() {
        return solidArea;
    }

    public boolean isWinter() {
        return winter;
    }

    public void setWinter(boolean winter) {
        this.winter = winter;
        updateCollision();
    }
}
