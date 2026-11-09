package com.bulletmagnet.bulletquest.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.bulletmagnet.bulletquest.helpers.GameConstants;
import com.bulletmagnet.bulletquest.helpers.interfaces.BitmapMethods;

public class Weapons implements BitmapMethods {

    public static final Weapons Sword = new Weapons("sword.png");

    private final String fileName;
    private Texture texture;

    TextureRegion weaponImg;

    Weapons(String fileName){
        this.fileName = fileName;
    }

    private void load() {
        if (texture != null) return;
        texture = new Texture(Gdx.files.internal(fileName));
        weaponImg = createRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
    }

    public TextureRegion getWeaponImg(){
        load();
        return weaponImg;
    }

    public void setWeaponImg(TextureRegion weaponImg){
        load();
        this.weaponImg = weaponImg;
    }

    // Same values as before: the image size multiplied by DEFAULT_SCALE (draw it with BitmapMethods.drawScaled)
    public int getWidth(){
        return getWeaponImg().getRegionWidth() * GameConstants.SPRITE.DEFAULT_SCALE;
    }

    public int getHeight(){
        return getWeaponImg().getRegionHeight() * GameConstants.SPRITE.DEFAULT_SCALE;
    }

    public static void disposeAll() {
        if (Sword.texture != null) {
            Sword.texture.dispose();
            Sword.texture = null;
            Sword.weaponImg = null;
        }
    }
}
