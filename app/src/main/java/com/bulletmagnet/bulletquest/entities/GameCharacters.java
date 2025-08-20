package com.bulletmagnet.bulletquest.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;

import com.bulletmagnet.bulletquest.MainActivity;
import com.bulletmagnet.bulletquest.R;
import com.bulletmagnet.bulletquest.helpers.GameConstants;
import com.bulletmagnet.bulletquest.helpers.interfaces.BitmapMethods;

public enum GameCharacters implements BitmapMethods {

    PLAYER(R.drawable.playersheet), SKELETON(R.drawable.skeletonsheet);

    private Bitmap spriteSheet;
    private Bitmap[][] sprites = new Bitmap[7][4];
    private BitmapFactory.Options options = new BitmapFactory.Options();

    GameCharacters(int resID) {
        options.inScaled = false;
        spriteSheet = BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(), resID, options);
        for (int j = 0; j < sprites.length; j++)
            for (int i = 0; i < sprites[j].length; i++)
                sprites[j][i] = getScaledBitmap(Bitmap.createBitmap(spriteSheet, GameConstants.Sprite.DEFAULT_SIZE * i, GameConstants.Sprite.DEFAULT_SIZE * j, GameConstants.Sprite.DEFAULT_SIZE, GameConstants.Sprite.DEFAULT_SIZE));
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

    public Bitmap getSpriteSheet(){
        return spriteSheet;
    }

    public Bitmap getSprite(int yPos, int xPos) {
        return sprites[yPos][xPos];
    }


}
