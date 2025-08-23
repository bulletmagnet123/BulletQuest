package com.bulletmagnet.bulletquest.mainui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.bulletmagnet.bulletquest.R;
import com.bulletmagnet.bulletquest.helpers.interfaces.BitmapMethods;
import com.bulletmagnet.bulletquest.main.MainActivity;

public enum ButtonImages implements BitmapMethods {

    MENU_START(R.drawable.bluebuttons, // Atlas Resource ID
            42, 0,             // Normal State: X, Y in atlas
            42 * 11, 0,            // Pushed State: X, Y in atlas
            42, 42                        // Single Button Graphic: Width, Height
    );

    private final int buttonGraphicWidth;
    private final int buttonGraphicHeight;

    private Bitmap normalImage;
    private Bitmap pushedImage;


    private static final BitmapFactory.Options options;

    static {
        options = new BitmapFactory.Options();
        options.inScaled = false;
    }

    ButtonImages(int atlasResId,
                 int normalStateX, int normalStateY,
                 int pushedStateX, int pushedStateY,
                 int singleButtonWidth, int singleButtonHeight) {

        this.buttonGraphicWidth = singleButtonWidth;
        this.buttonGraphicHeight = singleButtonHeight;

        Context context = MainActivity.getGameContext();
        if (context == null) {
            System.out.println("Context is null. Cannot load button images for: ");
            return;
        }

        Bitmap buttonAtlas = null;
        try {
            buttonAtlas = BitmapFactory.decodeResource(context.getResources(), atlasResId, BitmapMethods.options);
        } catch (Exception e) {
            System.out.println("Error loading buttonAtlas (resID: " + atlasResId + ") for: " + this.name());
        }

        if (buttonAtlas == null) {
            System.out.println("Failed to load buttonAtlas (resID: " + atlasResId + ") for: " + this.name());
            return;
        }

        // Load Normal Image
        try {
            if (isValidCrop(buttonAtlas, normalStateX, normalStateY, singleButtonWidth, singleButtonHeight)) {
                normalImage = Bitmap.createBitmap(buttonAtlas, normalStateX, normalStateY, singleButtonWidth, singleButtonHeight);
            } else {
                logBoundsError("Normal", this.name(), buttonAtlas, normalStateX, normalStateY, singleButtonWidth, singleButtonHeight);
            }
        } catch (Exception e) {
            System.out.println("Error creating normalImage for ");
        }

        // Load Pushed Image
        try {
            if (isValidCrop(buttonAtlas, pushedStateX, pushedStateY, singleButtonWidth, singleButtonHeight)) {
                pushedImage = Bitmap.createBitmap(buttonAtlas, pushedStateX, pushedStateY, singleButtonWidth, singleButtonHeight);
            } else {
                logBoundsError("Pushed", this.name(), buttonAtlas, pushedStateX, pushedStateY, singleButtonWidth, singleButtonHeight);
                // Fallback: If pushed image is out of bounds or fails, use normal image for pushed state
                if (normalImage != null) {
                    System.out.println("Falling back to normal image for pushed state of ");
                    pushedImage = normalImage;
                }
            }
        } catch (Exception e) {
            System.out.println("Error creating pushedImage for ");
            if (normalImage != null && pushedImage == null) {
                System.out.println("Falling back to normal image for pushed state of " + this.name() + " due to exception.");
                pushedImage = normalImage;
            }
        }
        // Consider not recycling the atlas here if multiple enum constants might share it.
        // Or if you only have one enum constant using one atlas, it might be okay.
        // if (buttonAtlas != null && !buttonAtlas.isRecycled()) {
        // buttonAtlas.recycle();
        // }
    }

    public Bitmap getBtnImg(boolean isPushed) {
        if (isPushed) {
            // If pushedImage is null (e.g., failed to load), fallback to normalImage
            return (pushedImage != null) ? pushedImage : normalImage;
        } else {
            return normalImage;
        }
    }

    public int getWidth() {
        return buttonGraphicWidth;
    }

    public int getHeight() {
        return buttonGraphicHeight;
    }

    private boolean isValidCrop(Bitmap atlas, int x, int y, int width, int height) {
        if (atlas == null) return false;
        return x >= 0 && y >= 0 &&
                x + width <= atlas.getWidth() &&
                y + height <= atlas.getHeight();
    }

    private void logBoundsError(String stateName, String buttonName, Bitmap atlas, int x, int y, int width, int height) {
        System.out.println("Invalid bounds for " + stateName + " image of " + buttonName + ": ");
    }



}
