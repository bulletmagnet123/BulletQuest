package com.bulletmagnet.bulletquest.enviroments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.bulletmagnet.bulletquest.main.MainActivity;
import com.bulletmagnet.bulletquest.R;
import com.bulletmagnet.bulletquest.helpers.GameConstants;
import com.bulletmagnet.bulletquest.helpers.interfaces.BitmapMethods;

public enum Floor implements BitmapMethods {
    OUTSIDE(R.drawable.floorsheet, 22,26);

    private Bitmap[] sprites;




    Floor(int resID,int tilesInWidth, int tilesInHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        sprites = new Bitmap[tilesInHeight * tilesInWidth];

        Bitmap spriteSheet = BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(), resID, options);
        spriteSheet = BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(), resID, options);
        for (int j = 0; j < tilesInHeight; j++)
            for (int i = 0; i < tilesInWidth; i++) {
                int index = j * tilesInWidth + i;
                sprites[index] = getScaledBitmap(Bitmap.createBitmap(spriteSheet, GameConstants.SPRITE.DEFAULT_SIZE * i, GameConstants.SPRITE.DEFAULT_SIZE * j, GameConstants.SPRITE.DEFAULT_SIZE, GameConstants.SPRITE.DEFAULT_SIZE));
        }
    }
    public Bitmap getSprite(int id) {
        return sprites[id];
    }

}
