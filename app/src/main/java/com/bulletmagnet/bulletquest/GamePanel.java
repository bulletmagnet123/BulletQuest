package com.bulletmagnet.bulletquest;

import static com.bulletmagnet.bulletquest.MainActivity.GAME_HEIGHT;
import static com.bulletmagnet.bulletquest.MainActivity.GAME_WIDTH;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.bulletmagnet.bulletquest.entities.GameCharacters;
import com.bulletmagnet.bulletquest.helpers.GameConstants;
import com.bulletmagnet.bulletquest.inputs.TouchEvents;

import java.util.Random;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    private Paint redPaint = new Paint();
    private SurfaceHolder holder;
    private float PlayerX, PlayerY;

    private float SkeletonX, SkeletonY;

    private boolean movePlayer;
    private PointF lastTouchDiff;

    private TouchEvents touchEvents;

    private Random rand = new Random();


    private GameLoop gameLoop;

    private Paint BluePaint = new Paint();
    private Paint RedPaint = new Paint();

    public static GameCharacters PLAYER = GameCharacters.PLAYER;
    public static GameCharacters SKELETON = GameCharacters.SKELETON;

    private PointF skeletonPos;
    private int skeletonDir;

    private int playerAntIndexY, playerFaceDir;
    private int aniTick;

    private int skeletonAnimationIndexY;
    private int aniSpeed = 10;



    public GamePanel(Context context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
        gameLoop = new GameLoop(this);
        BluePaint = new Paint(Color.BLUE);
        RedPaint = new Paint(Color.RED);

        skeletonPos = new PointF(rand.nextInt(GAME_WIDTH), rand.nextInt(GAME_HEIGHT));

        touchEvents = new TouchEvents(this);

        PlayerX = 200;
        PlayerY = 200;


    }

    public void update(double delta){
        /*for (PointF pos : skeletons) {
            pos.y += delta * 300;

            if (pos.y >= 1920)
                pos.y = 0;
        }*/

        switch (skeletonDir){
            case GameConstants.FACE_DIR.DOWN:
                skeletonPos.y += (float) (delta * 300);
                if (skeletonPos.y >= GAME_HEIGHT)
                    skeletonDir = GameConstants.FACE_DIR.UP;
                break;
            case GameConstants.FACE_DIR.UP:
                skeletonPos.y -= (float) (delta * 300);
                if (skeletonPos.y <= 0)
                    skeletonDir = GameConstants.FACE_DIR.DOWN;
                break;
            case GameConstants.FACE_DIR.LEFT:
                skeletonPos.x += (float) (delta * 300);
                if (skeletonPos.x >= GAME_WIDTH)
                    skeletonDir = GameConstants.FACE_DIR.RIGHT;
                break;
            case GameConstants.FACE_DIR.RIGHT:
                skeletonPos.x -= (float) (delta * 300);
                if (skeletonPos.x <= 0)
                    skeletonDir = GameConstants.FACE_DIR.LEFT;
                break;
        }

        updatePlayerMove(delta);

        updateAnimation();
    }

    private void updatePlayerMove(double delta) {
        if (!movePlayer || lastTouchDiff == null)
            return;
        float baseSpeed = (float) (delta * 300);
        float ratio = Math.abs(lastTouchDiff.y) / Math.abs(lastTouchDiff.x);
        double angle = Math.atan(ratio);
        float xSpeed = (float) Math.cos(angle);
        float ySpeed = (float) Math.sin(angle);
        if (xSpeed > ySpeed) {
            if (lastTouchDiff.x > 0) playerFaceDir = GameConstants.FACE_DIR.RIGHT;
            else playerFaceDir = GameConstants.FACE_DIR.LEFT;
        } else {
            if (lastTouchDiff.y > 0) playerFaceDir = GameConstants.FACE_DIR.DOWN;
            else playerFaceDir = GameConstants.FACE_DIR.UP;
        }
        if (lastTouchDiff.x < 0)
            xSpeed *= -1;
        if (lastTouchDiff.y < 0)
            ySpeed *= -1;
        PlayerX += xSpeed * baseSpeed;
        PlayerY += ySpeed * baseSpeed;
    }

    private void updateAnimation(){
        aniTick++; // Increment the animation tick regardless of player movement

        if (aniTick >= aniSpeed) {
            aniTick = 0;

            // --- Skeleton Animation (Always Animate) ---
            skeletonAnimationIndexY++;
            if (skeletonAnimationIndexY >= GameCharacters.SKELETON.getAnimationFrames(skeletonDir)) { // Use frame count from GameCharacters
                skeletonAnimationIndexY = 0;
            }

            // --- Player Animation (Only Animate if movePlayer is true) ---
            if (movePlayer) {
                playerAntIndexY++;
                if (playerAntIndexY >= GameCharacters.PLAYER.getAnimationFrames(playerFaceDir)) { // Use frame count from GameCharacters
                    playerAntIndexY = 0;
                }
            } else {
                return;
            }
        }
    }


    public void render(){
        Canvas c = holder.lockCanvas();
        c.drawColor(Color.BLACK);

        touchEvents.draw(c);



        c.drawBitmap(GameCharacters.PLAYER.getSprite(playerAntIndexY, playerFaceDir), PlayerX, PlayerY, null);


        c.drawBitmap(GameCharacters.SKELETON.getSprite(skeletonAnimationIndexY, skeletonDir), skeletonPos.x, skeletonPos.y, null);

        /*for (PointF pos : skeletons)
            c.drawBitmap(GameCharacters.SKELETON.getSprite(0, 0), pos.x, pos.y, null); */

        


        holder.unlockCanvasAndPost(c);
    }



    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        gameLoop = new GameLoop(this);

        PlayerX = 200;
        PlayerY = 200;



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
    public void setPlayerMoveTrue(PointF lastTouchDiff){
        movePlayer = true;
        this.lastTouchDiff = lastTouchDiff;
    }

    public void setPlayerMoveFalse(){
        movePlayer = false;

        resetAnimation();
    }

    private void resetAnimation() {
        aniTick = 0;
        playerAntIndexY = 0;
    }
}
