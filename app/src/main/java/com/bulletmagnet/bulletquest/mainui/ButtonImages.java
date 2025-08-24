package com.bulletmagnet.bulletquest.mainui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

import com.bulletmagnet.bulletquest.R;
import com.bulletmagnet.bulletquest.helpers.GameConstants;
import com.bulletmagnet.bulletquest.helpers.interfaces.BitmapMethods;
import com.bulletmagnet.bulletquest.main.MainActivity;

public enum ButtonImages implements BitmapMethods {



    MENU_START(R.drawable.bluebuttons, 320,160),
    PLAYING_MENU(R.drawable.bluebuttons,320,160);

    private int width, height;
    private Bitmap Playing_normal, Playing_pushed, Menu_normal, Menu_pushed;;
    public static RectF ButtonHitbox;
    static RectF ButtonHitBox2;


    ButtonImages(int resID, int width, int height) {
        options.inScaled = false;
        this.width = width;
        this.height = height;

        Bitmap buttonAtlas = BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(), resID, options);
        Playing_normal = Bitmap.createBitmap(buttonAtlas,16 , 0, 16, 16);
        Playing_pushed = Bitmap.createBitmap(buttonAtlas, 16 * 11, 0, 16, 16);

        Menu_normal = Bitmap.createBitmap(buttonAtlas,48 , 0, 16, 16);
        Menu_pushed = Bitmap.createBitmap(buttonAtlas, 208, 0, 16, 16);

    }

    public Bitmap getScaledBitmap(Bitmap bitmap) {
        int BUTTON_WIDTH = GameConstants.BUTTONS.BUTTON_SCALE;;
        int BUTTON_HEIGHT = GameConstants.BUTTONS.BUTTON_SCALE;
        return Bitmap.createScaledBitmap(bitmap, BUTTON_WIDTH, BUTTON_HEIGHT, false);
    }

    public int getWidthPlayingNormal() {
        return Playing_normal.getWidth();
    }

    public int getHeightPlayingNormal() {
        return Playing_normal.getHeight();
    }
    public int getWidthPlayingPushed() {
        return Playing_pushed.getWidth();
    }
    public int getHeightPlayingPushed() {
        return Playing_pushed.getHeight();
    }
    public int getWidthMenuNormal() {
        return Menu_normal.getWidth();
    }
    public int getHeightMenuNormal() {
        return Menu_normal.getHeight();
    }
    public int getWidthMenuPushed() {
        return Menu_pushed.getWidth();
    }
    public int getHeightMenuPushed() {
        return Menu_pushed.getHeight();
    }

    public Bitmap Menu_getBtnImg(boolean isBtnPushed) {
        return isBtnPushed ? Playing_pushed : Playing_normal;
    }

    public Bitmap Playing_getBtnImg(boolean isBtnPushed) {
        return isBtnPushed ? Menu_pushed : Menu_normal;
    }

}
