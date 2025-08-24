package com.bulletmagnet.bulletquest.helpers.interfaces;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.bulletmagnet.bulletquest.helpers.GameConstants;

public interface BitmapMethods {
    BitmapFactory.Options options = new BitmapFactory.Options();

    default Bitmap getScaledBitmap(Bitmap bitmap){
        return Bitmap.createScaledBitmap(bitmap,bitmap.getWidth() * GameConstants.SPRITE.DEFAULT_SCALE,bitmap.getHeight()* GameConstants.SPRITE.DEFAULT_SCALE,false);
    }
}
