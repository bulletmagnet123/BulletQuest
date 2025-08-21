package com.bulletmagnet.bulletquest;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import androidx.annotation.NonNull;
import com.bulletmagnet.bulletquest.entities.Character;
import com.bulletmagnet.bulletquest.entities.GameCharacters;
import com.bulletmagnet.bulletquest.entities.Player;
import com.bulletmagnet.bulletquest.entities.enemies.Skeleton;
import com.bulletmagnet.bulletquest.enviroments.MapManager;
import com.bulletmagnet.bulletquest.helpers.GameConstants;
import com.bulletmagnet.bulletquest.inputs.TouchEvents;


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder holder;
    private float PlayerX = MainActivity.GAME_WIDTH / 2f, PlayerY = MainActivity.GAME_HEIGHT / 2f;
    private float cameraX, cameraY;
    private Player player;
    private boolean movePlayer;
    private PointF lastTouchDiff;
    private TouchEvents touchEvents;
    private GameLoop gameLoop;
    private int playerAntIndexY, playerFaceDir;
    private int aniTick;
    private MapManager mapManager;
    private Skeleton skeleton;


    public GamePanel(Context context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
        touchEvents = new TouchEvents(this);
        gameLoop = new GameLoop(this);
        mapManager = new MapManager();
        player = new Player();
        skeleton = new Skeleton(new PointF(100, 100));
    }


    public void update(double delta) {
        updatePlayerMove(delta);
        player.update(delta, movePlayer);
        updateAnimation();
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

    private void updateAnimation() {
        aniTick++; // Increment the animation tick regardless of player movement

        if (aniTick >= GameConstants.Animation.ANIM_DEFAULT_SPEED) {
            aniTick = 0;

            if (movePlayer) {
                playerAntIndexY++;
                if (playerAntIndexY >= GameCharacters.PLAYER.getAnimationFrames(playerFaceDir)) { // Use frame count from GameCharacters
                    playerAntIndexY = 0;
                }
            }
        }
    }


    public void render() {
        Canvas c = holder.lockCanvas();
        c.drawColor(Color.BLACK);
        mapManager.draw(c);
        touchEvents.draw(c);


        drawPlayer(c);
        drawCharacter(c, skeleton);



        holder.unlockCanvasAndPost(c);
    }

    private void drawCharacter(Canvas canvas,Character c) {
        canvas.drawBitmap(c.getGameCharType().getSprite
                (c.getAniIndex(), c.getFaceDir()), skeleton.getHitbox().left + cameraX, skeleton.getHitbox().top + cameraY, null);
    }

    private void drawPlayer(Canvas canvas) {
        canvas.drawBitmap(player.getGameCharType().getSprite
                (playerAntIndexY, playerFaceDir), PlayerX, PlayerY, null);
    }


    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        gameLoop = new GameLoop(this);

        gameLoop.startGameLoop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {


            float newX = event.getX();
            float newY = event.getY();

            float xDiff = Math.abs(newX - PlayerX);
            float yDiff = Math.abs(newY - PlayerY);

            if (xDiff > yDiff) {
                if (newX > PlayerX)
                    playerFaceDir = GameConstants.FACE_DIR.RIGHT;
                else
                    playerFaceDir = GameConstants.FACE_DIR.LEFT;
            } else {
                if (newY > PlayerY)
                    playerFaceDir = GameConstants.FACE_DIR.DOWN;
                else
                    playerFaceDir = GameConstants.FACE_DIR.UP;
            }

        }

        return touchEvents.touchEvent(event);
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

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
