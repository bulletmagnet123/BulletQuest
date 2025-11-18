package com.bulletmagnet.bulletquest.mainui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.MotionEvent;
import com.bulletmagnet.bulletquest.gamestates.Playing;

public class PlayingUI {

    //For UI
    private final Paint circlePaint;
    private final Paint JoystickPAINT;
    private final Paint attackButtonPaint;
    private final CustomButton btnMenu;

    // Left Joystick (Movement)
    private PointF moveJoystickCenter;
    private final float moveJoystickRadius = 150;
    private PointF moveJoystickTouch;
    private int movePointerId = -1; // -1 means not active

    // Right Joystick (Attack)
    private PointF attackJoystickCenter;
    private float attackJoystickRadius = 150;
    private PointF attackJoystickTouch;
    private int attackPointerId = -1;

    private final Playing playing;

    public PlayingUI(Playing playing) {
        this.playing = playing;

        circlePaint = new Paint();
        circlePaint.setColor(Color.GRAY);
        circlePaint.setStrokeWidth(5);
        circlePaint.setStyle(Paint.Style.STROKE);

        JoystickPAINT = new Paint();
        JoystickPAINT.setColor(Color.BLUE);
        JoystickPAINT.setStrokeWidth(15);
        JoystickPAINT.setStyle(Paint.Style.FILL);

        attackButtonPaint = new Paint();
        attackButtonPaint.setColor(Color.RED);
        attackButtonPaint.setStyle(Paint.Style.FILL);

        btnMenu = new CustomButton(5, 5, ButtonImages.PLAYING_MENU.getWidthPlayingNormal(), ButtonImages.PLAYING_MENU.getHeightPlayingNormal());
        btnMenu.setPushed(false);
    }

    public void init(int screenWidth, int screenHeight) {
        // Position controls in a three-column layout at the bottom
        float yCenter = screenHeight - 200;
        moveJoystickCenter = new PointF(screenWidth / 4f, yCenter);
        attackJoystickCenter = new PointF(screenWidth * (3 / 4f), yCenter);
    }

    public void drawUI(Canvas c) {
        // Draw Left Joystick
        if (moveJoystickCenter != null) {
            c.drawCircle(moveJoystickCenter.x, moveJoystickCenter.y, moveJoystickRadius, circlePaint);
            if (movePointerId != -1 && moveJoystickTouch != null) {
                c.drawLine(moveJoystickCenter.x, moveJoystickCenter.y, moveJoystickTouch.x, moveJoystickTouch.y, JoystickPAINT);
            }
        }

        // Draw Right Joystick
        if (attackJoystickCenter != null) {
            c.drawCircle(attackJoystickCenter.x, attackJoystickCenter.y, attackJoystickRadius, circlePaint);
            if (attackPointerId != -1 && attackJoystickTouch != null) {
                c.drawCircle(attackJoystickTouch.x, attackJoystickTouch.y, 50, attackButtonPaint);
            }
        }

        c.drawBitmap(
                ButtonImages.PLAYING_MENU.getScaledBitmap(ButtonImages.PLAYING_MENU.Menu_getBtnImg(btnMenu.isPushed())
                ), btnMenu.getScaledHitbox().left, btnMenu.getScaledHitbox().top, null);
    }

    public void touchEvents(MotionEvent event) {
        int action = event.getActionMasked();
        int actionIndex = event.getActionIndex();
        int pointerId = event.getPointerId(actionIndex);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                float x = event.getX(actionIndex);
                float y = event.getY(actionIndex);

                if (moveJoystickCenter != null && isIn(x, y, moveJoystickCenter, moveJoystickRadius) && movePointerId == -1) {
                    movePointerId = pointerId;
                    moveJoystickTouch = new PointF(x, y);
                } else if (attackJoystickCenter != null && isIn(x, y, attackJoystickCenter, attackJoystickRadius) && attackPointerId == -1) {
                    attackPointerId = pointerId;
                    attackJoystickTouch = new PointF(x, y);
                    // Optional: Trigger an immediate attack on touch down
                    // playing.attack();
                }
                break;

            case MotionEvent.ACTION_MOVE:
                for (int i = 0; i < event.getPointerCount(); i++) {
                    int currentPointerId = event.getPointerId(i);
                    float moveX = event.getX(i);
                    float moveY = event.getY(i);

                    if (currentPointerId == movePointerId && moveJoystickCenter != null) {
                        moveJoystickTouch.set(moveX, moveY);
                        float xDiff = moveX - moveJoystickCenter.x;
                        float yDiff = moveY - moveJoystickCenter.y;
                        playing.setPlayerMoveTrue(new PointF(xDiff, yDiff));
                    } else if (currentPointerId == attackPointerId && attackJoystickCenter != null) {
                        attackJoystickTouch.set(moveX, moveY);
                        // You can add logic here if the attack joystick's movement has meaning
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (pointerId == movePointerId) {
                    movePointerId = -1;
                    moveJoystickTouch = null;
                    playing.setPlayerMoveTrue(new PointF(0, 0)); // Stop player movement
                } else if (pointerId == attackPointerId) {
                    attackPointerId = -1;
                    attackJoystickTouch = null;
                    // Optional: Trigger attack on release
                    playing.playerAttack();
                }
                break;
        }
    }

    private boolean isIn(float x, float y, PointF center, float radius) {
        float a = Math.abs(x - center.x);
        float b = Math.abs(y - center.y);
        return Math.hypot(a, b) <= radius;
    }
}