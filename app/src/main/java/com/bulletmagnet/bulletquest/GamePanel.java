package com.bulletmagnet.bulletquest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    private Paint redPaint = new Paint();
    private SurfaceHolder holder;
    private float x,y;


    private GameLoop gameLoop;




    public GamePanel(Context context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);

        gameLoop = new GameLoop(this);


    }

    public void render(){
        Canvas c = holder.lockCanvas();
        c.drawColor(Color.BLUE);

        holder.unlockCanvasAndPost(c);


    }



    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {

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
