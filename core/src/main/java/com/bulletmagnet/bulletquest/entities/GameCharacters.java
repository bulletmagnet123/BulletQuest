package com.bulletmagnet.bulletquest.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.bulletmagnet.bulletquest.helpers.GameConstants;
import com.bulletmagnet.bulletquest.helpers.interfaces.BitmapMethods;

public enum GameCharacters implements BitmapMethods {

    PLAYER("playersheet.png"), SKELETON("skeletonsheet.png");

    private final String fileName;
    private Texture spriteSheet;
    private TextureRegion[][] sprites;


    GameCharacters(String fileName) {
        this.fileName = fileName;
    }

    // Textures are loaded on first use (and freed in disposeAll) so they are always
    // created while libGDX's OpenGL context is alive.
    private void load() {
        if (spriteSheet != null) return;
        spriteSheet = new Texture(Gdx.files.internal(fileName));
        sprites = new TextureRegion[7][4];
        for (int j = 0; j < sprites.length; j++)
            for (int i = 0; i < sprites[j].length; i++)
                sprites[j][i] = createRegion(spriteSheet, GameConstants.SPRITE.DEFAULT_SIZE * i, GameConstants.SPRITE.DEFAULT_SIZE * j, GameConstants.SPRITE.DEFAULT_SIZE, GameConstants.SPRITE.DEFAULT_SIZE);
    }

    public int getAnimationFrames(int direction) {
        // For simplicity, assuming 4 frames for all animations for now
        // You can make this more specific if needed
        switch (this) {
            case PLAYER:
                // if direction corresponds to a specific animation with different frames
                return 4; // Or return a specific value based on direction/animation type
            case SKELETON:
                return 4; // Or return a specific value based on direction/animation type
            default:
                return 1;
        }
    }

    public Texture getSpriteSheet(){
        load();
        return spriteSheet;
    }

    public TextureRegion getSprite(int yPos, int xPos) {
        load();
        return sprites[yPos][xPos];
    }

    public static void disposeAll() {
        for (GameCharacters c : values()) {
            if (c.spriteSheet != null) {
                c.spriteSheet.dispose();
                c.spriteSheet = null;
                c.sprites = null;
            }
        }
    }
}
