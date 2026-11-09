package com.bulletmagnet.bulletquest.enviroments;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import com.bulletmagnet.bulletquest.helpers.GameConstants;
import com.bulletmagnet.bulletquest.helpers.interfaces.BitmapMethods;

/**
 * All the building images in assets/Buildings.
 * Every building has 6 frames of its door opening (_1 = closed ... _6 = open)
 * and the same 6 frames with snow (_winter1 ... _winter6).
 *
 * To put one in a scene: map.addBuilding(Buildings.BUILDING_3, tileX, tileY);
 * (see MapManager.addBuildings), or add it in Tiled (see MapManager.loadBuildings).
 *
 * Sizes are in tiles (1 tile = 16px) at the game's scale.
 */
public enum Buildings implements BitmapMethods {

    BUILDING_1("building#1", "building#1"),  // small shop with awning, 5 x 8 tiles
    BUILDING_2("building#2", "buiding#2"),   // house with side wing, 8 x 6 tiles (file names are spelled "buiding")
    BUILDING_3("building#3", "building#3"),  // 11 x 6 tiles
    BUILDING_4("building#4", "building#4"),  // 10 x 7 tiles
    BUILDING_5("building#5", "building#5"),  // tall and thin, 4 x 10 tiles
    BUILDING_6("building#6", "building#6"),  // big red-roof hall, 13 x 9 tiles
    BUILDING_7("building#7", "building#7"),  // 5 x 6 tiles
    BUILDING_8("building#8", "building#8"),  // 6 x 6 tiles
    BUILDING_9("building#9", "building#9");  // 10 x 6 tiles

    public static final int FRAMES = 6;
    // Pixels at least this opaque count as wall. The soft shadow around the buildings is ~82, so it doesn't block.
    private static final int SOLID_ALPHA = 128;

    private final String folder, filePrefix;
    private Texture[] textures, winterTextures;
    private TextureRegion[] frames, winterFrames;
    // Collision shape: [y][x] per image pixel, true = solid (taken from the closed-door frame)
    private boolean[][] mask, winterMask;
    private Rectangle solidBounds, winterSolidBounds; // smallest box around the solid pixels (image pixels)

    Buildings(String folder, String filePrefix) {
        this.folder = folder;
        this.filePrefix = filePrefix;
    }

    private void load() {
        if (frames != null) return;
        textures = new Texture[FRAMES];
        winterTextures = new Texture[FRAMES];
        frames = new TextureRegion[FRAMES];
        winterFrames = new TextureRegion[FRAMES];
        for (int i = 0; i < FRAMES; i++) {
            String path = "Buildings/" + folder + "/" + filePrefix + "_";
            textures[i] = new Texture(Gdx.files.internal(path + (i + 1) + ".png"));
            winterTextures[i] = new Texture(Gdx.files.internal(path + "winter" + (i + 1) + ".png"));
            frames[i] = createRegion(textures[i], 0, 0, textures[i].getWidth(), textures[i].getHeight());
            winterFrames[i] = createRegion(winterTextures[i], 0, 0, winterTextures[i].getWidth(), winterTextures[i].getHeight());
        }
        String path = "Buildings/" + folder + "/" + filePrefix + "_";
        mask = loadMask(path + "1.png");
        winterMask = loadMask(path + "winter1.png");
        solidBounds = findBounds(mask);
        winterSolidBounds = findBounds(winterMask);
    }

    private static boolean[][] loadMask(String file) {
        Pixmap pixmap = new Pixmap(Gdx.files.internal(file));
        boolean[][] m = new boolean[pixmap.getHeight()][pixmap.getWidth()];
        for (int y = 0; y < m.length; y++)
            for (int x = 0; x < m[0].length; x++)
                m[y][x] = (pixmap.getPixel(x, y) & 0xff) >= SOLID_ALPHA; // getPixel is RGBA8888, alpha = lowest byte
        pixmap.dispose();
        return m;
    }

    private static Rectangle findBounds(boolean[][] m) {
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, maxX = -1, maxY = -1;
        for (int y = 0; y < m.length; y++)
            for (int x = 0; x < m[0].length; x++)
                if (m[y][x]) {
                    minX = Math.min(minX, x); maxX = Math.max(maxX, x);
                    minY = Math.min(minY, y); maxY = Math.max(maxY, y);
                }
        if (maxX < 0) return new Rectangle(0, 0, 0, 0);
        return new Rectangle(minX, minY, maxX - minX + 1, maxY - minY + 1);
    }

    /** True if this image pixel (not scaled) is part of the building. */
    public boolean isSolid(int pixelX, int pixelY, boolean winter) {
        load();
        boolean[][] m = winter ? winterMask : mask;
        if (pixelY < 0 || pixelY >= m.length || pixelX < 0 || pixelX >= m[0].length) return false;
        return m[pixelY][pixelX];
    }

    /** Smallest box around the solid pixels, in image pixels (not scaled). */
    public Rectangle getSolidBounds(boolean winter) {
        load();
        return winter ? winterSolidBounds : solidBounds;
    }

    /** frame 0 = door closed, FRAMES-1 = door open */
    public TextureRegion getFrame(int frame, boolean winter) {
        load();
        frame = Math.max(0, Math.min(FRAMES - 1, frame));
        return winter ? winterFrames[frame] : frames[frame];
    }

    /** Width in screen pixels (already scaled up like the tiles). */
    public int getWidth() {
        load();
        return textures[0].getWidth() * GameConstants.SPRITE.DEFAULT_SCALE;
    }

    /** Height in screen pixels (already scaled up like the tiles). */
    public int getHeight() {
        load();
        return textures[0].getHeight() * GameConstants.SPRITE.DEFAULT_SCALE;
    }

    /** Finds a building by "BUILDING_3", "building#3" or just "3" (used for Tiled maps). Returns null if unknown. */
    public static Buildings fromName(String name) {
        if (name == null) return null;
        String digits = name.replaceAll("[^0-9]", "");
        for (Buildings b : values()) {
            if (b.name().equalsIgnoreCase(name) || b.folder.equalsIgnoreCase(name))
                return b;
            if (!digits.isEmpty() && b.name().equals("BUILDING_" + digits))
                return b;
        }
        return null;
    }

    public static void disposeAll() {
        for (Buildings b : values()) {
            if (b.frames == null) continue;
            for (int i = 0; i < FRAMES; i++) {
                b.textures[i].dispose();
                b.winterTextures[i].dispose();
            }
            b.textures = b.winterTextures = null;
            b.frames = b.winterFrames = null;
            b.mask = b.winterMask = null;
            b.solidBounds = b.winterSolidBounds = null;
        }
    }
}
