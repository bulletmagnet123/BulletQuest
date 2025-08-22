package com.bulletmagnet.bulletquest.gamestates;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.MotionEvent;

import com.bulletmagnet.bulletquest.main.Game;
import com.bulletmagnet.bulletquest.main.MainActivity;
import com.bulletmagnet.bulletquest.entities.Character;
import com.bulletmagnet.bulletquest.entities.GameCharacters;
import com.bulletmagnet.bulletquest.entities.Player;
import com.bulletmagnet.bulletquest.entities.enemies.Skeleton;
import com.bulletmagnet.bulletquest.enviroments.MapManager;
import com.bulletmagnet.bulletquest.helpers.GameConstants;
import com.bulletmagnet.bulletquest.helpers.interfaces.GameStateInterface;

public class Playing extends BaseState implements GameStateInterface {

    private float PlayerX = MainActivity.GAME_WIDTH / 2f, PlayerY = MainActivity.GAME_HEIGHT / 2f;
    private float cameraX, cameraY;
    private Player player;
    private boolean movePlayer;
    private PointF lastTouchDiff;
    private int playerAntIndexY, playerFaceDir;
    private int aniTick;
    private MapManager mapManager;
    private Skeleton skeleton;
    private float xCenter = 250, yCenter = 800, radius = 150;
    private Paint circlePaint, JoystickPAINT;

    float xTouch, yTouch;

    boolean touchDown = false;

    public Playing(Game game) {
        super(game);

        circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(5);
        circlePaint.setColor(Color.RED);

        JoystickPAINT = new Paint();
        JoystickPAINT.setStyle(Paint.Style.FILL);
        JoystickPAINT.setColor(Color.RED);

        mapManager = new MapManager();
        player = new Player();
        skeleton = new Skeleton(new PointF(100, 100));

    }
    @Override
    public void update(double delta) {
        updatePlayerMove(delta);

        player.update(delta, movePlayer);

        skeleton.update(delta);
        mapManager.setCameraValues(cameraX, cameraY);


    }
    private void updatePlayerMove(double delta) {
        if (!movePlayer)
            return;

        float baseSpeed = (float) (delta * 300);
        float ratio = Math.abs(lastTouchDiff.y) / Math.abs(lastTouchDiff.x);
        double angle = Math.atan(ratio);

        float xSpeed = (float) Math.cos(angle);
        float ySpeed = (float) Math.sin(angle);

        if (xSpeed > ySpeed) {
            if (lastTouchDiff.x > 0) player.setFaceDir(GameConstants.FACE_DIR.RIGHT);
            else player.setFaceDir(GameConstants.FACE_DIR.LEFT);
        } else {
            if (lastTouchDiff.y > 0) player.setFaceDir(GameConstants.FACE_DIR.DOWN);
            else player.setFaceDir(GameConstants.FACE_DIR.UP);
        }

        if (lastTouchDiff.x < 0)
            xSpeed *= -1;
        if (lastTouchDiff.y < 0)
            ySpeed *= -1;

        int pWidth = GameConstants.Sprite.SIZE;
        int pHeight = GameConstants.Sprite.SIZE;

        if (xSpeed <= 0)
            pWidth = 0;
        if (ySpeed <= 0)
            pHeight = 0;


        float deltaX = xSpeed * baseSpeed * -1;
        float deltaY = ySpeed * baseSpeed * -1;

        if (mapManager.canMoveHere(player.getHitbox().left + cameraX * -1 + deltaX * -1 + pWidth, player.getHitbox().top + cameraY * -1 + deltaY * -1 + pHeight)) {
            cameraX += deltaX;
            cameraY += deltaY;
        }
    }

    private void drawCharacter(Canvas canvas, Character c) {
        canvas.drawBitmap(c.getGameCharType().getSprite
                (c.getAniIndex(), c.getFaceDir()), skeleton.getHitbox().left + cameraX, skeleton.getHitbox().top + cameraY, null);
    }

    private void drawPlayer(Canvas c) {
        c.drawBitmap(
                player.getGameCharType().getSprite(player.getAniIndex(), player.getFaceDir()),
                player.getHitbox().left,
                player.getHitbox().top,
                null);
    }

    @Override
    public void render(Canvas c) {
        mapManager.draw(c);
        drawUI(c);


        drawPlayer(c);
        drawCharacter(c, skeleton);

    }

    private void drawUI(Canvas c) {
        c.drawCircle(xCenter, yCenter, radius, circlePaint);
        if (touchDown){
            c.drawLine(xCenter, yCenter, xTouch, yTouch, JoystickPAINT);
        }
    }
    @Override
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
                } else
                    game.setCurrentGameState(Game.GameState.MENU);
            }

            case MotionEvent.ACTION_MOVE -> {
                if (touchDown) {
                    xTouch = event.getX();
                    yTouch = event.getY();

                    float xDiff = xTouch - xCenter;
                    float yDiff = yTouch - yCenter;

                    setPlayerMoveTrue(new PointF(xDiff, yDiff));

                }

            }
            case MotionEvent.ACTION_UP -> {
                touchDown = false;
                setPlayerMoveFalse();

            }
        }

    }
   
    public void setPlayerMoveTrue(PointF lastTouchDiff) {
        movePlayer = true;
        this.lastTouchDiff = lastTouchDiff;
    }

    public void setPlayerMoveFalse() {
        movePlayer = false;
        player.resetAnimation();
    }
}
