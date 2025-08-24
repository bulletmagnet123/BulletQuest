package com.bulletmagnet.bulletquest.mainui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

import com.bulletmagnet.bulletquest.R;
import com.bulletmagnet.bulletquest.helpers.interfaces.BitmapMethods;
import com.bulletmagnet.bulletquest.main.MainActivity;

public enum ButtonImages implements BitmapMethods {



    MENU_START(R.drawable.bluebuttons, 320,160),
    PLAYING_MENU(R.drawable.bluebuttons,320,160);

    private int width, height;
    private Bitmap normal, pushed;
    public static RectF ButtonHitbox;
    static RectF ButtonHitBox2;

    ButtonImages(int resID, int width, int height) {
        options.inScaled = false;
        this.width = width;
        this.height = height;

        Bitmap buttonAtlas = BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(), resID, options);
        normal = Bitmap.createBitmap(buttonAtlas,16 , 0, 16, 16);
        pushed = Bitmap.createBitmap(buttonAtlas, 16 * 11, 0, 16, 16);

    }

    public int getWidth() {
        return normal.getWidth();
    }

    public int getHeight() {
        return normal.getHeight();
    }

    public Bitmap getBtnImg(boolean isBtnPushed) {
        return isBtnPushed ? pushed : normal;
    }
    /*
   *//* MENU_START(R.drawable.bluebuttons,42, 0,42 * 11, 0,
            42, 42),MENU_EXIT(R.drawable.bluebuttons, 126,0,126 * 11, 0,42, 42);
*//*
   MENU_START(
           42, 0,    // Normal State: X, Y in bluebuttons.png
           42 * 11, 0
   ),
    MENU_EXIT(
            126 * 4, 0,   // Normal State for Exit
            126 * 4 * 11, 0
    );

    int singleButtonWidth = 42;
    int singleButtonHeight = 42;

    private Bitmap normalImage;
    private static final int SHARED_ATLAS_RES_ID = R.drawable.bluebuttons;
    private Bitmap pushedImage;
    Context context;

    private static final BitmapFactory.Options options;
    Bitmap buttonAtlas;

    static {
        options = new BitmapFactory.Options();
        options.inScaled = false;
        Context context = null;
        Bitmap buttonAtlas = BitmapFactory.decodeResource(context.getResources(), SHARED_ATLAS_RES_ID, options);

    }

    ButtonImages( int normalStateX, int normalStateY,
                  int pushedStateX, int pushedStateY
                  ) {


        if (context == null) {
            System.out.println("Context is null. Cannot load button images for: ");
            return;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap normal = Bitmap.createBitmap(buttonAtlas, normalStateX, normalStateY, singleButtonWidth, singleButtonHeight);
        Bitmap pushed = Bitmap.createBitmap(buttonAtlas, pushedStateX, pushedStateY, singleButtonWidth, singleButtonHeight);

    }

    public static void initialize(Context context) {
        Context appContext = null;
        if (context == null) {
            System.out.println("Context is null. Cannot initialize ButtonImages.");
            return;
        }
        appContext = context;


    }

    public static void loadAllImages() {
    }

    *//* ButtonImages( // Constructor no longer takes atlasResId as a parameter
                   int normalStateX, int normalStateY,
                   int pushedStateX, int pushedStateY,
                   int singleButtonWidth, int singleButtonHeight) {
         BitmapFactory.Options options = new BitmapFactory.Options();
         options.inScaled = false;
     }
 *//*
    public Bitmap getBtnImg(boolean isPushed) {
        if (isPushed) {
            // If pushedImage is null (e.g., failed to load), fallback to normalImage
            return (pushedImage != null) ? pushedImage : normalImage;
        } else {
            return normalImage;
        }
    }

    public int getWidth() {
        return buttonAtlas.getWidth();
    }

    public int getHeight() {
        return buttonAtlas.getHeight();
    }

    private boolean isValidCrop(Bitmap atlas, int x, int y, int width, int height) {
        if (atlas == null) return false;
        return x >= 0 && y >= 0 &&
                x + width <= atlas.getWidth() &&
                y + height <= atlas.getHeight();
    }

    private void logBoundsError(String stateName, String buttonName, Bitmap atlas, int x, int y, int width, int height) {
        System.out.println("Invalid bounds for " + stateName + " image of " + buttonName + ": ");
    }*/
}
