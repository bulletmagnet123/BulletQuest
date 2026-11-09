package com.bulletmagnet.bulletquest.mainui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import com.bulletmagnet.bulletquest.helpers.GameConstants;

/**
 * Button pictures from bluebuttons.png. Each button is 16x16; the pushed version
 * is in the right half of the sheet (160px further right).
 * createButton() turns them into a scene2d ImageButton.
 */
public enum ButtonImages {

    MENU_START("bluebuttons.png", 16, 16 * 11),   // "play" button on the main menu
    PLAYING_MENU("bluebuttons.png", 48, 208);     // "back to menu" button while playing

    private final String fileName;
    private final int normalX, pushedX;
    private Texture buttonAtlas;
    private TextureRegion normal, pushed;


    ButtonImages(String fileName, int normalX, int pushedX) {
        this.fileName = fileName;
        this.normalX = normalX;
        this.pushedX = pushedX;
    }

    private void load() {
        if (buttonAtlas != null) return;
        buttonAtlas = new Texture(Gdx.files.internal(fileName));
        // Not flipped: scene2d uses the normal libGDX y-up coordinates
        normal = new TextureRegion(buttonAtlas, normalX, 0, 16, 16);
        pushed = new TextureRegion(buttonAtlas, pushedX, 0, 16, 16);
    }

    /** Makes a new ImageButton that shows the normal image, and the pushed image while pressed. */
    public ImageButton createButton() {
        load();
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = scaled(normal);
        style.imageDown = scaled(pushed);
        return new ImageButton(style);
    }

    private TextureRegionDrawable scaled(TextureRegion region) {
        TextureRegionDrawable drawable = new TextureRegionDrawable(region);
        drawable.setMinSize(GameConstants.BUTTONS.BUTTON_WIDTH, GameConstants.BUTTONS.BUTTON_HEIGHT);
        return drawable;
    }

    public static void disposeAll() {
        for (ButtonImages b : values()) {
            if (b.buttonAtlas != null) {
                b.buttonAtlas.dispose();
                b.buttonAtlas = null;
                b.normal = null;
                b.pushed = null;
            }
        }
    }

}
