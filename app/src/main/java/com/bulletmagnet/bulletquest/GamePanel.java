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

    private int playerAntIndexY, playerAntIndexX;
    private int aniTick;
    private int aniSpeed;


    public GamePanel(Context context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
        gameLoop = new GameLoop(this);
        BluePaint = new Paint(Color.BLUE);
        RedPaint = new Paint(Color.RED);

        for (int i = 0; i < 50; i++)
            skeletons.add(new PointF(rand.nextInt(1080), rand.nextInt(1920)));
    }

    public void update(double delta){
        for (PointF pos : skeletons) {
            pos.y += delta * 300;

            if (pos.y >= 1920)
                pos.y = 0;

        }
    }


    public void render(){
        Canvas c = holder.lockCanvas();
        c.drawColor(Color.BLACK);



        c.drawBitmap(GameCharacters.PLAYER.getSprite(6, 3), x, y, null);

        for (PointF pos : skeletons)
            c.drawBitmap(GameCharacters.SKELETON.getSprite(0, 0), pos.x, pos.y, null);


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
        x = event.getX();
        y = event.getY();

        if(event.getAction() == MotionEvent.ACTION_DOWN){

        }
        return true;
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }
}
