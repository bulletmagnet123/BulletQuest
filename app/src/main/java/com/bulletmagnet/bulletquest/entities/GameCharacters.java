package com.bulletmagnet.bulletquest.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.bulletmagnet.bulletquest.MainActivity;
import com.bulletmagnet.bulletquest.R;

public enum GameCharacters {

    PLAYER(R.drawable.playersheet);

    private Bitmap spriteSheet;
    GameCharacters(int resID) {
        spriteSheet = BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(), resID);
    }

    public Bitmap getSpriteSheet(){
        return spriteSheet;
    }
}
