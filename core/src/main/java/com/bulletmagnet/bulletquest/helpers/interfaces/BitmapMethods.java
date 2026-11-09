package com.bulletmagnet.bulletquest.helpers.interfaces;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.bulletmagnet.bulletquest.helpers.GameConstants;

public interface BitmapMethods {

    /**
     * libGDX version of Bitmap.createBitmap(sheet, x, y, w, h): cuts one sprite out of a sheet.
     * It's flipped vertically because the game camera is y-down (like Android's Canvas).
     */
    default TextureRegion createRegion(Texture sheet, int x, int y, int width, int height) {
        TextureRegion region = new TextureRegion(sheet, x, y, width, height);
        region.flip(false, true);
        return region;
    }

    /**
     * Replaces getScaledBitmap(): instead of making a bigger copy of the image,
     * libGDX just draws it DEFAULT_SCALE times bigger.
     */
    static void drawScaled(SpriteBatch batch, TextureRegion region, float x, float y) {
        batch.draw(region, x, y,
                region.getRegionWidth() * GameConstants.SPRITE.DEFAULT_SCALE,
                region.getRegionHeight() * GameConstants.SPRITE.DEFAULT_SCALE);
    }
}
