package com.bulletmagnet.bulletquest.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.bulletmagnet.bulletquest.R;
import com.bulletmagnet.bulletquest.helpers.interfaces.BitmapMethods;
import com.bulletmagnet.bulletquest.main.MainActivity;

public class Weapons implements BitmapMethods {

    public static final Weapons Sword = new Weapons(R.drawable.sword);

    BitmapFactory.Options options = new BitmapFactory.Options();

    Bitmap weaponImg;

    Weapons(int resid){
        options.inScaled = false;
        weaponImg = getScaledBitmap(BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(), resid, options));

    }

    public Bitmap getWeaponImg(){
        return weaponImg;
    }

    public void setWeaponImg(Bitmap weaponImg){
        this.weaponImg = weaponImg;
    }

    public int getWidth(){
        return weaponImg.getWidth();
    }

    public int getHeight(){
        return weaponImg.getHeight();
    }
}
