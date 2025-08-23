package com.bulletmagnet.bulletquest.gamestates;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.widget.Button;

import androidx.media3.common.util.Log;

import com.bulletmagnet.bulletquest.enviroments.MapManager;
import com.bulletmagnet.bulletquest.helpers.interfaces.GameStateInterface;
import com.bulletmagnet.bulletquest.main.Game;
import com.bulletmagnet.bulletquest.main.MainActivity;
import com.bulletmagnet.bulletquest.mainui.ButtonImages;
import com.bulletmagnet.bulletquest.mainui.CustomButton;
import com.bulletmagnet.bulletquest.mainui.GameImages;

import java.io.Console;

public class Menu extends BaseState implements GameStateInterface {

    private Paint paint;
    private Game game;

    private CustomButton btnStart;
    private int menuX = MainActivity.GAME_WIDTH / 6;
    private int menuY = 200;
    private int btnStartX = menuX + MainActivity.GAME_WIDTH / 2 - ButtonImages.MENU_START.getWidth() / 2;
    private int btnStartY = menuY + 100;


    public Menu(Game game) {
        super(game);
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(60);
        paint.setTextAlign(Paint.Align.CENTER);
        this.game = game;
        btnStartX = 100;
        btnStartY = 100;
        if (ButtonImages.MENU_START.getWidth() > 0 && ButtonImages.MENU_START.getHeight() > 0) {
            btnStart = new CustomButton(btnStartX, btnStartY,
                    ButtonImages.MENU_START.getWidth(),
                    ButtonImages.MENU_START.getHeight());
        } else {
            // Fallback or error if button dimensions aren't available (e.g., images didn't load)
            System.out.println("ButtonImages.MENU_START dimensions are invalid. Using default.");
            btnStart = new CustomButton(btnStartX, btnStartY, 200, 100); // Default fallback size
        }
    }
    @Override
    public void update(double delta) {

    }

    @Override
    public void render(Canvas c) {
        if (c == null) return;

        // Draw background (optional, example)
        // c.drawColor(Color.DKGRAY);

        c.drawText("MENU! PRESS START!", c.getWidth() / 2f, 150, paint); // Centered text example

        // Draw the start button
        if (btnStart != null && btnStart.getHitbox() != null) {
            Bitmap buttonBitmap = ButtonImages.MENU_START.getBtnImg(btnStart.isPressed());
            if (buttonBitmap != null) {
                c.drawBitmap(
                        buttonBitmap,
                        btnStart.getHitbox().left,
                        btnStart.getHitbox().top,
                        null); // Using null for Paint is fine for direct bitmap drawing
            } else {
                Log.w("MenuState", "Button bitmap for MENU_START is null.");
            }
        }
    }

    @Override
    public void touchEvents(MotionEvent event) {
        if (btnStart == null) return; // Safety check

        int action = event.getActionMasked(); // Use getActionMasked for pointer events
        float touchX = event.getX();
        float touchY = event.getY();

        boolean isInsideStartButton = btnStart.getHitbox().contains(touchX, touchY);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: // Handle multi-touch down if necessary
                if (isInsideStartButton) {
                    btnStart.setPressed(true);
                    // Consider only setting pressed if it's the first pointer down on the button
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (btnStart.isPressed()) { // Only care if the button was initially pressed
                    if (!isInsideStartButton) {
                        // Finger dragged outside while pressed
                        btnStart.setPressed(false);
                    }
                    // Optional: If you want it to re-press if dragged back inside:
                    // else { btnStart.setPressed(true); }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP: // Handle multi-touch up
                // Check if the button was in a pressed state AND the touch release is inside
                if (btnStart.isPressed() && isInside(event, btnStart)) {
                    System.out.println("Start Button Clicked!");
                    game.setCurrentGameState(Game.GameState.PLAYING);
                    // Play sound, etc.
                }
                btnStart.setPressed(false); // Always reset pressed state on UP, regardless of where
                break;
            case MotionEvent.ACTION_CANCEL:
                btnStart.setPressed(false); // Reset on cancel as well
                break;
        }
    }



    public boolean isInside(MotionEvent event, CustomButton button){
        return button.getHitbox().contains(event.getX(), event.getY());

    }


}
