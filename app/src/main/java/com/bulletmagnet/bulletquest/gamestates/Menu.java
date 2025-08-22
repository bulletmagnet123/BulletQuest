package com.bulletmagnet.bulletquest.gamestates;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.bulletmagnet.bulletquest.helpers.interfaces.GameStateInterface;
import com.bulletmagnet.bulletquest.main.Game;

public class Menu extends BaseState implements GameStateInterface {

    private Paint paint;
    private Game game;


    public Menu(Game game) {
        super(game);
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(60);
        paint.setTextAlign(Paint.Align.CENTER);
        this.game = game;

    }
    @Override
    public void update(double delta) {

    }

    @Override
    public void render(Canvas c) {
        c.drawText("MENU!", 800, 200, paint);
    }

    @Override
    public void touchEvents(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
            game.setCurrentGameState(Game.GameState.PLAYING);
    }


}
