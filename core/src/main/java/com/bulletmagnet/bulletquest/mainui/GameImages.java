package com.bulletmagnet.bulletquest.mainui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.bulletmagnet.bulletquest.helpers.interfaces.BitmapMethods;

/** Single images (not sprite sheets), e.g. the door. */
public enum GameImages implements BitmapMethods {

    DOOR("door.png");

    private final String fileName;
    private Texture texture;
    private TextureRegion image;

    GameImages(String fileName) {
        this.fileName = fileName;
    }

    public TextureRegion getImage() {
        if (texture == null) {
            texture = new Texture(Gdx.files.internal(fileName));
            image = createRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
        }
        return image;
    }

    public static void disposeAll() {
        for (GameImages g : values()) {
            if (g.texture != null) {
                g.texture.dispose();
                g.texture = null;
                g.image = null;
            }
        }
    }
}
