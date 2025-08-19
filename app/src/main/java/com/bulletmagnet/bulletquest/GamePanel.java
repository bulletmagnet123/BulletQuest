package com.bulletmagnet.bulletquest;

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

import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    private Paint redPaint = new Paint();
    private SurfaceHolder holder;
    private float x,y;

    private ArrayList<PointF> skeletons = new ArrayList<>();
    private ArrayList<PointF> Player = new ArrayList<>();

    private Random rand = new Random();


    private GameLoop gameLoop;

    private Paint BluePaint = new Paint();
    private Paint RedPaint = new Paint();

    public static GameCharacters PLAYER = GameCharacters.PLAYER;
    public static GameCharacters SKELETON = GameCharacters.SKELETON;

    private PointF skeletonPos;
    private int skeletonDir = GameConstants.FACE_DIR.DOWN;

    private int playerAntIndexY, playerFaceDir = GameConstants.FACE_DIR.DOWN;
    private int aniTick;
    private int aniSpeed = 10;



    public GamePanel(Context context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
        gameLoop = new GameLoop(this);
        BluePaint = new Paint(Color.BLUE);
        RedPaint = new Paint(Color.RED);

        skeletonPos = new PointF(rand.nextInt(1080), rand.nextInt(1920));

        x = 500;
        y = 500;


        /*for (int i = 0; i < 50; i++)
            skeletons.add(new PointF(rand.nextInt(1080), rand.nextInt(1920)));*/
    }

    public void update(double delta){
        /*for (PointF pos : skeletons) {
            pos.y += delta * 300;

            if (pos.y >= 1920)
                pos.y = 0;
        }*/

        switch (skeletonDir){
            case GameConstants.FACE_DIR.DOWN:
                skeletonPos.y += delta * 300;
                if (skeletonPos.y >= 1920)
                    skeletonDir = GameConstants.FACE_DIR.UP;
                break;
            case GameConstants.FACE_DIR.UP:
                skeletonPos.y -= delta * 300;
                if (skeletonPos.y <= 0)
                    skeletonDir = GameConstants.FACE_DIR.DOWN;
                break;
            case GameConstants.FACE_DIR.LEFT:
                skeletonPos.x += delta * 300;
                if (skeletonPos.x >= 1080)
                    skeletonDir = GameConstants.FACE_DIR.RIGHT;
                break;
            case GameConstants.FACE_DIR.RIGHT:
                skeletonPos.x -= delta * 300;
                if (skeletonPos.x <= 0)
                    skeletonDir = GameConstants.FACE_DIR.LEFT;
                break;
        }

        updateAnimation();
    }

    private void updateAnimation(){
        aniTick++;
        if (aniTick >= aniSpeed){
            aniTick = 0;
            playerAntIndexY++;
            if (playerAntIndexY >= 4)
                playerAntIndexY = 0;
        }
    }


    public void render(){
        Canvas c = holder.lockCanvas();
        c.drawColor(Color.BLACK);



        c.drawBitmap(GameCharacters.PLAYER.getSprite(playerAntIndexY, playerFaceDir), x, y, null);

        c.drawBitmap(GameCharacters.SKELETON.getSprite(playerAntIndexY, skeletonDir), skeletonPos.x, skeletonPos.y, null);

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

        gameLoop.startGameLoop();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {


            float newX = event.getX();
            float newY = event.getY();

            float xDiff = Math.abs(newX - x);
            float yDiff = Math.abs(newY - y);

            if (xDiff > yDiff) {
                if (newX > x)
                    playerFaceDir = GameConstants.FACE_DIR.RIGHT;
                else
                    playerFaceDir = GameConstants.FACE_DIR.LEFT;
            } else {
                if (newY > y)
                    playerFaceDir = GameConstants.FACE_DIR.DOWN;
                else
                    playerFaceDir = GameConstants.FACE_DIR.UP;
            }

            x = newX;
            y = newY;
        }
        return true;
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }
}
