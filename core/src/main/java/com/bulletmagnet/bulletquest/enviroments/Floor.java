package com.bulletmagnet.bulletquest.enviroments;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.bulletmagnet.bulletquest.helpers.GameConstants;
import com.bulletmagnet.bulletquest.helpers.interfaces.BitmapMethods;

public enum Floor implements BitmapMethods {
    OUTSIDE("floorsheet.png", 22,26);

    private final String fileName;
    private final int tilesInWidth, tilesInHeight;
    private Texture spriteSheet;
    private TextureRegion[] sprites;




    Floor(String fileName,int tilesInWidth, int tilesInHeight) {
        this.fileName = fileName;
        this.tilesInWidth = tilesInWidth;
        this.tilesInHeight = tilesInHeight;
    }

    private void load() {
        if (spriteSheet != null) return;
        sprites = new TextureRegion[tilesInHeight * tilesInWidth];

        spriteSheet = new Texture(Gdx.files.internal(fileName));
        for (int j = 0; j < tilesInHeight; j++)
            for (int i = 0; i < tilesInWidth; i++) {
                int index = j * tilesInWidth + i;
                sprites[index] = createRegion(spriteSheet, GameConstants.SPRITE.DEFAULT_SIZE * i, GameConstants.SPRITE.DEFAULT_SIZE * j, GameConstants.SPRITE.DEFAULT_SIZE, GameConstants.SPRITE.DEFAULT_SIZE);
        }
    }

    public TextureRegion getSprite(int id) {
        load();
        return sprites[id];
    }

    public static void disposeAll() {
        for (Floor f : values()) {
            if (f.spriteSheet != null) {
                f.spriteSheet.dispose();
                f.spriteSheet = null;
                f.sprites = null;
            }
        }
    }

}
