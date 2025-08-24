package com.bulletmagnet.bulletquest.mainui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.bulletmagnet.bulletquest.gamestates.Playing;

public class PlayingUI {

    //For UI
    private float xCenter = 250, yCenter = 800, radius = 150;
    private Paint circlePaint;
    private float xTouch, yTouch;
    private boolean touchDown;

    private CustomButton BtnExitToMenu;

    private final Playing playing;

    int buttonX, buttonY;

    Paint JoystickPAINT;

    RectF ButtonHitbox;

    private CustomButton btnMenu;


    public PlayingUI(Playing playing) {
        this.playing = playing;

        circlePaint = new Paint();
        circlePaint.setColor(Color.RED);
        circlePaint.setStrokeWidth(5);
        circlePaint.setStyle(Paint.Style.STROKE);
        JoystickPAINT = new Paint();
        JoystickPAINT.setColor(Color.BLUE);
        JoystickPAINT.setStrokeWidth(15);
        JoystickPAINT.setStyle(Paint.Style.FILL);

        btnMenu = new CustomButton(5, 5, ButtonImages.PLAYING_MENU.getWidthPlayingNormal(), ButtonImages.PLAYING_MENU.getHeightPlayingNormal());
        ButtonHitbox = new RectF(btnMenu.getHitbox());
        btnMenu.setPushed(false);
    }


    public void drawUI(Canvas c) {
        c.drawCircle(xCenter, yCenter, radius, circlePaint);
        if (touchDown) {
            c.drawLine(xCenter, yCenter, xTouch, yTouch, JoystickPAINT);
        }
    }

    public void touchEvents(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN -> {


                float x = event.getX();
                float y = event.getY();

                float a = Math.abs(x - xCenter);
                float b = Math.abs(y - yCenter);
                float c = (float) Math.hypot(a, b);

                if (c <= radius) {
                    touchDown = true;
                    xTouch = x;
                    yTouch = y;

                }

            }

            case MotionEvent.ACTION_MOVE -> {
                if (touchDown) {
                    xTouch = event.getX();
                    yTouch = event.getY();

                    float xDiff = xTouch - xCenter;
                    float yDiff = yTouch - yCenter;

                    playing.setPlayerMoveTrue(new PointF(xDiff, yDiff));

                }

            }
            case MotionEvent.ACTION_UP -> {
                touchDown = false;
            }
        }
    }

    public boolean isInside(MotionEvent event, RectF ButtonHitbox){
        return ButtonHitbox.contains(event.getX(), event.getY());
    }
}
