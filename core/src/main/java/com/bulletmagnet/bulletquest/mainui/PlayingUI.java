package com.bulletmagnet.bulletquest.mainui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.bulletmagnet.bulletquest.gamestates.Playing;
import com.bulletmagnet.bulletquest.helpers.TouchEvent;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class PlayingUI {

    //For UI (libGDX has no Paint: a colour + a line width does the same job)
    private final Color circlePaint = new Color(Color.GRAY);
    private final float circleStrokeWidth = 5;
    private final Color JoystickPAINT = new Color(Color.BLUE);
    private final float joystickStrokeWidth = 15;
    private final Color attackButtonPaint = new Color(Color.RED);
    private final ImageButton btnMenu;

    // ShapeDrawer draws circles/lines with the same SpriteBatch as the sprites
    private final Texture whitePixel;
    private ShapeDrawer shapes;

    // Left Joystick (Movement)
    private Vector2 moveJoystickCenter;
    private final float moveJoystickRadius = 50;
    private Vector2 moveJoystickTouch;
    private int movePointerId = -1; // -1 means not active

    // Right Joystick (Attack)
    private Vector2 attackJoystickCenter;
    private float attackJoystickRadius = 50;
    private Vector2 attackJoystickTouch;
    private int attackPointerId = -1;

    private final Playing playing;

    public PlayingUI(Playing playing) {
        this.playing = playing;

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        whitePixel = new Texture(pixmap);
        pixmap.dispose();

        // Menu button (top-left) - goes back to the main menu when tapped
        btnMenu = ButtonImages.PLAYING_MENU.createButton();
        btnMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                PlayingUI.this.playing.setGameStateMenu();
            }
        });
        Table table = new Table();
        table.setFillParent(true);
        table.top().left().pad(5, 5, 0, 0);
        table.add(btnMenu);
        playing.getStage().addActor(table);
    }

    public void init(int screenWidth, int screenHeight) {
        // Position controls in a three-column layout at the bottom
        float yCenter = screenHeight - 200;
        moveJoystickCenter = new Vector2(screenWidth / 4f, yCenter);
        attackJoystickCenter = new Vector2(screenWidth * (3 / 4f), yCenter);
    }

    public void drawUI(SpriteBatch c) {
        if (shapes == null)
            shapes = new ShapeDrawer(c, new TextureRegion(whitePixel));

        // Draw Left Joystick
        if (moveJoystickCenter != null) {
            shapes.setColor(circlePaint);
            shapes.circle(moveJoystickCenter.x, moveJoystickCenter.y, moveJoystickRadius, circleStrokeWidth);
            if (movePointerId != -1 && moveJoystickTouch != null) {
                shapes.setColor(JoystickPAINT);
                shapes.line(moveJoystickCenter.x, moveJoystickCenter.y, moveJoystickTouch.x, moveJoystickTouch.y, joystickStrokeWidth);
            }
        }

        // Draw Right Joystick
        if (attackJoystickCenter != null) {
            shapes.setColor(circlePaint);
            shapes.circle(attackJoystickCenter.x, attackJoystickCenter.y, attackJoystickRadius, circleStrokeWidth);
            if (attackPointerId != -1 && attackJoystickTouch != null) {
                shapes.setColor(attackButtonPaint);
                shapes.filledCircle(attackJoystickTouch.x, attackJoystickTouch.y, 50);
            }
        }
        // The menu button is drawn by the Stage (see Game.render)
    }

    /** Lets go of both joysticks (used when leaving the Playing state mid-touch). */
    public void releaseJoysticks() {
        movePointerId = -1;
        moveJoystickTouch = null;
        attackPointerId = -1;
        attackJoystickTouch = null;
    }

    public void touchEvents(TouchEvent event) {
        int action = event.getAction();
        int pointerId = event.getPointerId();
        float x = event.getX();
        float y = event.getY();

        switch (action) {
            case TouchEvent.ACTION_DOWN:
                if (moveJoystickCenter != null && isIn(x, y, moveJoystickCenter, moveJoystickRadius) && movePointerId == -1) {
                    movePointerId = pointerId;
                    moveJoystickTouch = new Vector2(x, y);
                } else if (attackJoystickCenter != null && isIn(x, y, attackJoystickCenter, attackJoystickRadius) && attackPointerId == -1) {
                    attackPointerId = pointerId;
                    attackJoystickTouch = new Vector2(x, y);
                    // Optional: Trigger an immediate attack on touch down
                    // playing.attack();
                }
                break;

            case TouchEvent.ACTION_MOVE:
                // libGDX sends one move event per finger, so no loop over pointers is needed
                if (pointerId == movePointerId && moveJoystickCenter != null) {
                    moveJoystickTouch.set(x, y);
                    float xDiff = x - moveJoystickCenter.x;
                    float yDiff = y - moveJoystickCenter.y;
                    playing.setPlayerMoveTrue(new Vector2(xDiff, yDiff));
                } else if (pointerId == attackPointerId && attackJoystickCenter != null) {
                    attackJoystickTouch.set(x, y);
                    // You can add logic here if the attack joystick's movement has meaning
                }
                break;

            case TouchEvent.ACTION_UP:
                if (pointerId == movePointerId) {
                    movePointerId = -1;
                    moveJoystickTouch = null;
                    playing.setPlayerMoveTrue(new Vector2(0, 0)); // Stop player movement
                } else if (pointerId == attackPointerId) {
                    attackPointerId = -1;
                    attackJoystickTouch = null;
                    // Optional: Trigger attack on release
                    playing.playerAttack();
                }
                break;
        }
    }

    private boolean isIn(float x, float y, Vector2 center, float radius) {
        float a = Math.abs(x - center.x);
        float b = Math.abs(y - center.y);
        return Math.hypot(a, b) <= radius;
    }

    public void dispose() {
        whitePixel.dispose();
    }
}
